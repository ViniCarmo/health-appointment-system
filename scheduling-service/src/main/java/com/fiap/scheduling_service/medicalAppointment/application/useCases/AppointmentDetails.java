package com.fiap.scheduling_service.medicalAppointment.application.useCases;

import com.fiap.scheduling_service.medicalAppointment.domain.entity.Appointment;

public record AppointmentDetails(Appointment appointment, String patientName, String doctorName) {
}
