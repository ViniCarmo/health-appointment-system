package com.fiap.scheduling_service.user.domain.entity;

import com.fiap.scheduling_service.user.domain.enums.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void createShouldBuildUserWithGivenData() {
        User user = User.create("patient@test.com", "hashed", Role.PATIENT, "John Doe", "11999999999");

        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("patient@test.com");
        assertThat(user.getPasswordHash()).isEqualTo("hashed");
        assertThat(user.getRole()).isEqualTo(Role.PATIENT);
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getPhoneNumber()).isEqualTo("11999999999");
        assertThat(user.getCreatedAt()).isEqualTo(user.getUpdatedAt());
    }

    @Test
    void isDoctorIsNurseIsPatientShouldReflectRole() {
        User doctor = User.create("d@test.com", "h", Role.DOCTOR, "Dr House", "111");
        User nurse = User.create("n@test.com", "h", Role.NURSE, "Nurse Joy", "222");
        User patient = User.create("p@test.com", "h", Role.PATIENT, "Patient Zero", "333");

        assertThat(doctor.isDoctor()).isTrue();
        assertThat(doctor.isNurse()).isFalse();
        assertThat(doctor.isPatient()).isFalse();

        assertThat(nurse.isNurse()).isTrue();
        assertThat(nurse.isDoctor()).isFalse();

        assertThat(patient.isPatient()).isTrue();
        assertThat(patient.isDoctor()).isFalse();
    }

    @Test
    void updateContactInfoShouldChangeNamePhoneAndEmail() {
        User user = User.create("old@test.com", "h", Role.PATIENT, "Old Name", "000");

        user.updateContactInfo("New Name", "111222333", "new@test.com");

        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getPhoneNumber()).isEqualTo("111222333");
        assertThat(user.getEmail()).isEqualTo("new@test.com");
    }

    @Test
    void updatePasswordShouldReplacePasswordHash() {
        User user = User.create("p@test.com", "oldHash", Role.PATIENT, "Name", "000");

        user.updatePassword("newHash");

        assertThat(user.getPasswordHash()).isEqualTo("newHash");
    }
}
