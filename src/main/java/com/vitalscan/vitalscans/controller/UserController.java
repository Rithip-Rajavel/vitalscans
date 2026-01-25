package com.vitalscan.vitalscans.controller;

import com.vitalscan.vitalscans.entity.User;
import com.vitalscan.vitalscans.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('NURSE') or @userService.getCurrentUser().id == #id")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/identifier/{identifier}")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<User> getUserByIdentifier(@PathVariable String identifier) {
        User user = userService.getUserByIdentifier(identifier);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('NURSE') or @userService.getCurrentUser().id == #id")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().body("User deactivated successfully");
    }

    @GetMapping("/nurse")
    public ResponseEntity<User> getNurse() {
        User nurse = userService.getNurse()
                .orElseThrow(() -> new RuntimeException("No nurse found in the system"));
        return ResponseEntity.ok(nurse);
    }
}
