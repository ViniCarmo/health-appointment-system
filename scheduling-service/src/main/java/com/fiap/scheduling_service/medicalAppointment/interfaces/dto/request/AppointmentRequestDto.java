package com.fiap.scheduling_service.medicalAppointment.interfaces.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequestDto(@NotNull(message = "Patient id is required")
                                    UUID patientId,

                                    @NotNull(message = "Doctor id is required")
                                    UUID doctorId,

                                    @NotNull(message = "Date and time are required")
                                    @Future(message = "Appointment date must be in the future")
                                    LocalDateTime dateTime,

                                    @Size(max = 500, message = "Notes must be at most 500 characters")
                                    String notes,

                                    @NotNull(message = "CreatedByUserId is required")
                                    UUID createdByUserId) {
}
