package com.fiap.scheduling_service.medicalAppointment.infrastructure.mapper;

import com.fiap.scheduling_service.medicalAppointment.infrastructure.persistence.AppointmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentJpaEntity, UUID> {




}
