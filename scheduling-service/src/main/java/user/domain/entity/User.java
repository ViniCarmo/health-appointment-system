package user.domain.entity;

import user.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;
public class User {
    private final UUID id;
    private String email;
    private String passwordHash;
    private final Role role;
    private String name;
    private String phoneNumber;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(UUID id, String email, String passwordHash, Role role, String name,
                String phoneNumber, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String email, String passwordHash, Role role, String name, String phoneNumber) {
        LocalDateTime now = LocalDateTime.now();
        return new User(UUID.randomUUID(), email, passwordHash, role, name, phoneNumber, now, now);
    }

    public boolean isDoctor() { return role == Role.DOCTOR; }
    public boolean isNurse() { return role == Role.NURSE; }
    public boolean isPatient() { return role == Role.PATIENT; }

    public void updateContactInfo(String name, String phoneNumber, String email) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePassword(String newPasswordHash) {
       this.passwordHash = newPasswordHash;
       this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
