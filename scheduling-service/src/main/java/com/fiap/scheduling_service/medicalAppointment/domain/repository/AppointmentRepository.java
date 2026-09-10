package com.fiap.scheduling_service.medicalAppointment.domain.repository;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;

import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);
    Optional<Appointment> findById(UUID id);
    Optional<Appointment> findByEmail(String email);
    void deleteById(UUID id);

}
