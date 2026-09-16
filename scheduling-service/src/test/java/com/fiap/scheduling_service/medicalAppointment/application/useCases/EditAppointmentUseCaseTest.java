package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.enums.AppointmentStatus;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEvent;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEventPublisher;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEventType;
import com.fiap.scheduling_service.medicalAppointment.domain.repository.AppointmentRepository;
import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.enums.Role;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class EditAppointmentUseCaseTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AppointmentEventPublisher eventPublisher;

    private EditAppointmentUseCase useCase;

    private UUID appointmentId;
    private User patient;
    private User doctor;

    @BeforeEach
    void setUp() {
        useCase = new EditAppointmentUseCase(appointmentRepository, userRepository, eventPublisher);
        appointmentId = UUID.randomUUID();
        patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111");
        doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");
    }

    @Test
    void shouldRescheduleAndUpdateNotesThenPublishEditedEvent() {
        Appointment appointment = Appointment.create(patient.getId(), doctor.getId(), LocalDateTime.now().plusDays(1), "old notes", UUID.randomUUID());
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(3);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        AppointmentDetails result = useCase.execute(appointmentId, newDateTime, "new notes");

        assertThat(result.appointment().getDateTime()).isEqualTo(newDateTime);
        assertThat(result.appointment().getNotes()).isEqualTo("new notes");

        ArgumentCaptor<AppointmentEvent> captor = ArgumentCaptor.forClass(AppointmentEvent.class);
        verify(eventPublisher).publish(captor.capture());
        assertThat(captor.getValue().eventType()).isEqualTo(AppointmentEventType.EDITED);
    }

    @Test
    void shouldThrowWhenAppointmentNotFound() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(appointmentId, LocalDateTime.now().plusDays(1), "notes"))
                .isInstanceOf(NoSuchElementException.class);

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldRejectReschedulingToThePast() {
        Appointment appointment = Appointment.create(patient.getId(), doctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        assertThatThrownBy(() -> useCase.execute(appointmentId, LocalDateTime.now().minusDays(1), "notes"))
                .isInstanceOf(IllegalArgumentException.class);

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldRejectReschedulingNonScheduledAppointment() {
        LocalDateTime now = LocalDateTime.now();
        Appointment cancelledAppointment = new Appointment(appointmentId, patient.getId(), doctor.getId(), now.plusDays(1),
                AppointmentStatus.CANCELLED, "notes", now, now, UUID.randomUUID());
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(cancelledAppointment));

        assertThatThrownBy(() -> useCase.execute(appointmentId, now.plusDays(2), "notes"))
                .isInstanceOf(IllegalStateException.class);

        verify(appointmentRepository, never()).save(any());
    }
}
