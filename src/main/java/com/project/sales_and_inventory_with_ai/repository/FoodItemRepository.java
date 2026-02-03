package com.project.sales_and_inventory_with_ai.repository;

import com.project.sales_and_inventory_with_ai.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    
    // Find food items by name (case-insensitive)
    List<FoodItem> findByNameContainingIgnoreCase(String name);
    
    // Find food item with ingredients loaded (to avoid N+1 queries)
    @Query("SELECT f FROM FoodItem f LEFT JOIN FETCH f.ingredients WHERE f.id = :id")
    Optional<FoodItem> findByIdWithIngredients(Long id);
    
    // Find all food items with ingredients loaded
    @Query("SELECT DISTINCT f FROM FoodItem f LEFT JOIN FETCH f.ingredients")
    List<FoodItem> findAllWithIngredients();
}
