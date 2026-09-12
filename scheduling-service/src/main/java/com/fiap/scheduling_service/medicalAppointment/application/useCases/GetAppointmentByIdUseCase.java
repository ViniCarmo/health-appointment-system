package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

public class GetAppointmentByIdUseCase {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailsAssembler detailsAssembler;

    public GetAppointmentByIdUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.detailsAssembler = new AppointmentDetailsAssembler(userRepository);
    }

    public AppointmentDetails execute(UUID appointmentId, UUID requestingUserId, boolean isPatientRole) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + appointmentId));

        if (isPatientRole && !appointment.belongsToPatient(requestingUserId)) {
            throw new SecurityException("Patients can only view their own appointments");
        }

        return detailsAssembler.assemble(appointment);
    }
}
