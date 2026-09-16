package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEvent;
import com.fiap.scheduling_service.medicalAppointment.domain.event.AppointmentEventPublisher;
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
class CreateAppointmentUseCaseTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AppointmentEventPublisher eventPublisher;

    private CreateAppointmentUseCase useCase;

    private UUID patientId;
    private UUID doctorId;
    private UUID createdByUserId;

    @BeforeEach
    void setUp() {
        useCase = new CreateAppointmentUseCase(appointmentRepository, userRepository, eventPublisher);
        patientId = UUID.randomUUID();
        doctorId = UUID.randomUUID();
        createdByUserId = UUID.randomUUID();
    }

    @Test
    void shouldCreateAppointmentAndPublishEvent() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        User patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111");
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");

        when(userRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(userRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentDetails result = useCase.execute(patientId, doctorId, dateTime, "notes", createdByUserId);

        assertThat(result.patientName()).isEqualTo("Patient Name");
        assertThat(result.doctorName()).isEqualTo("Doctor Name");
        assertThat(result.appointment().getPatientId()).isEqualTo(patientId);
        assertThat(result.appointment().getDoctorId()).isEqualTo(doctorId);

        verify(appointmentRepository).save(any(Appointment.class));

        ArgumentCaptor<AppointmentEvent> eventCaptor = ArgumentCaptor.forClass(AppointmentEvent.class);
        verify(eventPublisher).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue().patientName()).isEqualTo("Patient Name");
        assertThat(eventCaptor.getValue().doctorName()).isEqualTo("Doctor Name");
    }

    @Test
    void shouldRejectAppointmentInThePast() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);

        assertThatThrownBy(() -> useCase.execute(patientId, doctorId, pastDateTime, "notes", createdByUserId))
                .isInstanceOf(IllegalArgumentException.class);

        verify(appointmentRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void shouldRejectSamePatientAndDoctor() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);

        assertThatThrownBy(() -> useCase.execute(patientId, patientId, dateTime, "notes", createdByUserId))
                .isInstanceOf(IllegalArgumentException.class);

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldNotPersistWhenPatientDoesNotExist() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        when(userRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(patientId, doctorId, dateTime, "notes", createdByUserId))
                .isInstanceOf(NoSuchElementException.class);

        verify(appointmentRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void shouldRejectWhenTargetUserDoesNotHaveExpectedRole() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        User wrongRolePatient = User.create("p@test.com", "h", Role.DOCTOR, "Not a patient", "111");
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");

        when(userRepository.findById(patientId)).thenReturn(Optional.of(wrongRolePatient));
        when(userRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        assertThatThrownBy(() -> useCase.execute(patientId, doctorId, dateTime, "notes", createdByUserId))
                .isInstanceOf(IllegalArgumentException.class);

        verify(appointmentRepository, never()).save(any());
    }
}
