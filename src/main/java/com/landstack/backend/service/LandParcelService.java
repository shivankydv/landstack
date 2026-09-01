package com.landstack.backend.service;

import com.landstack.backend.dto.LandParcelDto;
import com.landstack.backend.entity.LandParcel;
import com.landstack.backend.exception.DuplicateResourceException;
import com.landstack.backend.exception.ResourceNotFoundException;
import com.landstack.backend.repository.LandParcelRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Predicate;

@Service
public class LandParcelService {

    private final LandParcelRepository landParcelRepository;

    public LandParcelService(LandParcelRepository landParcelRepository) {
        this.landParcelRepository = landParcelRepository;
    }

    public List<LandParcelDto> getAllParcels() {
        return landParcelRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public LandParcelDto getByUlpin(String ulpin) {
        LandParcel parcel = landParcelRepository.findByUlpin(ulpin)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parcel not found: " + ulpin
                        )
                );

        return toDto(parcel);
    }

    public Page<LandParcelDto> searchParcels(
            String ulpin,
            String name,
            String propertyType,
            String address,
            Double minArea,
            Double maxArea,
            Pageable pageable) {

        Specification<LandParcel> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (ulpin != null && !ulpin.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("ulpin"), ulpin)
                );
            }

            if (name != null && !name.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            if (propertyType != null && !propertyType.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("propertyType")),
                                propertyType.toLowerCase()
                        )
                );
            }

            if (address != null && !address.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("address")),
                                "%" + address.toLowerCase() + "%"
                        )
                );
            }

            if (minArea != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("area"),
                                minArea
                        )
                );
            }

            if (maxArea != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("area"),
                                maxArea
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        return landParcelRepository
                .findAll(specification, pageable)
                .map(this::toDto);
    }

    private LandParcelDto toDto(LandParcel parcel) {
        LandParcelDto dto = new LandParcelDto();

        dto.setUlpin(parcel.getUlpin());
        dto.setName(parcel.getName());
        dto.setPropertyType(parcel.getPropertyType());
        dto.setPlotReference(parcel.getPlotReference());
        dto.setArea(parcel.getArea());
        dto.setAreaUnit(parcel.getAreaUnit());
        dto.setAddress(parcel.getAddress());
        dto.setDemoData(parcel.isDemoData());

        if (parcel.getGeometry() != null) {
            dto.setLatitude(parcel.getGeometry().getY());
            dto.setLongitude(parcel.getGeometry().getX());
        }

        return dto;
    }

    public LandParcelDto createParcel(LandParcelDto dto) {

        if (landParcelRepository.existsByUlpin(dto.getUlpin())) {
            throw new DuplicateResourceException(
                    "Parcel already exists with ULPIN: " + dto.getUlpin()
            );
        }

        LandParcel parcel = new LandParcel();

        parcel.setUlpin(dto.getUlpin());

        applyDtoToEntity(dto, parcel);

        LandParcel savedParcel = landParcelRepository.save(parcel);

        return toDto(savedParcel);
    }

    public LandParcelDto updateParcel(String ulpin, LandParcelDto dto) {

        LandParcel parcel = landParcelRepository.findByUlpin(ulpin)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parcel not found: " + ulpin
                        )
                );

        // ULPIN itself is not changed via update; it is the lookup key
        applyDtoToEntity(dto, parcel);

        LandParcel updatedParcel = landParcelRepository.save(parcel);

        return toDto(updatedParcel);
    }

    public void deleteParcel(String ulpin) {

        LandParcel parcel = landParcelRepository.findByUlpin(ulpin)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parcel not found: " + ulpin
                        )
                );

        landParcelRepository.delete(parcel);
    }

    private void applyDtoToEntity(LandParcelDto dto, LandParcel parcel) {

        parcel.setName(dto.getName());
        parcel.setPropertyType(dto.getPropertyType());
        parcel.setPlotReference(dto.getPlotReference());
        parcel.setArea(dto.getArea());
        parcel.setAreaUnit(dto.getAreaUnit());
        parcel.setAddress(dto.getAddress());
        parcel.setDemoData(dto.isDemoData());

        if (dto.getLatitude() != null && dto.getLongitude() != null) {

            GeometryFactory geometryFactory = new GeometryFactory();

            Point point = geometryFactory.createPoint(
                    new Coordinate(
                            dto.getLongitude(),
                            dto.getLatitude()
                    )
            );

            point.setSRID(4326);

            parcel.setGeometry(point);
        }
    }
}