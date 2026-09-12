package com.fiap.scheduling_service.medicalAppointment.interfaces.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AppointmentEditRequestDto(@Future(message = "Appointment date must be in the future")
                                        LocalDateTime dateTime,

                                        @Size(max = 500, message = "Notes must be at most 500 characters")
                                        String notes) {
}
