package com.fiap.scheduling_service.shared.security;

import com.fiap.scheduling_service.user.infrastructure.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
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
