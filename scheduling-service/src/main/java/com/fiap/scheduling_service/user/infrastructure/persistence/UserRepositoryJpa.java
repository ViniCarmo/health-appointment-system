package com.fiap.scheduling_service.user.infrastructure.persistence;

import org.springframework.stereotype.Component;
import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;
import com.fiap.scheduling_service.user.infrastructure.mapper.UserMapper;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryJpa implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryJpa(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserMapper.toJpaEntity(user);
        UserJpaEntity saved = userJpaRepository.save(entity);
        return UserMapper.toDomainEntity(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(UserMapper::toDomainEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserMapper::toDomainEntity);
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }
}
