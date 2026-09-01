package com.landstack.backend.controller;

import com.landstack.backend.dto.OwnerDto;
import com.landstack.backend.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public List<OwnerDto> getAllOwners() {
        return ownerService.getAllOwners();
    }

    @GetMapping("/{id}")
    public OwnerDto getOwner(@PathVariable Long id) {
        return ownerService.getOwnerById(id);
    }

    @PostMapping
    public OwnerDto createOwner(
            @Valid @RequestBody OwnerDto dto) {

        return ownerService.createOwner(dto);
    }

    @PutMapping("/{id}")
    public OwnerDto updateOwner(
            @PathVariable Long id,
            @Valid @RequestBody OwnerDto dto) {

        return ownerService.updateOwner(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(
            @PathVariable Long id) {

        ownerService.deleteOwner(id);

        return ResponseEntity.noContent().build();
    }
}