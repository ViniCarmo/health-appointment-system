package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

public class ListAppointmentsByPatientUseCase {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public ListAppointmentsByPatientUseCase(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    public List<AppointmentDetails> execute(UUID patientId, UUID requestingUserId, boolean isPatientRole) {
        if (isPatientRole && !patientId.equals(requestingUserId)) {
            throw new SecurityException("Patients can only view their own appointments");
        }

        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

        Map<UUID, String> nameCache = new HashMap<>();
        return appointments.stream()
                .map(appointment -> new AppointmentDetails(
                        appointment,
                        resolveName(appointment.getPatientId(), nameCache),
                        resolveName(appointment.getDoctorId(), nameCache)
                ))
                .collect(Collectors.toList());
    }

    private String resolveName(UUID userId, Map<UUID, String> nameCache) {
        return nameCache.computeIfAbsent(userId, id -> userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id))
                .getName());
    }
}
