package com.landstack.backend.controller;

import com.landstack.backend.dto.LandParcelDto;
import com.landstack.backend.service.LandParcelService;
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
    public LandParcelDto createParcel(@RequestBody LandParcelDto dto) {
        return landParcelService.createParcel(dto);
    }
}