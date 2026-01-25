package com.vitalscan.vitalscans.repository;

import com.vitalscan.vitalscans.entity.HealthMetrics;
import com.vitalscan.vitalscans.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthMetricsRepository extends JpaRepository<HealthMetrics, Long> {

    List<HealthMetrics> findByUserOrderByRecordedAtDesc(User user);

    Optional<HealthMetrics> findTopByUserOrderByRecordedAtDesc(User user);

    @Query("SELECT hm FROM HealthMetrics hm WHERE hm.user = :user AND hm.recordedAt BETWEEN :startDate AND :endDate ORDER BY hm.recordedAt DESC")
    List<HealthMetrics> findByUserAndDateRange(@Param("user") User user, 
                                               @Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT hm FROM HealthMetrics hm WHERE hm.user.id = :userId ORDER BY hm.recordedAt DESC")
    List<HealthMetrics> findByUserIdOrderByRecordedAtDesc(@Param("userId") Long userId);
}
