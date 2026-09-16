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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAppointmentByIdUseCaseTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;

    private GetAppointmentByIdUseCase useCase;

    private UUID appointmentId;
    private User patient;
    private User doctor;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        useCase = new GetAppointmentByIdUseCase(appointmentRepository, userRepository);
        appointmentId = UUID.randomUUID();
        patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111");
        doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");
        appointment = Appointment.create(patient.getId(), doctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());
    }

    @Test
    void staffShouldSeeAnyAppointment() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(userRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        AppointmentDetails result = useCase.execute(appointmentId, UUID.randomUUID(), false);

        assertThat(result.appointment()).isEqualTo(appointment);
    }

    @Test
    void patientShouldSeeOwnAppointment() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(userRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        AppointmentDetails result = useCase.execute(appointmentId, patient.getId(), true);

        assertThat(result.appointment()).isEqualTo(appointment);
    }

    @Test
    void patientShouldNotSeeOthersAppointment() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        assertThatThrownBy(() -> useCase.execute(appointmentId, UUID.randomUUID(), true))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void shouldThrowWhenAppointmentNotFound() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(appointmentId, UUID.randomUUID(), false))
                .isInstanceOf(NoSuchElementException.class);
    }
}
