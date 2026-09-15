package com.fiap.notification_service.notification.application.usecase;

import com.fiap.notification_service.notification.domain.event.AppointmentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendAppointmentReminderUseCase {

    private static final Logger log = LoggerFactory.getLogger(SendAppointmentReminderUseCase.class);

    public void execute(AppointmentEvent event) {
        String message = buildReminderMessage(event);
        log.info(message);
    }

    private String buildReminderMessage(AppointmentEvent event) {
        return switch (event.eventType()) {
            case CREATED -> "Lembrete: %s, sua consulta com Dr(a). %s foi agendada para %s."
                    .formatted(event.patientName(), event.doctorName(), event.dateTime());
            case EDITED -> "Lembrete: %s, sua consulta com Dr(a). %s foi remarcada para %s."
                    .formatted(event.patientName(), event.doctorName(), event.dateTime());
        };
    }
}
