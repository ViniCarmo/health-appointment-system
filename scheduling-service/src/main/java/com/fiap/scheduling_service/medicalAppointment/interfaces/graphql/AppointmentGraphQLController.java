package com.fiap.scheduling_service.medicalAppointment.interfaces.graphql;

import com.fiap.scheduling_service.medicalAppointment.application.useCases.ListAppointmentsByPatientUseCase;
import com.fiap.scheduling_service.medicalAppointment.interfaces.dto.response.AppointmentResponseDto;
import com.fiap.scheduling_service.shared.security.AuthenticatedUserProvider;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class AppointmentGraphQLController {

    private final ListAppointmentsByPatientUseCase listAppointmentsByPatientUseCase;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public AppointmentGraphQLController(ListAppointmentsByPatientUseCase listAppointmentsByPatientUseCase,
                                         AuthenticatedUserProvider authenticatedUserProvider) {
        this.listAppointmentsByPatientUseCase = listAppointmentsByPatientUseCase;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @QueryMapping
    public List<AppointmentResponseDto> appointmentsByPatient(@Argument UUID patientId) {
        return listAppointmentsByPatientUseCase.execute(
                        patientId, authenticatedUserProvider.getLoggedUserId(), authenticatedUserProvider.isPatientRole())
                .stream()
                .map(AppointmentResponseDto::from)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<AppointmentResponseDto> upcomingAppointmentsByPatient(@Argument UUID patientId) {
        return listAppointmentsByPatientUseCase.execute(
                        patientId, authenticatedUserProvider.getLoggedUserId(), authenticatedUserProvider.isPatientRole())
                .stream()
                .filter(details -> details.appointment().getDateTime().isAfter(LocalDateTime.now()))
                .map(AppointmentResponseDto::from)
                .collect(Collectors.toList());
    }
}
