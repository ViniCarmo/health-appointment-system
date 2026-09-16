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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserContactInfoUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private UpdateUserContactInfoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserContactInfoUseCase(userRepository);
    }

    @Test
    void patientShouldUpdateOwnContactInfo() {
        UUID id = UUID.randomUUID();
        User user = User.create("old@test.com", "h", Role.PATIENT, "Old Name", "000");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = useCase.execute(id, "New Name", "111222333", "new@test.com", id, true);

        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getPhoneNumber()).isEqualTo("111222333");
        assertThat(result.getEmail()).isEqualTo("new@test.com");
    }

    @Test
    void patientShouldNotUpdateOthersContactInfo() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> useCase.execute(id, "New Name", "111", "new@test.com", UUID.randomUUID(), true))
                .isInstanceOf(SecurityException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void staffShouldUpdateAnyUsersContactInfo() {
        UUID id = UUID.randomUUID();
        User user = User.create("old@test.com", "h", Role.PATIENT, "Old Name", "000");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = useCase.execute(id, "New Name", "111", "new@test.com", UUID.randomUUID(), false);

        assertThat(result.getName()).isEqualTo("New Name");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id, "New Name", "111", "new@test.com", UUID.randomUUID(), false))
                .isInstanceOf(NoSuchElementException.class);
    }
}
