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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAppointmentsUseCaseTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;

    private ListAppointmentsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListAppointmentsUseCase(appointmentRepository, userRepository);
    }

    @Test
    void patientShouldOnlySeeOwnAppointments() {
        UUID patientId = UUID.randomUUID();
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");
        Appointment appointment = Appointment.create(patientId, doctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());

        when(appointmentRepository.findByPatientId(patientId)).thenReturn(List.of(appointment));
        when(userRepository.findById(patientId)).thenReturn(Optional.of(
                User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111")));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        List<AppointmentDetails> result = useCase.execute(patientId, true);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).patientName()).isEqualTo("Patient Name");
        assertThat(result.get(0).doctorName()).isEqualTo("Doctor Name");
        verify(appointmentRepository, never()).findAll();
    }

    @Test
    void staffShouldSeeAllAppointments() {
        UUID requesterId = UUID.randomUUID();
        User patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111");
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");
        Appointment appointment = Appointment.create(patient.getId(), doctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());

        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        when(userRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        List<AppointmentDetails> result = useCase.execute(requesterId, false);

        assertThat(result).hasSize(1);
        verify(appointmentRepository, never()).findByPatientId(any());
    }

    @Test
    void shouldCacheUserNameLookupsAcrossMultipleAppointments() {
        UUID requesterId = UUID.randomUUID();
        User patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111");
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");
        Appointment first = Appointment.create(patient.getId(), doctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());
        Appointment second = Appointment.create(patient.getId(), doctor.getId(), LocalDateTime.now().plusDays(2), "notes", UUID.randomUUID());

        when(appointmentRepository.findAll()).thenReturn(List.of(first, second));
        when(userRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        List<AppointmentDetails> result = useCase.execute(requesterId, false);

        assertThat(result).hasSize(2);
        verify(userRepository, times(1)).findById(patient.getId());
        verify(userRepository, times(1)).findById(doctor.getId());
    }
}
