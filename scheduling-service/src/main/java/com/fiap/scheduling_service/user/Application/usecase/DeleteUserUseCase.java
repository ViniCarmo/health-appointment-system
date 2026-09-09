package com.fiap.scheduling_service.user.Application.usecase;

import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.util.UUID;

public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID id){
        userRepository.deleteById(id);
    }
}
