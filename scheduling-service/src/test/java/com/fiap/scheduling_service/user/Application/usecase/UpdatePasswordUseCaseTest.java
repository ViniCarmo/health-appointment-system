package com.fiap.scheduling_service.user.Application.usecase;

import com.fiap.scheduling_service.user.domain.PasswordEncoderService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePasswordUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoderService passwordEncoderService;

    private UpdatePasswordUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdatePasswordUseCase(userRepository, passwordEncoderService);
    }

    @Test
    void patientShouldUpdateOwnPassword() {
        UUID id = UUID.randomUUID();
        User user = User.create("p@test.com", "oldHash", Role.PATIENT, "Patient", "111");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoderService.encode("newRawPassword")).thenReturn("newHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(id, "newRawPassword", id, true);

        assertThat(user.getPasswordHash()).isEqualTo("newHash");
        verify(userRepository).save(user);
    }

    @Test
    void patientShouldNotUpdateOthersPassword() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> useCase.execute(id, "newRawPassword", UUID.randomUUID(), true))
                .isInstanceOf(SecurityException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id, "newRawPassword", UUID.randomUUID(), false))
                .isInstanceOf(NoSuchElementException.class);
    }
}
