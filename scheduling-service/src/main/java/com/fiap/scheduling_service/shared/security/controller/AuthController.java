package com.fiap.scheduling_service.shared.security.controller;

import com.fiap.scheduling_service.shared.security.TokenService;
import com.fiap.scheduling_service.shared.security.dto.request.LoginRequestDto;
import com.fiap.scheduling_service.shared.security.dto.response.LoginResponseDto;
import com.fiap.scheduling_service.user.infrastructure.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

@PostMapping("/login")
public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request){
    var usernamePassword = new UsernamePasswordAuthenticationToken(
            request.email(), request.password());

    Authentication authentication = authenticationManager.authenticate(usernamePassword);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String token = tokenService.generateToken(userDetails);

    return ResponseEntity.ok(new LoginResponseDto(token));
}
}
