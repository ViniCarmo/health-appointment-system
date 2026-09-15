package com.fiap.scheduling_service.medicalAppointment.domain.event;

public interface AppointmentEventPublisher {
    void publish(AppointmentEvent event);
}
