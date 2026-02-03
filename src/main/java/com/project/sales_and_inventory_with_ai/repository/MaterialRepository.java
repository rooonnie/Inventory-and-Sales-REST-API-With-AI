package com.project.sales_and_inventory_with_ai.repository;

import com.project.sales_and_inventory_with_ai.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    
    // Find materials by name (case-insensitive)
    List<Material> findByNameContainingIgnoreCase(String name);
    
    // Find materials with low stock (quantity below threshold)
    List<Material> findByQuantityLessThan(java.math.BigDecimal threshold);
}
