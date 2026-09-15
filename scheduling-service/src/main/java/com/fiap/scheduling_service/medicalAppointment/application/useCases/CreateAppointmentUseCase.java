package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEvent;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEventPublisher;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEventType;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailsAssembler detailsAssembler;
    private final AppointmentEventPublisher eventPublisher;

    public CreateAppointmentUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository, AppointmentEventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.detailsAssembler = new AppointmentDetailsAssembler(userRepository);
        this.eventPublisher = eventPublisher;
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
        AppointmentDetails result = new AppointmentDetails(saved, details.patientName(), details.doctorName());

        eventPublisher.publish(new AppointmentEvent(
                AppointmentEventType.CREATED,
                saved.getId(),
                saved.getPatientId(),
                details.patientName(),
                details.doctorName(),
                saved.getDateTime()
        ));

        return result;
    }
}
