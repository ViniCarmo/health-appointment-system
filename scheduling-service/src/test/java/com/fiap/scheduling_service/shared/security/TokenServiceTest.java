package com.fiap.scheduling_service.shared.security;

import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.enums.Role;
import com.fiap.scheduling_service.user.infrastructure.security.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenServiceTest {

    private static final String SECRET = "test-secret-key-minimum-32-characters-long-0123456789";

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", SECRET);
    }

    @Test
    void shouldGenerateTokenAndValidateItBack() {
        User user = User.create("doctor@test.com", "hash", Role.DOCTOR, "Dr House", "111");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        String token = tokenService.generateToken(userDetails);

        assertThat(token).isNotBlank();
        assertThat(tokenService.validateTokenAndGetSubject(token)).isEqualTo("doctor@test.com");
    }

    @Test
    void shouldRejectTamperedToken() {
        User user = User.create("doctor@test.com", "hash", Role.DOCTOR, "Dr House", "111");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String token = tokenService.generateToken(userDetails);
        String tamperedToken = token.substring(0, token.length() - 1) + (token.endsWith("a") ? "b" : "a");

        assertThatThrownBy(() -> tokenService.validateTokenAndGetSubject(tamperedToken))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void shouldRejectTokenSignedWithDifferentSecret() {
        User user = User.create("doctor@test.com", "hash", Role.DOCTOR, "Dr House", "111");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String token = tokenService.generateToken(userDetails);

        TokenService otherTokenService = new TokenService();
        ReflectionTestUtils.setField(otherTokenService, "secret", "another-secret-key-minimum-32-characters-long");

        assertThatThrownBy(() -> otherTokenService.validateTokenAndGetSubject(token))
                .isInstanceOf(JwtException.class);
    }
}
