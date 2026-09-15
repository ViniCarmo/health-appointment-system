package com.fiap.notification_service.notification.infrastructure.config;

import com.fiap.notification_service.notification.application.usecase.SendAppointmentReminderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationUseCaseConfig {

    @Bean
    public SendAppointmentReminderUseCase sendAppointmentReminderUseCase() {
        return new SendAppointmentReminderUseCase();
    }
}
