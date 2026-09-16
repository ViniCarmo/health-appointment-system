package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;
import com.fiap.scheduling_service.user.domain.entity.User;
import com.fiap.scheduling_service.user.domain.enums.Role;
import com.fiap.scheduling_service.user.domain.repository.UserRepository;
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
class AppointmentDetailsAssemblerTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldAssembleDetailsWithPatientAndDoctorNames() {
        User patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111");
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");
        Appointment appointment = Appointment.create(patient.getId(), doctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());

        when(userRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        AppointmentDetailsAssembler assembler = new AppointmentDetailsAssembler(userRepository);
        AppointmentDetails result = assembler.assemble(appointment);

        assertThat(result.patientName()).isEqualTo("Patient Name");
        assertThat(result.doctorName()).isEqualTo("Doctor Name");
    }

    @Test
    void shouldThrowWhenPatientUserNotFound() {
        UUID patientId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        Appointment appointment = Appointment.create(patientId, doctorId, LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());

        when(userRepository.findById(patientId)).thenReturn(Optional.empty());

        AppointmentDetailsAssembler assembler = new AppointmentDetailsAssembler(userRepository);

        assertThatThrownBy(() -> assembler.assemble(appointment)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldRejectWhenPatientIdPointsToNonPatientUser() {
        User notAPatient = User.create("d2@test.com", "h", Role.DOCTOR, "Not Patient", "111");
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Doctor Name", "222");
        Appointment appointment = Appointment.create(notAPatient.getId(), doctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());

        when(userRepository.findById(notAPatient.getId())).thenReturn(Optional.of(notAPatient));
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        AppointmentDetailsAssembler assembler = new AppointmentDetailsAssembler(userRepository);

        assertThatThrownBy(() -> assembler.assemble(appointment)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectWhenDoctorIdPointsToNonDoctorUser() {
        User patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Name", "111");
        User notADoctor = User.create("n@test.com", "h", Role.NURSE, "Not Doctor", "222");
        Appointment appointment = Appointment.create(patient.getId(), notADoctor.getId(), LocalDateTime.now().plusDays(1), "notes", UUID.randomUUID());

        when(userRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(userRepository.findById(notADoctor.getId())).thenReturn(Optional.of(notADoctor));

        AppointmentDetailsAssembler assembler = new AppointmentDetailsAssembler(userRepository);

        assertThatThrownBy(() -> assembler.assemble(appointment)).isInstanceOf(IllegalArgumentException.class);
    }
}
