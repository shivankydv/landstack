package com.landstack.backend.controller;

import com.landstack.backend.dto.LandParcelDto;
import com.landstack.backend.service.LandParcelService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parcels")
public class LandParcelController {

    private final LandParcelService landParcelService;

    public LandParcelController(LandParcelService landParcelService) {
        this.landParcelService = landParcelService;
    }

    @GetMapping
    public List<LandParcelDto> getAllParcels() {
        return landParcelService.getAllParcels();
    }

    @GetMapping("/search")
    public Page<LandParcelDto> searchParcels(
            @RequestParam(required = false) String ulpin,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double minArea,
            @RequestParam(required = false) Double maxArea,
            @PageableDefault(size = 10) Pageable pageable) {

        return landParcelService.searchParcels(
                ulpin,
                name,
                propertyType,
                address,
                minArea,
                maxArea,
                pageable
        );
    }

    @GetMapping("/{ulpin}")
    public LandParcelDto getParcel(@PathVariable String ulpin) {
        return landParcelService.getByUlpin(ulpin);
    }

    @PostMapping
    public LandParcelDto createParcel(
            @Valid @RequestBody LandParcelDto dto) {

        return landParcelService.createParcel(dto);
    }

    @PutMapping("/{ulpin}")
    public LandParcelDto updateParcel(
            @PathVariable String ulpin,
            @Valid @RequestBody LandParcelDto dto) {

        return landParcelService.updateParcel(ulpin, dto);
    }

    @DeleteMapping("/{ulpin}")
    public ResponseEntity<Void> deleteParcel(
            @PathVariable String ulpin) {

        landParcelService.deleteParcel(ulpin);

        return ResponseEntity.noContent().build();
    }
}