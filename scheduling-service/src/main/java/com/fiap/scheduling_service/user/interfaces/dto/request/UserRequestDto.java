package com.fiap.scheduling_service.user.interfaces.dto.request;

import com.fiap.scheduling_service.user.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDto(
        @Email(message = "Invalid Email")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String passwordHash,

        @NotNull(message = "Role is required")
        Role role,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Phone number is required")
        String phoneNumber) {
}
