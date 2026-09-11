package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

public class CancelAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;

    public CancelAppointmentUseCase(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment execute(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + appointmentId));
        appointment.cancel();
        return appointmentRepository.save(appointment);
    }
}
