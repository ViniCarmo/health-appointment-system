CREATE TABLE appointments (
                              id                  UUID PRIMARY KEY,
                              patient_id          UUID NOT NULL,
                              doctor_id           UUID NOT NULL,
                              date_time           TIMESTAMP    NOT NULL,
                              status              VARCHAR(20)  NOT NULL,
                              notes               VARCHAR(500),
                              created_at          TIMESTAMP    NOT NULL,
                              updated_at          TIMESTAMP    NOT NULL,
                              created_by_user_id  UUID NOT NULL,
                              CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES users(id),
                              CONSTRAINT fk_appointments_doctor FOREIGN KEY (doctor_id) REFERENCES users(id),
                              CONSTRAINT fk_appointments_created_by FOREIGN KEY (created_by_user_id) REFERENCES users(id)
);