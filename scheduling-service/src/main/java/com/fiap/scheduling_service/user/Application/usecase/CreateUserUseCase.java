package com.fiap.scheduling_service.user.Application.usecase;

import com.fiap.scheduling_service.user.domain.PasswordEncoderService;
import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.enums.Role;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    public CreateUserUseCase(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    public User execute(String email, String rawPassword, Role role, String name, String phoneNumber) {
        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new IllegalArgumentException("User with email " + email + " already exists.");
        }
        String encodedPassword = passwordEncoderService.encode(rawPassword);
        return userRepository.save(User.create(email, encodedPassword, role, name, phoneNumber));
    }
}
