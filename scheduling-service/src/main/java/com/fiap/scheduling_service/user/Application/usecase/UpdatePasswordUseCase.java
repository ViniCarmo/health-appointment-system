package com.fiap.scheduling_service.user.Application.usecase;

import com.fiap.scheduling_service.user.domain.PasswordEncoderService;
import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

public class UpdatePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    public UpdatePasswordUseCase(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    public void execute(UUID id, String newRawPassword, UUID requestingUserId, boolean isPatientRole){
        if (isPatientRole && !id.equals(requestingUserId)) {
            throw new SecurityException("Patients can only update their own account");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        user.updatePassword(passwordEncoderService.encode(newRawPassword));
        userRepository.save(user);
    }
}
