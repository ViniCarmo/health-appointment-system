package com.fiap.scheduling_service.medicalAppointment.infrastructure.persistence;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.medicalAppointment.infrastructure.mapper.AppointmentMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AppointmentRepositoryJpa implements AppointmentRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;

    public AppointmentRepositoryJpa(AppointmentJpaRepository appointmentJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
    }

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentJpaEntity entity = AppointmentMapper.toJpaEntity(appointment);
        AppointmentJpaEntity saved = appointmentJpaRepository.save(entity);
        return AppointmentMapper.toDomainEntity(saved);
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return appointmentJpaRepository.findById(id).map(AppointmentMapper::toDomainEntity);
    }

    @Override
    public void deleteById(UUID id) {
        appointmentJpaRepository.deleteById(id);
    }

    @Override
    public List<Appointment> findByPatientId(UUID patientId) {
        return appointmentJpaRepository.findByPatientId(patientId).stream()
                .map(AppointmentMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentJpaRepository.findAll().stream()
                .map(AppointmentMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
}
