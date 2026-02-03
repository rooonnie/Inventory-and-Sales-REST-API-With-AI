package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.FoodItem;
import com.project.sales_and_inventory_with_ai.entity.Ingredient;
import com.project.sales_and_inventory_with_ai.entity.Material;
import com.project.sales_and_inventory_with_ai.repository.FoodItemRepository;
import com.project.sales_and_inventory_with_ai.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodItemServiceImpl implements FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final IngredientRepository ingredientRepository;
    private final MaterialService materialService;

    @Override
    @Transactional(readOnly = true)
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAllWithIngredients();
    }

    @Override
    @Transactional(readOnly = true)
    public FoodItem getFoodItemById(Long id) {
        return foodItemRepository.findByIdWithIngredients(id)
                .orElseThrow(() -> new RuntimeException("Food item not found with id: " + id));
    }

    @Override
    public FoodItem createFoodItem(FoodItem foodItem) {
        if (foodItem.getPricePerServing().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Price per serving must be greater than zero");
        }
        
        // Save food item first
        FoodItem savedFoodItem = foodItemRepository.save(foodItem);
        
        // Then save ingredients with reference to saved food item
        if (foodItem.getIngredients() != null && !foodItem.getIngredients().isEmpty()) {
            for (Ingredient ingredient : foodItem.getIngredients()) {
                // Verify material exists
                Material material = materialService.getMaterialById(ingredient.getMaterial().getId());
                ingredient.setMaterial(material);
                ingredient.setFoodItem(savedFoodItem);
                
                if (ingredient.getQuantityRequired().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new RuntimeException("Ingredient quantity must be greater than zero");
                }
            }
            savedFoodItem.setIngredients(foodItem.getIngredients());
            ingredientRepository.saveAll(foodItem.getIngredients());
        }
        
        return savedFoodItem;
    }

    @Override
    public FoodItem updateFoodItem(Long id, FoodItem foodItem) {
        FoodItem existingFoodItem = getFoodItemById(id);
        
        existingFoodItem.setName(foodItem.getName());
        existingFoodItem.setPricePerServing(foodItem.getPricePerServing());
        
        // Clear existing ingredients
        ingredientRepository.deleteByFoodItemId(id);
        existingFoodItem.getIngredients().clear();
        
        // Add new ingredients
        if (foodItem.getIngredients() != null && !foodItem.getIngredients().isEmpty()) {
            for (Ingredient ingredient : foodItem.getIngredients()) {
                Material material = materialService.getMaterialById(ingredient.getMaterial().getId());
                ingredient.setMaterial(material);
                ingredient.setFoodItem(existingFoodItem);
                existingFoodItem.addIngredient(ingredient);
            }
        }
        
        return foodItemRepository.save(existingFoodItem);
    }

    @Override
    public void deleteFoodItem(Long id) {
        if (!foodItemRepository.existsById(id)) {
            throw new RuntimeException("Food item not found with id: " + id);
        }
        foodItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodItem> searchFoodItemsByName(String name) {
        return foodItemRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public FoodItem addIngredientToFoodItem(Long foodItemId, Ingredient ingredient) {
        FoodItem foodItem = getFoodItemById(foodItemId);
        Material material = materialService.getMaterialById(ingredient.getMaterial().getId());
        
        ingredient.setMaterial(material);
        ingredient.setFoodItem(foodItem);
        
        ingredientRepository.save(ingredient);
        foodItem.addIngredient(ingredient);
        
        return foodItem;
    }

    @Override
    public void removeIngredientFromFoodItem(Long foodItemId, Long ingredientId) {
        FoodItem foodItem = getFoodItemById(foodItemId);
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + ingredientId));
        
        if (!ingredient.getFoodItem().getId().equals(foodItemId)) {
            throw new RuntimeException("Ingredient does not belong to this food item");
        }
        
        foodItem.removeIngredient(ingredient);
        ingredientRepository.delete(ingredient);
    }
}
