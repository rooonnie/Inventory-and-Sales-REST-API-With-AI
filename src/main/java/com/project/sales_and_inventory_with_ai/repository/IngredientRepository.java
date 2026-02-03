package com.project.sales_and_inventory_with_ai.repository;

import com.project.sales_and_inventory_with_ai.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    
    // Find ingredients by food item
    List<Ingredient> findByFoodItemId(Long foodItemId);
    
    // Find ingredients by material
    List<Ingredient> findByMaterialId(Long materialId);
    
    // Delete all ingredients for a food item
    void deleteByFoodItemId(Long foodItemId);
}
