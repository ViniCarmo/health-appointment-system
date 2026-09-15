package com.fiap.scheduling_service.shared.security;

import java.util.UUID;

public interface AuthenticatedUserProvider {
    UUID getLoggedUserId();
    boolean isPatientRole();
}
