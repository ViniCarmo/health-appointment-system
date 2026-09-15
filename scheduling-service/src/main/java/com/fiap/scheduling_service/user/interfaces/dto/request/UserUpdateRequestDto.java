package com.fiap.scheduling_service.user.interfaces.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequestDto(
        @Email(message = "Invalid Email")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Phone number is required")
        String phoneNumber) {
}
