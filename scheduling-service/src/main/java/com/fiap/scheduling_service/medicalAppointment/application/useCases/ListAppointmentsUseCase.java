package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;

import java.util.List;
import java.util.UUID;

public class ListAppointmentsUseCase {

    private final AppointmentRepository appointmentRepository;

    public ListAppointmentsUseCase(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> execute(UUID requestingUserId, boolean isPatientRole) {
        if (isPatientRole) {
            return appointmentRepository.findByPatientId(requestingUserId);
        }
        return appointmentRepository.findAll();
    }
}
