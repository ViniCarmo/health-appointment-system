package com.fiap.scheduling_service.medicalAppointment.domain.entity;

import com.fiap.scheduling_service.medicalAppointment.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {
    private final UUID id;
    private final UUID patientId;
    private final UUID doctorId;
    private LocalDateTime dateTime;
    private AppointmentStatus status;
    private String notes;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final UUID createdByUserId;

    public Appointment(UUID id, UUID patientId, UUID doctorId, LocalDateTime dateTime, AppointmentStatus status, String notes, LocalDateTime createdAt, LocalDateTime updatedAt, UUID createdByUserId) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdByUserId = createdByUserId;
    }

    public static Appointment create(UUID patientId, UUID doctorId, LocalDateTime dateTime, String notes, UUID createdByUserId) {
        LocalDateTime now = LocalDateTime.now();
        return new Appointment(UUID.randomUUID(), patientId, doctorId, dateTime, AppointmentStatus.SCHEDULED, notes, now, now, createdByUserId);
    }

    public void cancel() {
        this.status = AppointmentStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = AppointmentStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reschedule(LocalDateTime newDateTime) {
        this.dateTime = newDateTime;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateNotes(String notes) {
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean belongsToPatient(UUID userId) {
        return this.patientId.equals(userId);
    }

    public boolean belongsToDoctor(UUID userId) {
        return this.doctorId.equals(userId);
    }

    public UUID getId() {
        return id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UUID getCreatedByUserId() {
        return createdByUserId;
    }
}
