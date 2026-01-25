package com.vitalscan.vitalscans.service;

import com.vitalscan.vitalscans.dto.HealthMetricsRequest;
import com.vitalscan.vitalscans.entity.HealthMetrics;
import com.vitalscan.vitalscans.entity.User;
import com.vitalscan.vitalscans.repository.HealthMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HealthMetricsService {

    @Autowired
    private HealthMetricsRepository healthMetricsRepository;

    public HealthMetrics createHealthMetrics(HealthMetricsRequest request, User user) {
        HealthMetrics healthMetrics = HealthMetrics.builder()
                .user(user)
                .height(request.getHeight())
                .weight(request.getWeight())
                .bloodPressureSystolic(request.getBloodPressureSystolic())
                .bloodPressureDiastolic(request.getBloodPressureDiastolic())
                .heartRate(request.getHeartRate())
                .temperature(request.getTemperature())
                .oxygenSaturation(request.getOxygenSaturation())
                .notes(request.getNotes())
                .recordedAt(LocalDateTime.now())
                .build();

        return healthMetricsRepository.save(healthMetrics);
    }

    public List<HealthMetrics> getUserHealthMetrics(User user) {
        return healthMetricsRepository.findByUserOrderByRecordedAtDesc(user);
    }

    public HealthMetrics getLatestHealthMetrics(User user) {
        return healthMetricsRepository.findTopByUserOrderByRecordedAtDesc(user)
                .orElse(null);
    }

    public List<HealthMetrics> getUserHealthMetricsByDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return healthMetricsRepository.findByUserAndDateRange(user, startDate, endDate);
    }

    public HealthMetrics updateHealthMetrics(Long id, HealthMetricsRequest request) {
        HealthMetrics healthMetrics = healthMetricsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Health metrics not found"));

        healthMetrics.setHeight(request.getHeight());
        healthMetrics.setWeight(request.getWeight());
        healthMetrics.setBloodPressureSystolic(request.getBloodPressureSystolic());
        healthMetrics.setBloodPressureDiastolic(request.getBloodPressureDiastolic());
        healthMetrics.setHeartRate(request.getHeartRate());
        healthMetrics.setTemperature(request.getTemperature());
        healthMetrics.setOxygenSaturation(request.getOxygenSaturation());
        healthMetrics.setNotes(request.getNotes());

        return healthMetricsRepository.save(healthMetrics);
    }

    public void deleteHealthMetrics(Long id) {
        if (!healthMetricsRepository.existsById(id)) {
            throw new RuntimeException("Health metrics not found");
        }
        healthMetricsRepository.deleteById(id);
    }
}
