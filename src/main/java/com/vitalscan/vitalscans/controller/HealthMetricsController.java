package com.vitalscan.vitalscans.controller;

import com.vitalscan.vitalscans.dto.HealthMetricsRequest;
import com.vitalscan.vitalscans.entity.HealthMetrics;
import com.vitalscan.vitalscans.entity.User;
import com.vitalscan.vitalscans.service.HealthMetricsService;
import com.vitalscan.vitalscans.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/health-metrics")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HealthMetricsController {

    @Autowired
    private HealthMetricsService healthMetricsService;

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'STAFF', 'NURSE')")
    public ResponseEntity<HealthMetrics> createHealthMetrics(@Valid @RequestBody HealthMetricsRequest request) {
        User currentUser = userService.getCurrentUser();
        HealthMetrics healthMetrics = healthMetricsService.createHealthMetrics(request, currentUser);
        return ResponseEntity.ok(healthMetrics);
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<HealthMetrics> createHealthMetricsForUser(
            @PathVariable Long userId,
            @Valid @RequestBody HealthMetricsRequest request) {
        User user = userService.getUserById(userId);
        HealthMetrics healthMetrics = healthMetricsService.createHealthMetrics(request, user);
        return ResponseEntity.ok(healthMetrics);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT', 'STAFF', 'NURSE')")
    public ResponseEntity<List<HealthMetrics>> getCurrentUserHealthMetrics() {
        User currentUser = userService.getCurrentUser();
        List<HealthMetrics> healthMetrics = healthMetricsService.getUserHealthMetrics(currentUser);
        return ResponseEntity.ok(healthMetrics);
    }

    @GetMapping("/my/latest")
    @PreAuthorize("hasAnyRole('STUDENT', 'STAFF', 'NURSE')")
    public ResponseEntity<HealthMetrics> getLatestCurrentUserHealthMetrics() {
        User currentUser = userService.getCurrentUser();
        HealthMetrics healthMetrics = healthMetricsService.getLatestHealthMetrics(currentUser);
        return ResponseEntity.ok(healthMetrics);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('NURSE') or @userService.getCurrentUser().id == #userId")
    public ResponseEntity<List<HealthMetrics>> getUserHealthMetrics(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<HealthMetrics> healthMetrics = healthMetricsService.getUserHealthMetrics(user);
        return ResponseEntity.ok(healthMetrics);
    }

    @GetMapping("/user/{userId}/latest")
    @PreAuthorize("hasRole('NURSE') or @userService.getCurrentUser().id == #userId")
    public ResponseEntity<HealthMetrics> getLatestUserHealthMetrics(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        HealthMetrics healthMetrics = healthMetricsService.getLatestHealthMetrics(user);
        return ResponseEntity.ok(healthMetrics);
    }

    @GetMapping("/user/{userId}/range")
    @PreAuthorize("hasRole('NURSE') or @userService.getCurrentUser().id == #userId")
    public ResponseEntity<List<HealthMetrics>> getUserHealthMetricsByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        User user = userService.getUserById(userId);
        List<HealthMetrics> healthMetrics = healthMetricsService.getUserHealthMetricsByDateRange(user, startDate, endDate);
        return ResponseEntity.ok(healthMetrics);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<HealthMetrics> getHealthMetricsById(@PathVariable Long id) {
        HealthMetrics healthMetrics = healthMetricsService.getLatestHealthMetrics(null);
        return ResponseEntity.ok(healthMetrics);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<HealthMetrics> updateHealthMetrics(
            @PathVariable Long id,
            @Valid @RequestBody HealthMetricsRequest request) {
        HealthMetrics healthMetrics = healthMetricsService.updateHealthMetrics(id, request);
        return ResponseEntity.ok(healthMetrics);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<?> deleteHealthMetrics(@PathVariable Long id) {
        healthMetricsService.deleteHealthMetrics(id);
        return ResponseEntity.ok().body("Health metrics deleted successfully");
    }
}
