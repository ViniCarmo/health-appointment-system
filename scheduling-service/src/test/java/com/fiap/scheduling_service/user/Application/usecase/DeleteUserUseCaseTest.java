package com.fiap.scheduling_service.user.Application.usecase;

import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.enums.Role;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private DeleteUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteUserUseCase(userRepository);
    }

    @Test
    void staffShouldDeleteAnyUser() {
        UUID targetId = UUID.randomUUID();
        when(userRepository.findById(targetId)).thenReturn(Optional.of(
                User.create("p@test.com", "h", Role.PATIENT, "Patient", "111")));

        useCase.execute(targetId, UUID.randomUUID(), false);

        verify(userRepository).deleteById(targetId);
    }

    @Test
    void patientShouldDeleteOwnAccount() {
        UUID targetId = UUID.randomUUID();
        when(userRepository.findById(targetId)).thenReturn(Optional.of(
                User.create("p@test.com", "h", Role.PATIENT, "Patient", "111")));

        useCase.execute(targetId, targetId, true);

        verify(userRepository).deleteById(targetId);
    }

    @Test
    void patientShouldNotDeleteOthersAccount() {
        UUID targetId = UUID.randomUUID();

        assertThatThrownBy(() -> useCase.execute(targetId, UUID.randomUUID(), true))
                .isInstanceOf(SecurityException.class);

        verify(userRepository, never()).deleteById(targetId);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID targetId = UUID.randomUUID();
        when(userRepository.findById(targetId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(targetId, UUID.randomUUID(), false))
                .isInstanceOf(NoSuchElementException.class);

        verify(userRepository, never()).deleteById(targetId);
    }
}
