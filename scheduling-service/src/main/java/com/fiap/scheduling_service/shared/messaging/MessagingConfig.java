package com.fiap.scheduling_service.shared.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class MessagingConfig {

    @Bean
    public KafkaAppointmentEventPublisher appointmentEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaAppointmentEventPublisher(kafkaTemplate);
    }
}
