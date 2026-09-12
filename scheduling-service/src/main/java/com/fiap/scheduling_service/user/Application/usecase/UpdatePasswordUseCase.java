package com.fiap.scheduling_service.user.Application.usecase;

import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

public class UpdatePasswordUseCase {

    private final UserRepository userRepository;

    public UpdatePasswordUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID id, String newPasswordHash){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        user.updatePassword(newPasswordHash);
        userRepository.save(user);
    }
}
