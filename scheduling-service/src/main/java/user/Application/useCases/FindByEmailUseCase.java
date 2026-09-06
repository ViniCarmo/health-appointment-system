package user.Application.useCases;

import user.domain.entity.User;
import user.domain.repository.UserRepository;

public class FindByEmailUseCase {

    private final UserRepository userRepository;

    public FindByEmailUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
