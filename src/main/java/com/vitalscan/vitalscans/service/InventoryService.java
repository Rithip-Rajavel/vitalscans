package com.vitalscan.vitalscans.service;

import com.vitalscan.vitalscans.entity.Inventory;
import com.vitalscan.vitalscans.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public Inventory createInventory(Inventory inventory) {
        if (inventoryRepository.existsByMedicineNameAndIsActiveTrue(inventory.getMedicineName())) {
            throw new RuntimeException("Medicine already exists in inventory");
        }
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory inventoryDetails) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        inventory.setMedicineName(inventoryDetails.getMedicineName());
        inventory.setDescription(inventoryDetails.getDescription());
        inventory.setCurrentStock(inventoryDetails.getCurrentStock());
        inventory.setMinimumStock(inventoryDetails.getMinimumStock());
        inventory.setUnit(inventoryDetails.getUnit());
        inventory.setManufacturer(inventoryDetails.getManufacturer());
        inventory.setExpiryDate(inventoryDetails.getExpiryDate());
        inventory.setBatchNumber(inventoryDetails.getBatchNumber());

        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findByIsActiveTrueOrderByMedicineName();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));
    }

    public Inventory getInventoryByMedicineName(String medicineName) {
        return inventoryRepository.findByMedicineNameAndIsActiveTrue(medicineName)
                .orElseThrow(() -> new RuntimeException("Medicine not found in inventory"));
    }

    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }

    public List<Inventory> getOutOfStockItems() {
        return inventoryRepository.findOutOfStockItems();
    }

    public void updateStock(Long id, Integer quantityChange) {
        Inventory inventory = getInventoryById(id);
        int newStock = inventory.getCurrentStock() + quantityChange;
        
        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock. Current stock: " + inventory.getCurrentStock());
        }
        
        inventory.setCurrentStock(newStock);
        inventoryRepository.save(inventory);
    }

    public void reduceStock(String medicineName, Integer quantity) {
        Inventory inventory = getInventoryByMedicineName(medicineName);
        
        if (inventory.getCurrentStock() < quantity) {
            throw new RuntimeException("Insufficient stock for " + medicineName + 
                    ". Current stock: " + inventory.getCurrentStock() + ", Required: " + quantity);
        }
        
        inventory.setCurrentStock(inventory.getCurrentStock() - quantity);
        inventoryRepository.save(inventory);
    }

    public void deactivateInventory(Long id) {
        Inventory inventory = getInventoryById(id);
        inventory.setActive(false);
        inventoryRepository.save(inventory);
    }

    public long getLowStockCount() {
        return inventoryRepository.countLowStockItems();
    }

    public long getOutOfStockCount() {
        return inventoryRepository.countOutOfStockItems();
    }
}
