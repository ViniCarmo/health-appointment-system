package user.infrastructure.mapper;

import user.domain.entity.User;
import user.infrastructure.persistence.UserJpaEntity;

public class UserMapper {
    public static UserJpaEntity toJpaEntity(User user){
        return new UserJpaEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static User toDomainEntity(UserJpaEntity entity){
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.getPhoneNumber(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
