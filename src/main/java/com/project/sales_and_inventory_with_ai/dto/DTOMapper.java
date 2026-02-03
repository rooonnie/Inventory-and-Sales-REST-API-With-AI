package com.project.sales_and_inventory_with_ai.dto;

import com.project.sales_and_inventory_with_ai.entity.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DTOMapper {
    
    // Material mappings
    public MaterialDTO toMaterialDTO(Material material) {
        if (material == null) return null;
        
        MaterialDTO dto = new MaterialDTO();
        dto.setId(material.getId());
        dto.setName(material.getName());
        dto.setUnit(material.getUnit());
        dto.setPricePerUnit(material.getPricePerUnit());
        dto.setQuantity(material.getQuantity());
        dto.setDatePurchased(material.getDatePurchased());
        return dto;
    }
    
    public Material toMaterialEntity(MaterialDTO dto) {
        if (dto == null) return null;
        
        Material material = new Material();
        material.setId(dto.getId());
        material.setName(dto.getName());
        material.setUnit(dto.getUnit());
        material.setPricePerUnit(dto.getPricePerUnit());
        material.setQuantity(dto.getQuantity());
        material.setDatePurchased(dto.getDatePurchased());
        return material;
    }
    
    // Ingredient mappings
    public IngredientDTO toIngredientDTO(Ingredient ingredient) {
        if (ingredient == null) return null;
        
        IngredientDTO dto = new IngredientDTO();
        dto.setId(ingredient.getId());
        dto.setMaterialId(ingredient.getMaterial().getId());
        dto.setMaterialName(ingredient.getMaterial().getName());
        dto.setQuantityRequired(ingredient.getQuantityRequired());
        return dto;
    }
    
    public Ingredient toIngredientEntity(IngredientDTO dto) {
        if (dto == null) return null;
        
        Ingredient ingredient = new Ingredient();
        ingredient.setId(dto.getId());
        
        Material material = new Material();
        material.setId(dto.getMaterialId());
        ingredient.setMaterial(material);
        
        ingredient.setQuantityRequired(dto.getQuantityRequired());
        return ingredient;
    }
    
    // FoodItem mappings
    public FoodItemDTO toFoodItemDTO(FoodItem foodItem) {
        if (foodItem == null) return null;
        
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(foodItem.getId());
        dto.setName(foodItem.getName());
        dto.setPricePerServing(foodItem.getPricePerServing());
        
        if (foodItem.getIngredients() != null) {
            dto.setIngredients(
                foodItem.getIngredients().stream()
                    .map(this::toIngredientDTO)
                    .collect(Collectors.toList())
            );
        }
        
        return dto;
    }
    
    public FoodItem toFoodItemEntity(FoodItemDTO dto) {
        if (dto == null) return null;
        
        FoodItem foodItem = new FoodItem();
        foodItem.setId(dto.getId());
        foodItem.setName(dto.getName());
        foodItem.setPricePerServing(dto.getPricePerServing());
        
        if (dto.getIngredients() != null) {
            foodItem.setIngredients(
                dto.getIngredients().stream()
                    .map(this::toIngredientEntity)
                    .collect(Collectors.toList())
            );
        }
        
        return foodItem;
    }
    
    // Sale mappings
    public SaleDTO toSaleDTO(Sale sale) {
        if (sale == null) return null;
        
        SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        dto.setFoodItemId(sale.getFoodItem().getId());
        dto.setFoodItemName(sale.getFoodItem().getName());
        dto.setQuantitySold(sale.getQuantitySold());
        dto.setSalePrice(sale.getSalePrice());
        dto.setSaleDate(sale.getSaleDate());
        dto.setProfit(sale.getProfit());
        dto.setCostOfIngredients(sale.getCostOfIngredients());
        return dto;
    }
    
    public Sale toSaleEntity(SaleDTO dto) {
        if (dto == null) return null;
        
        Sale sale = new Sale();
        sale.setId(dto.getId());
        
        FoodItem foodItem = new FoodItem();
        foodItem.setId(dto.getFoodItemId());
        sale.setFoodItem(foodItem);
        
        sale.setQuantitySold(dto.getQuantitySold());
        sale.setSalePrice(dto.getSalePrice());
        sale.setSaleDate(dto.getSaleDate());
        sale.setProfit(dto.getProfit());
        sale.setCostOfIngredients(dto.getCostOfIngredients());
        return sale;
    }
}
