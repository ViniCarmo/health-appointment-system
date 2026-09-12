package com.fiap.scheduling_service.user.interfaces.dto.response;

import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto(UUID id, String email, String name, Role role,
                              String phoneNumber, LocalDateTime createdAt, LocalDateTime updatedAt
                              ) {

    public static UserResponseDto from(User user){
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
