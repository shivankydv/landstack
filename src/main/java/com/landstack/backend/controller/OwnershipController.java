package com.landstack.backend.controller;

import com.landstack.backend.dto.OwnershipDto;
import com.landstack.backend.service.OwnershipService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ownerships")
public class OwnershipController {

    private final OwnershipService ownershipService;

    public OwnershipController(OwnershipService ownershipService) {
        this.ownershipService = ownershipService;
    }

    @GetMapping
    public List<OwnershipDto> getAllOwnerships() {
        return ownershipService.getAllOwnerships();
    }

    @GetMapping("/{id}")
    public OwnershipDto getOwnership(@PathVariable Long id) {
        return ownershipService.getOwnershipById(id);
    }

    @GetMapping("/owner/{ownerId}")
    public List<OwnershipDto> getOwnershipsByOwner(
            @PathVariable Long ownerId) {

        return ownershipService.getOwnershipsByOwner(ownerId);
    }

    @GetMapping("/parcel/{ulpin}")
    public List<OwnershipDto> getOwnershipsByParcel(
            @PathVariable String ulpin) {

        return ownershipService.getOwnershipsByParcel(ulpin);
    }

    @PostMapping
    public OwnershipDto createOwnership(
            @Valid @RequestBody OwnershipDto dto) {

        return ownershipService.createOwnership(dto);
    }

    @PutMapping("/{id}")
    public OwnershipDto updateOwnership(
            @PathVariable Long id,
            @Valid @RequestBody OwnershipDto dto) {

        return ownershipService.updateOwnership(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwnership(
            @PathVariable Long id) {

        ownershipService.deleteOwnership(id);

        return ResponseEntity.noContent().build();
    }
}