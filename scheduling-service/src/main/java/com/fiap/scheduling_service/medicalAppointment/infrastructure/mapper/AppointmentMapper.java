package com.fiap.scheduling_service.medicalAppointment.infrastructure.mapper;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.infrastructure.persistence.AppointmentJpaEntity;

public class AppointmentMapper {
    public static AppointmentJpaEntity toJpaEntity(Appointment appointment){
        return new AppointmentJpaEntity(
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getDateTime(),
                appointment.getStatus(),
                appointment.getNotes(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt(),
                appointment.getCreatedByUserId()
        );
    }

    public static Appointment toDomainEntity(AppointmentJpaEntity appointmentJpaEntity){
        return new Appointment(
                appointmentJpaEntity.getId(),
                appointmentJpaEntity.getPatientId(),
                appointmentJpaEntity.getDoctorId(),
                appointmentJpaEntity.getDateTime(),
                appointmentJpaEntity.getStatus(),
                appointmentJpaEntity.getNotes(),
                appointmentJpaEntity.getCreatedAt(),
                appointmentJpaEntity.getUpdatedAt(),
                appointmentJpaEntity.getCreatedByUserId()
        );
    }
}
