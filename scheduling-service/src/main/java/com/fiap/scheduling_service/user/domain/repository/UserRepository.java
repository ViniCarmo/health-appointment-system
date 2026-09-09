package com.fiap.scheduling_service.user.domain.repository;

import com.fiap.scheduling_service.user.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    void deleteById(UUID id);


}
