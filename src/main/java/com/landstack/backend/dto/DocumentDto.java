package com.landstack.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class DocumentDto {

    private Long id;

    @NotBlank(message = "Parcel ULPIN is required")
    private String parcelUlpin;

    @NotBlank(message = "Document type is required")
    @Size(max = 50, message = "Document type must not exceed 50 characters")
    private String documentType;

    @Size(max = 100, message = "Document number must not exceed 100 characters")
    private String documentNumber;

    private LocalDate documentDate;

    @Size(max = 30, message = "Status must not exceed 30 characters")
    private String status;

    @Size(max = 30, message = "Verification status must not exceed 30 characters")
    private String verificationStatus;


    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParcelUlpin() {
        return parcelUlpin;
    }

    public void setParcelUlpin(String parcelUlpin) {
        this.parcelUlpin = parcelUlpin;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}