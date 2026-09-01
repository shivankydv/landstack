package com.landstack.backend.controller;

import com.landstack.backend.dto.LandParcelDto;
import com.landstack.backend.service.LandParcelService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> deleteParcel(@PathVariable String ulpin) {
        landParcelService.deleteParcel(ulpin);
        return ResponseEntity.noContent().build();
    }
}