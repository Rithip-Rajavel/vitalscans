package com.vitalscan.vitalscans.controller;

import com.vitalscan.vitalscans.entity.Inventory;
import com.vitalscan.vitalscans.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody Inventory inventory) {
        Inventory newInventory = inventoryService.createInventory(inventory);
        return ResponseEntity.ok(newInventory);
    }

    @GetMapping
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/medicine/{medicineName}")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<Inventory> getInventoryByMedicineName(@PathVariable String medicineName) {
        Inventory inventory = inventoryService.getInventoryByMedicineName(medicineName);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<List<Inventory>> getLowStockItems() {
        List<Inventory> items = inventoryService.getLowStockItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/out-of-stock")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<List<Inventory>> getOutOfStockItems() {
        List<Inventory> items = inventoryService.getOutOfStockItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<Map<String, Object>> getInventoryStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("lowStockCount", inventoryService.getLowStockCount());
        status.put("outOfStockCount", inventoryService.getOutOfStockCount());
        return ResponseEntity.ok(status);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @Valid @RequestBody Inventory inventoryDetails) {
        Inventory inventory = inventoryService.updateInventory(id, inventoryDetails);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{id}/stock")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestParam Integer quantityChange) {
        inventoryService.updateStock(id, quantityChange);
        return ResponseEntity.ok().body("Stock updated successfully");
    }

    @PutMapping("/medicine/{medicineName}/reduce")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<?> reduceStock(@PathVariable String medicineName, @RequestParam Integer quantity) {
        inventoryService.reduceStock(medicineName, quantity);
        return ResponseEntity.ok().body("Stock reduced successfully for " + medicineName);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<?> deactivateInventory(@PathVariable Long id) {
        inventoryService.deactivateInventory(id);
        return ResponseEntity.ok().body("Inventory item deactivated successfully");
    }
}
