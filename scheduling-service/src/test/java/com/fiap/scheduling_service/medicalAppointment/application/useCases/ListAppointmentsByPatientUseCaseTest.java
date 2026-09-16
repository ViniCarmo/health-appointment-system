package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.enums.Role;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAppointmentsByPatientUseCaseTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;

    private ListAppointmentsByPatientUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListAppointmentsByPatientUseCase(appointmentRepository, userRepository);
    }

    @Test
    void staffShouldSeeAnyPatientsAppointments() {
        UUID patientId = UUID.randomUUID();
        User patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111");
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");
        Appointment appointment = Appointment.create(patientId, doctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());

        when(appointmentRepository.findByPatientId(patientId)).thenReturn(List.of(appointment));
        when(userRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        List<AppointmentDetails> result = useCase.execute(patientId, UUID.randomUUID(), false);

        assertThat(result).hasSize(1);
    }

    @Test
    void patientShouldSeeOwnAppointments() {
        UUID patientId = UUID.randomUUID();
        User patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111");
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");
        Appointment appointment = Appointment.create(patientId, doctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());

        when(appointmentRepository.findByPatientId(patientId)).thenReturn(List.of(appointment));
        when(userRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        List<AppointmentDetails> result = useCase.execute(patientId, patientId, true);

        assertThat(result).hasSize(1);
    }

    @Test
    void patientShouldNotSeeAnotherPatientsAppointments() {
        UUID patientId = UUID.randomUUID();

        assertThatThrownBy(() -> useCase.execute(patientId, UUID.randomUUID(), true))
                .isInstanceOf(SecurityException.class);
    }
}
