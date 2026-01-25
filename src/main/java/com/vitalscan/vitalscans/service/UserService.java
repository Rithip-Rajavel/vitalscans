package com.vitalscan.vitalscans.service;

import com.vitalscan.vitalscans.entity.User;
import com.vitalscan.vitalscans.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User getUserByIdentifier(String identifier) {
        return userRepository.findByUsernameOrRollNumberOrMobileNumber(identifier)
                .orElseThrow(() -> new RuntimeException("User not found with identifier: " + identifier));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setMobileNumber(userDetails.getMobileNumber());
        user.setHeight(userDetails.getHeight());
        user.setWeight(userDetails.getWeight());
        
        return userRepository.save(user);
    }

    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public Optional<User> getNurse() {
        return userRepository.findNurse();
    }
}
