package com.fiap.scheduling_service.medicalAppointment.infrastructure.config;

import com.fiap.scheduling_service.medicalAppointment.application.useCases.*;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentUseCaseConfig {


    @Bean
    public CreateAppointmentUseCase createAppointmentUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        return new CreateAppointmentUseCase(appointmentRepository, userRepository);
    }

    @Bean
    public EditAppointmentUseCase editAppointmentUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        return new EditAppointmentUseCase(appointmentRepository, userRepository);
    }

    @Bean
    public CancelAppointmentUseCase cancelAppointmentUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        return new CancelAppointmentUseCase(appointmentRepository, userRepository);
    }

    @Bean
    public CompleteAppointmentUseCase completeAppointmentUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        return new CompleteAppointmentUseCase(appointmentRepository, userRepository);
    }

    @Bean
    public GetAppointmentByIdUseCase getAppointmentByIdUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        return new GetAppointmentByIdUseCase(appointmentRepository, userRepository);
    }

    @Bean
    public ListAppointmentsUseCase listAppointmentsUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        return new ListAppointmentsUseCase(appointmentRepository, userRepository);
    }
}
