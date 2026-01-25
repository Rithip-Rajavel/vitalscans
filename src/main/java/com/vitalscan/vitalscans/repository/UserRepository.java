package com.vitalscan.vitalscans.repository;

import com.vitalscan.vitalscans.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByRollNumber(String rollNumber);

    Optional<User> findByMobileNumber(String mobileNumber);

    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.rollNumber = :identifier OR u.mobileNumber = :identifier")
    Optional<User> findByUsernameOrRollNumberOrMobileNumber(@Param("identifier") String identifier);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByRollNumber(String rollNumber);

    boolean existsByMobileNumber(String mobileNumber);

    @Query("SELECT u FROM User u WHERE u.role = com.vitalscan.vitalscans.entity.User.Role.NURSE")
    Optional<User> findNurse();
}
