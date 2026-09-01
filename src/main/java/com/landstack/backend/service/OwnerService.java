package com.landstack.backend.service;

import com.landstack.backend.dto.OwnerDto;
import com.landstack.backend.entity.Owner;
import com.landstack.backend.exception.ResourceNotFoundException;
import com.landstack.backend.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public List<OwnerDto> getAllOwners() {
        return ownerRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OwnerDto getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Owner not found: " + id
                        )
                );

        return toDto(owner);
    }

    public OwnerDto createOwner(OwnerDto dto) {

        Owner owner = new Owner();

        applyDtoToEntity(dto, owner);

        Owner savedOwner = ownerRepository.save(owner);

        return toDto(savedOwner);
    }

    public OwnerDto updateOwner(Long id, OwnerDto dto) {

        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Owner not found: " + id
                        )
                );

        applyDtoToEntity(dto, owner);

        Owner updatedOwner = ownerRepository.save(owner);

        return toDto(updatedOwner);
    }

    public void deleteOwner(Long id) {

        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Owner not found: " + id
                        )
                );

        ownerRepository.delete(owner);
    }

    private void applyDtoToEntity(OwnerDto dto, Owner owner) {

        owner.setName(dto.getName());
        owner.setEmail(dto.getEmail());
        owner.setPhone(dto.getPhone());
        owner.setAddress(dto.getAddress());
    }

    private OwnerDto toDto(Owner owner) {

        OwnerDto dto = new OwnerDto();

        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setEmail(owner.getEmail());
        dto.setPhone(owner.getPhone());
        dto.setAddress(owner.getAddress());

        return dto;
    }
}