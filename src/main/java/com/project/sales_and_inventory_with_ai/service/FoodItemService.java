package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.FoodItem;
import com.project.sales_and_inventory_with_ai.entity.Ingredient;

import java.util.List;

public interface FoodItemService {
    
    List<FoodItem> getAllFoodItems();
    
    FoodItem getFoodItemById(Long id);
    
    FoodItem createFoodItem(FoodItem foodItem);
    
    FoodItem updateFoodItem(Long id, FoodItem foodItem);
    
    void deleteFoodItem(Long id);
    
    List<FoodItem> searchFoodItemsByName(String name);
    
    FoodItem addIngredientToFoodItem(Long foodItemId, Ingredient ingredient);
    
    void removeIngredientFromFoodItem(Long foodItemId, Long ingredientId);
}
