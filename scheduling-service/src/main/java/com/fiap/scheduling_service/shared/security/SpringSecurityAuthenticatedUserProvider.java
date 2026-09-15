package com.fiap.scheduling_service.shared.security;

import com.fiap.scheduling_service.user.infrastructure.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpringSecurityAuthenticatedUserProvider implements AuthenticatedUserProvider{

    @Override
    public UUID getLoggedUserId() {
        return getLoggedUserDetails().getDomainUser().getId();
    }

    @Override
    public boolean isPatientRole() {
        return getLoggedUserDetails().getDomainUser().isPatient();
    }

    private UserDetailsImpl getLoggedUserDetails() {
        return (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
