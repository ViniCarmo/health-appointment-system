package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

public class EditAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailsAssembler detailsAssembler;

    public EditAppointmentUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.detailsAssembler = new AppointmentDetailsAssembler(userRepository);
    }

    public AppointmentDetails execute(UUID appointmentId, LocalDateTime newDateTime, String notes) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + appointmentId));

        if (newDateTime != null) {
            if (newDateTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Cannot reschedule to a past date");
            }
            appointment.reschedule(newDateTime);
        }

        if (notes != null) {
            appointment.updateNotes(notes);
        }

        Appointment saved = appointmentRepository.save(appointment);
        return detailsAssembler.assemble(saved);
    }
}
