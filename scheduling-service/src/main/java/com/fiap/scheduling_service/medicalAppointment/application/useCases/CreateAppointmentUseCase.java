package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailsAssembler detailsAssembler;

    public CreateAppointmentUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.detailsAssembler = new AppointmentDetailsAssembler(userRepository);
    }

    public AppointmentDetails execute(UUID patientId, UUID doctorId, LocalDateTime dateTime, String notes, UUID createdByUserId) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot schedule an appointment in the past");
        }

        if (patientId.equals(doctorId)) {
            throw new IllegalArgumentException("Patient and doctor cannot be the same user");
        }

        Appointment appointment = Appointment.create(patientId, doctorId, dateTime, notes, createdByUserId);
        AppointmentDetails details = detailsAssembler.assemble(appointment);

        Appointment saved = appointmentRepository.save(appointment);
        return new AppointmentDetails(saved, details.patientName(), details.doctorName());
    }
}
