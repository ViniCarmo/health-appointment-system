package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

public class CompleteAppointmentUseCase {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailsAssembler detailsAssembler;

    public CompleteAppointmentUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.detailsAssembler = new AppointmentDetailsAssembler(userRepository);
    }

    public AppointmentDetails execute(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + appointmentId));
        appointment.complete();
        Appointment saved = appointmentRepository.save(appointment);
        return detailsAssembler.assemble(saved);
    }
}
