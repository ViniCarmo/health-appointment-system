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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoderService passwordEncoderService;

    private CreateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateUserUseCase(userRepository, passwordEncoderService);
    }

    @Test
    void shouldCreateUserWithEncodedPassword() {
        when(userRepository.findByEmailIgnoreCase("new@test.com")).thenReturn(Optional.empty());
        when(passwordEncoderService.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = useCase.execute("new@test.com", "rawPassword", Role.PATIENT, "New User", "111");

        assertThat(result.getEmail()).isEqualTo("new@test.com");
        assertThat(result.getPasswordHash()).isEqualTo("encodedPassword");
        assertThat(result.getRole()).isEqualTo(Role.PATIENT);
    }

    @Test
    void shouldRejectDuplicateEmail() {
        when(userRepository.findByEmailIgnoreCase("existing@test.com")).thenReturn(Optional.of(
                User.create("existing@test.com", "h", Role.PATIENT, "Existing", "111")));

        assertThatThrownBy(() -> useCase.execute("existing@test.com", "rawPassword", Role.PATIENT, "New User", "111"))
                .isInstanceOf(IllegalArgumentException.class);

        verify(userRepository, never()).save(any());
    }
}
