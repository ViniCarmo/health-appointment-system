package com.fiap.notification_service.notification.application.usecase;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fiap.notification_service.notification.domain.event.AppointmentEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SendAppointmentReminderUseCaseTest {

    private final SendAppointmentReminderUseCase useCase = new SendAppointmentReminderUseCase();
    private ListAppender<ILoggingEvent> logAppender;
    private Logger logger;

    @BeforeEach
    void setUp() {
        logger = (Logger) LoggerFactory.getLogger(SendAppointmentReminderUseCase.class);
        logAppender = new ListAppender<>();
        logAppender.start();
        logger.addAppender(logAppender);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(logAppender);
    }

    @Test
    void shouldLogReminderForCreatedEvent() {
        AppointmentEvent event = new AppointmentEvent(AppointmentEvent.EventType.CREATED, UUID.randomUUID(), UUID.randomUUID(),
                "Patient Name", "Doctor Name", LocalDateTime.now().plusDays(1));

        useCase.execute(event);

        assertThat(logAppender.list).hasSize(1);
        String message = logAppender.list.get(0).getFormattedMessage();
        assertThat(message).contains("Patient Name").contains("Doctor Name").contains("agendada");
    }

    @Test
    void shouldLogReminderForEditedEvent() {
        AppointmentEvent event = new AppointmentEvent(AppointmentEvent.EventType.EDITED, UUID.randomUUID(), UUID.randomUUID(),
                "Patient Name", "Doctor Name", LocalDateTime.now().plusDays(1));

        useCase.execute(event);

        assertThat(logAppender.list).hasSize(1);
        String message = logAppender.list.get(0).getFormattedMessage();
        assertThat(message).contains("remarcada");
    }
}
