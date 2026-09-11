package com.fiap.scheduling_service.medicalAppointment.infrastructure.config;

import com.fiap.scheduling_service.medicalAppointment.application.useCases.*;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentUseCaseConfig {


    @Bean
    public CreateAppointmentUseCase createAppointmentUseCase(AppointmentRepository appointmentRepository) {
        return new CreateAppointmentUseCase(appointmentRepository);
    }

    @Bean
    public EditAppointmentUseCase editAppointmentUseCase(AppointmentRepository appointmentRepository) {
        return new EditAppointmentUseCase(appointmentRepository);
    }

    @Bean
    public CancelAppointmentUseCase cancelAppointmentUseCase(AppointmentRepository appointmentRepository) {
        return new CancelAppointmentUseCase(appointmentRepository);
    }

    @Bean
    public CompleteAppointmentUseCase completeAppointmentUseCase(AppointmentRepository appointmentRepository) {
        return new CompleteAppointmentUseCase(appointmentRepository);
    }

    @Bean
    public GetAppointmentByIdUseCase getAppointmentByIdUseCase(AppointmentRepository appointmentRepository) {
        return new GetAppointmentByIdUseCase(appointmentRepository);
    }

    @Bean
    public ListAppointmentsUseCase listAppointmentsUseCase(AppointmentRepository appointmentRepository) {
        return new ListAppointmentsUseCase(appointmentRepository);
    }
}
