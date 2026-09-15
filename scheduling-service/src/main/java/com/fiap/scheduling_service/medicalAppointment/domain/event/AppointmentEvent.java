package com.fiap.scheduling_service.medicalAppointment.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentEvent(AppointmentEventType eventType,
                               UUID appointmentId,
                               UUID patientId,
                               String patientName,
                               String doctorName,
                               LocalDateTime dateTime) {
}
