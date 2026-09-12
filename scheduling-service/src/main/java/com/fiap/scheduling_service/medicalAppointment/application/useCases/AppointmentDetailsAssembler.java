package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

class AppointmentDetailsAssembler {
    private final UserRepository userRepository;

    AppointmentDetailsAssembler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    AppointmentDetails assemble(Appointment appointment) {
        User patient = findUser(appointment.getPatientId(), "Patient");
        User doctor = findUser(appointment.getDoctorId(), "Doctor");
        return new AppointmentDetails(appointment, patient.getName(), doctor.getName());
    }

    private User findUser(UUID id, String label) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(label + " not found: " + id));
    }
}
