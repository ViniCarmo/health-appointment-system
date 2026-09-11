package com.fiap.scheduling_service.medicalAppointment.domain.repository;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);
    Optional<Appointment> findById(UUID id);
    void deleteById(UUID id);
    List<Appointment> findByPatientId(UUID patientId);
    List<Appointment> findAll();

}
