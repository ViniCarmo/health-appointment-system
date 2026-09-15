package com.fiap.scheduling_service.shared.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

final class SecurityErrorResponseWriter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private SecurityErrorResponseWriter() {
    }

    static void write(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        response.setStatus(status.value());
        response.setContentType("application/json");
        MAPPER.writeValue(response.getWriter(), body);
    }
}
