package com.fiap.notification_service.notification.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentEvent(EventType eventType,
                               UUID appointmentId,
                               UUID patientId,
                               String patientName,
                               String doctorName,
                               LocalDateTime dateTime) {

    public enum EventType {
        CREATED,
        EDITED
    }
}
