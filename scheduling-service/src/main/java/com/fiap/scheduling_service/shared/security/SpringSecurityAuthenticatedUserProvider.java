package com.fiap.scheduling_service.shared.security;

import com.fiap.scheduling_service.user.infrastructure.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SpringSecurityAuthenticatedUserProvider implements AuthenticatedUserProvider{
    @Override
    public UUID getLoggedUserId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userDetails.getDomainUser().getId();
    }
}
