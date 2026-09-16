package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.enums.AppointmentStatus;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelAppointmentUseCaseTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;

    private CancelAppointmentUseCase useCase;

    private UUID appointmentId;
    private User patient;
    private User doctor;

    @BeforeEach
    void setUp() {
        useCase = new CancelAppointmentUseCase(appointmentRepository, userRepository);
        appointmentId = UUID.randomUUID();
        patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111");
        doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");
    }

    @Test
    void shouldCancelScheduledAppointment() {
        Appointment appointment = Appointment.create(patient.getId(), doctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        AppointmentDetails result = useCase.execute(appointmentId);

        assertThat(result.appointment().getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
    }

    @Test
    void shouldThrowWhenAppointmentNotFound() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(appointmentId)).isInstanceOf(NoSuchElementException.class);

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldRejectCancellingAlreadyCompletedAppointment() {
        LocalDateTime now = LocalDateTime.now();
        Appointment completedAppointment = new Appointment(appointmentId, patient.getId(), doctor.getId(), now.plusDays(1),
                AppointmentStatus.COMPLETED, "notes", now, now, UUID.randomUUID());
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(completedAppointment));

        assertThatThrownBy(() -> useCase.execute(appointmentId)).isInstanceOf(IllegalStateException.class);

        verify(appointmentRepository, never()).save(any());
    }
}
