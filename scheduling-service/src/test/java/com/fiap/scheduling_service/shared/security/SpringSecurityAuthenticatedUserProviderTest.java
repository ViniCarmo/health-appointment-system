package com.fiap.scheduling_service.shared.security;

import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.enums.Role;
import com.fiap.scheduling_service.user.infrastructure.security.UserDetailsImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class SpringSecurityAuthenticatedUserProviderTest {

    private final SpringSecurityAuthenticatedUserProvider provider = new SpringSecurityAuthenticatedUserProvider();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnLoggedUserIdAndPatientRoleForPatient() {
        User patient = User.create("p@test.com", "h", Role.PATIENT, "Patient", "111");
        authenticateAs(patient);

        assertThat(provider.getLoggedUserId()).isEqualTo(patient.getId());
        assertThat(provider.isPatientRole()).isTrue();
    }

    @Test
    void shouldReturnFalseForPatientRoleWhenDoctor() {
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor", "111");
        authenticateAs(doctor);

        assertThat(provider.getLoggedUserId()).isEqualTo(doctor.getId());
        assertThat(provider.isPatientRole()).isFalse();
    }

    @Test
    void shouldReturnFalseForPatientRoleWhenNurse() {
        User nurse = User.create("n@test.com", "h", Role.NURSE, "Nurse", "111");
        authenticateAs(nurse);

        assertThat(provider.isPatientRole()).isFalse();
    }

    private void authenticateAs(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
