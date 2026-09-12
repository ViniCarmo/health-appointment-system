package com.fiap.scheduling_service.user.interfaces.controller;

import com.fiap.scheduling_service.user.Application.usecase.*;
import com.fiap.scheduling_service.user.interfaces.dto.request.UpdatePasswordRequestDto;
import com.fiap.scheduling_service.user.interfaces.dto.request.UserRequestDto;
import com.fiap.scheduling_service.user.interfaces.dto.response.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final FindUserByEmailUseCase findUserByEmailUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final UpdatePasswordUseCase updatePasswordUseCase;
    private final UpdateUserContactInfoUseCase updateUserContactInfoUseCase;


    public UserController(CreateUserUseCase createUserUseCase, DeleteUserUseCase deleteUserUseCase, FindUserByEmailUseCase findUserByEmailUseCase, FindUserByIdUseCase findUserByIdUseCase, UpdatePasswordUseCase updatePasswordUseCase, UpdateUserContactInfoUseCase updateUserContactInfoUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.findUserByEmailUseCase = findUserByEmailUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.updatePasswordUseCase = updatePasswordUseCase;
        this.updateUserContactInfoUseCase = updateUserContactInfoUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        var user = createUserUseCase.execute(userRequestDto.email(), userRequestDto.passwordHash(), userRequestDto.role(), userRequestDto.name(), userRequestDto.phoneNumber());
        return ResponseEntity.ok(UserResponseDto.from(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable UUID id) {
        var user = findUserByIdUseCase.execute(id);
        return ResponseEntity.ok(UserResponseDto.from(user));
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponseDto> findUserByEmail(@RequestParam String email) {
        var user = findUserByEmailUseCase.execute(email);
        return ResponseEntity.ok(UserResponseDto.from(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser( @PathVariable UUID id, @RequestBody UserRequestDto userRequestDto) {
        var user = updateUserContactInfoUseCase.execute(id, userRequestDto.name(), userRequestDto.phoneNumber(), userRequestDto.email());
        return ResponseEntity.ok(UserResponseDto.from(user));
    }

    @PutMapping(("/{id}/password"))
    public ResponseEntity<Void> updatePassword(@PathVariable UUID id, @RequestBody UpdatePasswordRequestDto request) {
        updatePasswordUseCase.execute(id, request.newPassword());
        return ResponseEntity.ok().build();
    }
}
