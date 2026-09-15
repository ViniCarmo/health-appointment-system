package com.fiap.scheduling_service.medicalAppointment.interfaces.controller;

import com.fiap.scheduling_service.medicalAppointment.application.useCases.*;
import com.fiap.scheduling_service.medicalAppointment.interfaces.dto.request.AppointmentEditRequestDto;
import com.fiap.scheduling_service.medicalAppointment.interfaces.dto.request.AppointmentRequestDto;
import com.fiap.scheduling_service.medicalAppointment.interfaces.dto.response.AppointmentResponseDto;
import com.fiap.scheduling_service.shared.security.AuthenticatedUserProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final CancelAppointmentUseCase cancelAppointmentUseCase;
    private final CompleteAppointmentUseCase completeAppointmentUseCase;
    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final EditAppointmentUseCase editAppointmentUseCase;
    private final GetAppointmentByIdUseCase getAppointmentByIdUseCase;
    private final ListAppointmentsUseCase listAppointmentsUseCase;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public AppointmentController(CancelAppointmentUseCase cancelAppointmentUseCase,
                                  CompleteAppointmentUseCase completeAppointmentUseCase,
                                  CreateAppointmentUseCase createAppointmentUseCase,
                                  EditAppointmentUseCase editAppointmentUseCase,
                                  GetAppointmentByIdUseCase getAppointmentByIdUseCase,
                                  ListAppointmentsUseCase listAppointmentsUseCase,
                                  AuthenticatedUserProvider authenticatedUserProvider) {
        this.cancelAppointmentUseCase = cancelAppointmentUseCase;
        this.completeAppointmentUseCase = completeAppointmentUseCase;
        this.createAppointmentUseCase = createAppointmentUseCase;
        this.editAppointmentUseCase = editAppointmentUseCase;
        this.getAppointmentByIdUseCase = getAppointmentByIdUseCase;
        this.listAppointmentsUseCase = listAppointmentsUseCase;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> create(@Valid @RequestBody AppointmentRequestDto request) {
        AppointmentDetails details = createAppointmentUseCase.execute(
                request.patientId(), request.doctorId(), request.dateTime(), request.notes(), request.createdByUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(AppointmentResponseDto.from(details));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getById(@PathVariable UUID id) {
        AppointmentDetails details = getAppointmentByIdUseCase.execute(
                id, authenticatedUserProvider.getLoggedUserId(), authenticatedUserProvider.isPatientRole());
        return ResponseEntity.ok(AppointmentResponseDto.from(details));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> list() {
        List<AppointmentResponseDto> response = listAppointmentsUseCase.execute(
                        authenticatedUserProvider.getLoggedUserId(), authenticatedUserProvider.isPatientRole())
                .stream()
                .map(AppointmentResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> edit(@PathVariable UUID id,
                                                         @Valid @RequestBody AppointmentEditRequestDto request) {
        AppointmentDetails details = editAppointmentUseCase.execute(id, request.dateTime(), request.notes());
        return ResponseEntity.ok(AppointmentResponseDto.from(details));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDto> cancel(@PathVariable UUID id) {
        AppointmentDetails details = cancelAppointmentUseCase.execute(id);
        return ResponseEntity.ok(AppointmentResponseDto.from(details));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponseDto> complete(@PathVariable UUID id) {
        AppointmentDetails details = completeAppointmentUseCase.execute(id);
        return ResponseEntity.ok(AppointmentResponseDto.from(details));
    }
}
