package com.fiap.scheduling_service.user.infrastructure.config;

import com.fiap.scheduling_service.user.Application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

@Configuration
public class UserUseCaseConfig {
    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository) {
        return new CreateUserUseCase(userRepository);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(UserRepository userRepository) {
        return new FindUserByIdUseCase(userRepository);
    }

    @Bean
    public FindUserByEmailUseCase findUserByEmailUseCase(UserRepository userRepository) {
        return new FindUserByEmailUseCase(userRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository) {
        return new DeleteUserUseCase(userRepository);
    }

    @Bean
    public UpdateUserContactInfoUseCase updateUserContactInfoUseCase(UserRepository userRepository) {
        return new UpdateUserContactInfoUseCase(userRepository);
    }

    @Bean
    public UpdatePasswordUseCase updatePasswordUseCase(UserRepository userRepository) {
        return new UpdatePasswordUseCase(userRepository);
    }

}
