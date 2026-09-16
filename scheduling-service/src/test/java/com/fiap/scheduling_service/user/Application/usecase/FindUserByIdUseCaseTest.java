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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private FindUserByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindUserByIdUseCase(userRepository);
    }

    @Test
    void shouldReturnUserWhenFound() {
        UUID id = UUID.randomUUID();
        User user = User.create("p@test.com", "h", Role.PATIENT, "Patient", "111");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = useCase.execute(id);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id)).isInstanceOf(NoSuchElementException.class);
    }
}
