package user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import user.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);
}
