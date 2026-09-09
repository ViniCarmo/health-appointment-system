package com.fiap.scheduling_service.user.Application.usecase;

import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.util.UUID;

public class UpdateUserContactInfoUseCase {

    private final UserRepository userRepository;

    public UpdateUserContactInfoUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UUID id, String name, String phoneNumber, String email){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.updateContactInfo(name, phoneNumber, email);
        return userRepository.save(user);
    }
}
