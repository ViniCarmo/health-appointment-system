package com.fiap.scheduling_service.medicalAppointment.domain.entity;

import com.fiap.scheduling_service.medicalAppointment.domain.enums.AppointmentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentTest {

    private final UUID patientId = UUID.randomUUID();
    private final UUID doctorId = UUID.randomUUID();
    private final UUID createdByUserId = UUID.randomUUID();

    @Test
    void createShouldStartAsScheduled() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);

        Appointment appointment = Appointment.create(patientId, doctorId, dateTime, "notes", createdByUserId);

        assertThat(appointment.getId()).isNotNull();
        assertThat(appointment.getPatientId()).isEqualTo(patientId);
        assertThat(appointment.getDoctorId()).isEqualTo(doctorId);
        assertThat(appointment.getDateTime()).isEqualTo(dateTime);
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
        assertThat(appointment.getNotes()).isEqualTo("notes");
        assertThat(appointment.getCreatedByUserId()).isEqualTo(createdByUserId);
        assertThat(appointment.getCreatedAt()).isEqualTo(appointment.getUpdatedAt());
    }

    @Test
    void cancelShouldMoveScheduledToCancelled() {
        Appointment appointment = scheduledAppointment();

        appointment.cancel();

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
    }

    @Test
    void completeShouldMoveScheduledToCompleted() {
        Appointment appointment = scheduledAppointment();

        appointment.complete();

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
    }

    @ParameterizedTest
    @EnumSource(value = AppointmentStatus.class, names = "SCHEDULED", mode = EnumSource.Mode.EXCLUDE)
    void cancelShouldRejectNonScheduledAppointment(AppointmentStatus status) {
        Appointment appointment = appointmentWithStatus(status);

        assertThatThrownBy(appointment::cancel).isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @EnumSource(value = AppointmentStatus.class, names = "SCHEDULED", mode = EnumSource.Mode.EXCLUDE)
    void completeShouldRejectNonScheduledAppointment(AppointmentStatus status) {
        Appointment appointment = appointmentWithStatus(status);

        assertThatThrownBy(appointment::complete).isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @EnumSource(value = AppointmentStatus.class, names = "SCHEDULED", mode = EnumSource.Mode.EXCLUDE)
    void rescheduleShouldRejectNonScheduledAppointment(AppointmentStatus status) {
        Appointment appointment = appointmentWithStatus(status);

        assertThatThrownBy(() -> appointment.reschedule(LocalDateTime.now().plusDays(2)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void rescheduleShouldUpdateDateTimeWhenScheduled() {
        Appointment appointment = scheduledAppointment();
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(5);

        appointment.reschedule(newDateTime);

        assertThat(appointment.getDateTime()).isEqualTo(newDateTime);
    }

    @Test
    void updateNotesShouldWorkRegardlessOfStatus() {
        Appointment appointment = appointmentWithStatus(AppointmentStatus.COMPLETED);

        appointment.updateNotes("follow-up notes");

        assertThat(appointment.getNotes()).isEqualTo("follow-up notes");
    }

    @Test
    void belongsToPatientShouldMatchPatientId() {
        Appointment appointment = scheduledAppointment();

        assertThat(appointment.belongsToPatient(patientId)).isTrue();
        assertThat(appointment.belongsToPatient(UUID.randomUUID())).isFalse();
    }

    @Test
    void belongsToDoctorShouldMatchDoctorId() {
        Appointment appointment = scheduledAppointment();

        assertThat(appointment.belongsToDoctor(doctorId)).isTrue();
        assertThat(appointment.belongsToDoctor(UUID.randomUUID())).isFalse();
    }

    private Appointment scheduledAppointment() {
        return Appointment.create(patientId, doctorId, LocalDateTime.now().plusDays(1), "notes", createdByUserId);
    }

    private Appointment appointmentWithStatus(AppointmentStatus status) {
        LocalDateTime now = LocalDateTime.now();
        return new Appointment(UUID.randomUUID(), patientId, doctorId, now.plusDays(1), status, "notes", now, now, createdByUserId);
    }
}
