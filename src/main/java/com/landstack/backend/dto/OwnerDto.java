package com.landstack.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OwnerDto {

    private Long id;

    @NotBlank(message = "Owner name is required")
    @Size(max = 150, message = "Owner name must not exceed 150 characters")
    private String name;

    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Pattern(
            regexp = "^[0-9+()\\-\\s]*$",
            message = "Invalid phone number format"
    )
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;

    private String address;


    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}