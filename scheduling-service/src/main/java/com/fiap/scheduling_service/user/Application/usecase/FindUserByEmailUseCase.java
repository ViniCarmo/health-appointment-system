package com.fiap.scheduling_service.user.Application.usecase;

import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.util.NoSuchElementException;

public class FindUserByEmailUseCase {

    private final UserRepository userRepository;

    public FindUserByEmailUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));
    }
}
