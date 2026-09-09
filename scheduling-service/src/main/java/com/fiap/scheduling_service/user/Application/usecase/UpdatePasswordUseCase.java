package com.fiap.scheduling_service.user.Application.usecase;

import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.util.UUID;

public class UpdatePasswordUseCase {

    private final UserRepository userRepository;

    public UpdatePasswordUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID id, String newPasswordHash){
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id))
                .updatePassword(newPasswordHash);
    }
}
