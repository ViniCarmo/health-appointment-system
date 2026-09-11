package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

public class EditAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;

    public EditAppointmentUseCase(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment execute(UUID appointmentId, LocalDateTime newDateTime, String notes) {
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

        return appointmentRepository.save(appointment);
    }
}
