package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

public class GetAppointmentByIdUseCase {
    private final AppointmentRepository appointmentRepository;

    public GetAppointmentByIdUseCase(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment execute(UUID appointmentId, UUID requestingUserId, boolean isPatientRole) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + appointmentId));

        if (isPatientRole && !appointment.belongsToPatient(requestingUserId)) {
            throw new SecurityException("Patients can only view their own appointments");
        }

        return appointment;
    }
}
