package com.vitalscan.vitalscans.dto;

import com.vitalscan.vitalscans.entity.Appointment;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Appointment date is required")
    private LocalDateTime appointmentDate;

    @NotNull(message = "Type is required")
    private Appointment.AppointmentType type;

    private String symptoms;
    private String notes;
}
