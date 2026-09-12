package com.fiap.scheduling_service.medicalAppointment.interfaces.dto.response;

import com.fiap.scheduling_service.medicalAppointment.application.useCases.AppointmentDetails;
import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDto(UUID id,
                                     UUID patientId,
                                     String patientName,
                                     UUID doctorId,
                                     String doctorName,
                                     LocalDateTime dateTime,
                                     AppointmentStatus status,
                                     String notes,
                                     UUID createdByUserId,
                                     LocalDateTime createdAt,
                                     LocalDateTime updatedAt) {

    public static AppointmentResponseDto from(AppointmentDetails details) {
        Appointment appointment = details.appointment();
        return new AppointmentResponseDto(
                appointment.getId(),
                appointment.getPatientId(),
                details.patientName(),
                appointment.getDoctorId(),
                details.doctorName(),
                appointment.getDateTime(),
                appointment.getStatus(),
                appointment.getNotes(),
                appointment.getCreatedByUserId(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt()
        );
    }
}
