package com.fiap.scheduling_service.shared.messaging;

import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEvent;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaAppointmentEventPublisher implements AppointmentEventPublisher {
    private static final String TOPIC = "appointment-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaAppointmentEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(AppointmentEvent event) {
        kafkaTemplate.send(TOPIC, event.appointmentId().toString(), event);
    }
}
