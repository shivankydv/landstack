package com.landstack.backend.service;

import com.landstack.backend.dto.LandParcelDto;
import com.landstack.backend.entity.LandParcel;
import com.landstack.backend.repository.LandParcelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
                        new NoSuchElementException("Parcel not found: " + ulpin)
                );

        return toDto(parcel);
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

        LandParcel parcel = new LandParcel();

        parcel.setUlpin(dto.getUlpin());
        parcel.setName(dto.getName());
        parcel.setPropertyType(dto.getPropertyType());
        parcel.setPlotReference(dto.getPlotReference());
        parcel.setArea(dto.getArea());
        parcel.setAreaUnit(dto.getAreaUnit());
        parcel.setAddress(dto.getAddress());
        parcel.setDemoData(dto.isDemoData());

        if (dto.getLatitude() != null && dto.getLongitude() != null) {
            org.locationtech.jts.geom.GeometryFactory geometryFactory =
                    new org.locationtech.jts.geom.GeometryFactory();

            org.locationtech.jts.geom.Point point =
                    geometryFactory.createPoint(
                            new org.locationtech.jts.geom.Coordinate(
                                    dto.getLongitude(),
                                    dto.getLatitude()
                            )
                    );

            point.setSRID(4326);
            parcel.setGeometry(point);
        }

        LandParcel savedParcel = landParcelRepository.save(parcel);

        return toDto(savedParcel);
    }
}