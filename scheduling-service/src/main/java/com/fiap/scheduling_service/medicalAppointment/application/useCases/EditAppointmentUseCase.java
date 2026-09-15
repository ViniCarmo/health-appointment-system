package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEvent;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEventPublisher;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEventType;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

public class EditAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailsAssembler detailsAssembler;
    private final AppointmentEventPublisher eventPublisher;

    public EditAppointmentUseCase(AppointmentRepository appointmentRepository,
                                  UserRepository userRepository,
                                  AppointmentEventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.detailsAssembler = new AppointmentDetailsAssembler(userRepository);
        this.eventPublisher = eventPublisher;
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
        AppointmentDetails details = detailsAssembler.assemble(saved);

        eventPublisher.publish(new AppointmentEvent(
                AppointmentEventType.EDITED,
                saved.getId(),
                saved.getPatientId(),
                details.patientName(),
                details.doctorName(),
                saved.getDateTime()
        ));

        return details;
    }
}
