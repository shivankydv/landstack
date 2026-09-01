package com.landstack.backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class OwnershipDto {

    private Long id;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    @NotNull(message = "Parcel ULPIN is required")
    private String parcelUlpin;

    @Size(max = 50, message = "Ownership type must not exceed 50 characters")
    private String ownershipType;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Ownership percentage must be greater than 0"
    )
    @DecimalMax(
            value = "100.0",
            message = "Ownership percentage cannot exceed 100"
    )
    private Double ownershipPercentage;

    private LocalDate validFrom;

    private LocalDate validTo;

    private boolean current = true;


    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getParcelUlpin() {
        return parcelUlpin;
    }

    public void setParcelUlpin(String parcelUlpin) {
        this.parcelUlpin = parcelUlpin;
    }

    public String getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(String ownershipType) {
        this.ownershipType = ownershipType;
    }

    public Double getOwnershipPercentage() {
        return ownershipPercentage;
    }

    public void setOwnershipPercentage(Double ownershipPercentage) {
        this.ownershipPercentage = ownershipPercentage;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}