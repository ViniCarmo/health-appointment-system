package com.fiap.notification_service.notification.infrastructure.messaging;

import com.fiap.notification_service.notification.application.usecase.SendAppointmentReminderUseCase;
import com.fiap.notification_service.notification.domain.event.AppointmentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventListener {

    private final SendAppointmentReminderUseCase sendAppointmentReminderUseCase;

    public AppointmentEventListener(SendAppointmentReminderUseCase sendAppointmentReminderUseCase) {
        this.sendAppointmentReminderUseCase = sendAppointmentReminderUseCase;
    }

    @KafkaListener(topics = "appointment-events", containerFactory = "appointmentEventListenerContainerFactory")
    public void onAppointmentEvent(AppointmentEvent event) {
        sendAppointmentReminderUseCase.execute(event);
    }
}
