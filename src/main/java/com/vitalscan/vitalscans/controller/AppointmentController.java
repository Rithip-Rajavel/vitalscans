package com.vitalscan.vitalscans.controller;

import com.vitalscan.vitalscans.dto.AppointmentRequest;
import com.vitalscan.vitalscans.entity.Appointment;
import com.vitalscan.vitalscans.entity.User;
import com.vitalscan.vitalscans.service.AppointmentService;
import com.vitalscan.vitalscans.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'STAFF', 'NURSE')")
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        User currentUser = userService.getCurrentUser();
        Appointment appointment = appointmentService.createAppointment(appointmentRequest, currentUser);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT', 'STAFF', 'NURSE')")
    public ResponseEntity<List<Appointment>> getCurrentUserAppointments() {
        User currentUser = userService.getCurrentUser();
        List<Appointment> appointments = appointmentService.getCurrentUserAppointments(currentUser);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('NURSE') or @userService.getCurrentUser().id == #patientId")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/nurse")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<List<Appointment>> getNurseAppointments() {
        User nurse = userService.getCurrentUser();
        List<Appointment> appointments = appointmentService.getNurseAppointments(nurse);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<List<Appointment>> getPendingAppointments() {
        List<Appointment> appointments = appointmentService.getPendingAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasAnyRole('STUDENT', 'STAFF', 'NURSE')")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments() {
        List<Appointment> appointments = appointmentService.getUpcomingAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('NURSE') or @appointmentService.getAppointmentById(#id).patient.id == @userService.getCurrentUser().id")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<Appointment> approveAppointment(@PathVariable Long id) {
        User nurse = userService.getCurrentUser();
        Appointment appointment = appointmentService.approveAppointment(id, nurse);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestParam Appointment.AppointmentStatus status,
            @RequestParam(required = false) String diagnosis,
            @RequestParam(required = false) String prescription) {
        User nurse = userService.getCurrentUser();
        Appointment appointment = appointmentService.updateAppointment(id, status, diagnosis, prescription, nurse);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('NURSE') or @appointmentService.getAppointmentById(#id).patient.id == @userService.getCurrentUser().id")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok().body("Appointment cancelled successfully");
    }
}
