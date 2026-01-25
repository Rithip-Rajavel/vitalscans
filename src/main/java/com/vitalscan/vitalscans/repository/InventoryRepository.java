package com.vitalscan.vitalscans.repository;

import com.vitalscan.vitalscans.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByIsActiveTrueOrderByMedicineName();

    List<Inventory> findByCurrentStockLessThanEqualOrderByMedicineName(Integer threshold);

    @Query("SELECT i FROM Inventory i WHERE i.isActive = true AND i.currentStock <= i.minimumStock")
    List<Inventory> findLowStockItems();

    @Query("SELECT i FROM Inventory i WHERE i.isActive = true AND i.currentStock <= 0")
    List<Inventory> findOutOfStockItems();

    Optional<Inventory> findByMedicineNameAndIsActiveTrue(String medicineName);

    boolean existsByMedicineNameAndIsActiveTrue(String medicineName);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.isActive = true AND i.currentStock <= i.minimumStock")
    long countLowStockItems();

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.isActive = true AND i.currentStock <= 0")
    long countOutOfStockItems();
}
