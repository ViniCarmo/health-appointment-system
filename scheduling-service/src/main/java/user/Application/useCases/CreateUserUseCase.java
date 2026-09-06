package user.Application.useCases;

import user.domain.entity.User;
import user.domain.enums.Role;
import user.domain.repository.UserRepository;

public class CreateUserUseCase {

    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(String email, String passwordHash, Role role, String name, String phoneNumber) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with email " + email + " already exists.");
        }
        userRepository.save(User.create(email, passwordHash, role, name, phoneNumber));
    }
}
