package user.Application.useCases;

import user.domain.entity.User;
import user.domain.repository.UserRepository;

import java.util.UUID;

public class UpdateInfoContactUseCase {

    private final UserRepository userRepository;

    public UpdateInfoContactUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UUID id, String name, String phoneNumber, String email){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.updateContactInfo(name, phoneNumber, email);
        return userRepository.save(user);
    }
}
