package com.vitalscan.vitalscans.repository;

import com.vitalscan.vitalscans.entity.Appointment;
import com.vitalscan.vitalscans.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientOrderByAppointmentDateDesc(User patient);

    List<Appointment> findByNurseOrderByAppointmentDateDesc(User nurse);

    List<Appointment> findByStatusOrderByAppointmentDate(Appointment.AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.patient = :patient AND a.appointmentDate BETWEEN :startDate AND :endDate ORDER BY a.appointmentDate DESC")
    List<Appointment> findByPatientAndDateRange(@Param("patient") User patient, 
                                                @Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Appointment a WHERE a.status = :status ORDER BY a.appointmentDate ASC")
    List<Appointment> findByStatusOrderByAppointmentDateAsc(@Param("status") Appointment.AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate >= :dateTime ORDER BY a.appointmentDate ASC")
    List<Appointment> findUpcomingAppointments(@Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId ORDER BY a.appointmentDate DESC")
    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(@Param("patientId") Long patientId);

    Optional<Appointment> findByIdAndPatient(Long id, User patient);
}
