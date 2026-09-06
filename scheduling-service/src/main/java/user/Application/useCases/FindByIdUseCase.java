package user.Application.useCases;

import user.domain.entity.User;
import user.domain.repository.UserRepository;

import java.util.UUID;

public class FindByIdUseCase {

    private final UserRepository userRepository;

    public FindByIdUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
