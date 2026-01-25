package com.vitalscan.vitalscans.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class HealthMetricsRequest {

    private Double height;
    private Double weight;
    @Positive(message = "Blood pressure systolic must be positive")
    private Integer bloodPressureSystolic;
    @Positive(message = "Blood pressure diastolic must be positive")
    private Integer bloodPressureDiastolic;
    @Positive(message = "Heart rate must be positive")
    private Integer heartRate;
    private Double temperature;
    private Double oxygenSaturation;
    private String notes;
}
