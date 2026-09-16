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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByEmailUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private FindUserByEmailUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindUserByEmailUseCase(userRepository);
    }

    @Test
    void shouldReturnUserWhenFound() {
        User user = User.create("p@test.com", "h", Role.PATIENT, "Patient", "111");
        when(userRepository.findByEmailIgnoreCase("p@test.com")).thenReturn(Optional.of(user));

        User result = useCase.execute("p@test.com");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(userRepository.findByEmailIgnoreCase("missing@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("missing@test.com")).isInstanceOf(NoSuchElementException.class);
    }
}
