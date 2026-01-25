package com.vitalscan.vitalscans.service;

import com.vitalscan.vitalscans.dto.AppointmentRequest;
import com.vitalscan.vitalscans.entity.Appointment;
import com.vitalscan.vitalscans.entity.User;
import com.vitalscan.vitalscans.repository.AppointmentRepository;
import com.vitalscan.vitalscans.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    public Appointment createAppointment(AppointmentRequest appointmentRequest, User currentUser) {
        User patient = userRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .appointmentDate(appointmentRequest.getAppointmentDate())
                .status(Appointment.AppointmentStatus.PENDING)
                .type(appointmentRequest.getType())
                .symptoms(appointmentRequest.getSymptoms())
                .notes(appointmentRequest.getNotes())
                .build();

        return appointmentRepository.save(appointment);
    }

    public Appointment approveAppointment(Long appointmentId, User nurse) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(Appointment.AppointmentStatus.APPROVED);
        appointment.setNurse(nurse);

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long appointmentId, Appointment.AppointmentStatus status, 
                                      String diagnosis, String prescription, User nurse) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(status);
        appointment.setNurse(nurse);
        appointment.setDiagnosis(diagnosis);
        appointment.setPrescription(prescription);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return appointmentRepository.findByPatientOrderByAppointmentDateDesc(patient);
    }

    public List<Appointment> getCurrentUserAppointments(User currentUser) {
        return appointmentRepository.findByPatientOrderByAppointmentDateDesc(currentUser);
    }

    public List<Appointment> getNurseAppointments(User nurse) {
        return appointmentRepository.findByNurseOrderByAppointmentDateDesc(nurse);
    }

    public List<Appointment> getPendingAppointments() {
        return appointmentRepository.findByStatusOrderByAppointmentDate(Appointment.AppointmentStatus.PENDING);
    }

    public List<Appointment> getUpcomingAppointments() {
        return appointmentRepository.findUpcomingAppointments(LocalDateTime.now());
    }

    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }
}
