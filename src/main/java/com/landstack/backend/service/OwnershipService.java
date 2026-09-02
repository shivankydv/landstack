package com.landstack.backend.service;

import com.landstack.backend.dto.OwnershipDto;
import com.landstack.backend.entity.LandParcel;
import com.landstack.backend.entity.Owner;
import com.landstack.backend.entity.Ownership;
import com.landstack.backend.exception.ResourceNotFoundException;
import com.landstack.backend.repository.LandParcelRepository;
import com.landstack.backend.repository.OwnerRepository;
import com.landstack.backend.repository.OwnershipRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OwnershipService {

    private final OwnershipRepository ownershipRepository;
    private final OwnerRepository ownerRepository;
    private final LandParcelRepository landParcelRepository;
    private final AuditLogService auditLogService;

    public OwnershipService(
            OwnershipRepository ownershipRepository,
            OwnerRepository ownerRepository,
            LandParcelRepository landParcelRepository,
            AuditLogService auditLogService) {

        this.ownershipRepository = ownershipRepository;
        this.ownerRepository = ownerRepository;
        this.landParcelRepository = landParcelRepository;
        this.auditLogService = auditLogService;
    }

    // =========================
    // Get all ownerships
    // =========================

    public List<OwnershipDto> getAllOwnerships() {

        return ownershipRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // =========================
    // Get ownership by ID
    // =========================

    public OwnershipDto getOwnershipById(Long id) {

        Ownership ownership = ownershipRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ownership record not found: " + id
                        )
                );

        return toDto(ownership);
    }

    // =========================
    // Get ownerships by owner
    // =========================

    public List<OwnershipDto> getOwnershipsByOwner(
            Long ownerId) {

        if (!ownerRepository.existsById(ownerId)) {
            throw new ResourceNotFoundException(
                    "Owner not found: " + ownerId
            );
        }

        return ownershipRepository
                .findByOwnerId(ownerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // =========================
    // Get ownerships by parcel
    // =========================

    public List<OwnershipDto> getOwnershipsByParcel(
            String ulpin) {

        if (!landParcelRepository.existsByUlpin(ulpin)) {
            throw new ResourceNotFoundException(
                    "Parcel not found: " + ulpin
            );
        }

        return ownershipRepository
                .findByParcelUlpin(ulpin)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // =========================
    // Create ownership
    // =========================

    public OwnershipDto createOwnership(
            OwnershipDto dto) {

        Owner owner = ownerRepository
                .findById(dto.getOwnerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Owner not found: "
                                        + dto.getOwnerId()
                        )
                );

        LandParcel parcel = landParcelRepository
                .findByUlpin(dto.getParcelUlpin())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parcel not found: "
                                        + dto.getParcelUlpin()
                        )
                );

        validateOwnershipRules(dto, null);

        Ownership ownership = new Ownership();

        ownership.setOwner(owner);
        ownership.setParcel(parcel);
        ownership.setOwnershipType(
                dto.getOwnershipType()
        );
        ownership.setOwnershipPercentage(
                dto.getOwnershipPercentage()
        );
        ownership.setValidFrom(dto.getValidFrom());
        ownership.setValidTo(dto.getValidTo());
        ownership.setCurrent(dto.isCurrent());

        Ownership savedOwnership =
                ownershipRepository.save(ownership);

        // Automatic audit log
        auditLogService.log(
                "OWNERSHIP",
                String.valueOf(savedOwnership.getId()),
                "CREATED",
                "system",
                "Ownership record created for parcel "
                        + savedOwnership.getParcel().getUlpin()
        );

        return toDto(savedOwnership);
    }

    // =========================
    // Update ownership
    // =========================

    public OwnershipDto updateOwnership(
            Long id,
            OwnershipDto dto) {

        Ownership ownership =
                ownershipRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Ownership record not found: "
                                                + id
                                )
                        );

        Owner owner = ownerRepository
                .findById(dto.getOwnerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Owner not found: "
                                        + dto.getOwnerId()
                        )
                );

        LandParcel parcel = landParcelRepository
                .findByUlpin(dto.getParcelUlpin())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parcel not found: "
                                        + dto.getParcelUlpin()
                        )
                );

        validateOwnershipRules(dto, ownership);

        ownership.setOwner(owner);
        ownership.setParcel(parcel);
        ownership.setOwnershipType(
                dto.getOwnershipType()
        );
        ownership.setOwnershipPercentage(
                dto.getOwnershipPercentage()
        );
        ownership.setValidFrom(dto.getValidFrom());
        ownership.setValidTo(dto.getValidTo());
        ownership.setCurrent(dto.isCurrent());

        Ownership updatedOwnership =
                ownershipRepository.save(ownership);

        // Automatic audit log
        auditLogService.log(
                "OWNERSHIP",
                String.valueOf(updatedOwnership.getId()),
                "UPDATED",
                "system",
                "Ownership record updated"
        );

        return toDto(updatedOwnership);
    }

    // =========================
    // Delete ownership
    // =========================

    public void deleteOwnership(Long id) {

        Ownership ownership =
                ownershipRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Ownership record not found: "
                                                + id
                                )
                        );

        // Create audit before deleting
        auditLogService.log(
                "OWNERSHIP",
                String.valueOf(ownership.getId()),
                "DELETED",
                "system",
                "Ownership record deleted"
        );

        ownershipRepository.delete(ownership);
    }

    // =========================
    // Ownership validation
    // =========================

    private void validateOwnershipRules(
            OwnershipDto dto,
            Ownership existingOwnership) {

        /*
         * Rule 1:
         * SOLE ownership must represent 100%.
         */
        if ("SOLE".equalsIgnoreCase(
                dto.getOwnershipType())
                && dto.getOwnershipPercentage() != null
                && dto.getOwnershipPercentage() != 100.0) {

            throw new IllegalArgumentException(
                    "SOLE ownership must have 100% ownership"
            );
        }

        /*
         * Rule 2:
         * Current ownership percentages for a parcel
         * cannot exceed 100%.
         */
        if (dto.isCurrent()
                && dto.getOwnershipPercentage() != null) {

            double existingPercentage =
                    ownershipRepository
                            .findByParcelUlpinAndIsCurrentTrue(
                                    dto.getParcelUlpin()
                            )
                            .stream()
                            .filter(existing ->
                                    existingOwnership == null
                                            || !Objects.equals(
                                            existing.getId(),
                                            existingOwnership.getId()
                                    )
                            )
                            .map(Ownership::getOwnershipPercentage)
                            .filter(Objects::nonNull)
                            .mapToDouble(Double::doubleValue)
                            .sum();

            double totalPercentage =
                    existingPercentage
                            + dto.getOwnershipPercentage();

            if (totalPercentage > 100.0) {

                throw new IllegalArgumentException(
                        "Total current ownership percentage "
                                + "cannot exceed 100%"
                );
            }
        }

        /*
         * Rule 3:
         * validTo cannot be before validFrom.
         */
        if (dto.getValidFrom() != null
                && dto.getValidTo() != null
                && dto.getValidTo().isBefore(
                dto.getValidFrom())) {

            throw new IllegalArgumentException(
                    "Valid-to date cannot be before "
                            + "valid-from date"
            );
        }
    }

    // =========================
    // Entity → DTO
    // =========================

    private OwnershipDto toDto(
            Ownership ownership) {

        OwnershipDto dto = new OwnershipDto();

        dto.setId(ownership.getId());

        dto.setOwnerId(
                ownership.getOwner().getId()
        );

        dto.setParcelUlpin(
                ownership.getParcel().getUlpin()
        );

        dto.setOwnershipType(
                ownership.getOwnershipType()
        );

        dto.setOwnershipPercentage(
                ownership.getOwnershipPercentage()
        );

        dto.setValidFrom(
                ownership.getValidFrom()
        );

        dto.setValidTo(
                ownership.getValidTo()
        );

        dto.setCurrent(
                ownership.isCurrent()
        );

        return dto;
    }
}