package com.project.sales_and_inventory_with_ai.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemDTO {
    
    private Long id;
    
    @NotBlank(message = "Food item name is required")
    private String name;
    
    @NotNull(message = "Price per serving is required")
    @DecimalMin(value = "0.01", message = "Price per serving must be greater than zero")
    private BigDecimal pricePerServing;
    
    @Valid
    private List<IngredientDTO> ingredients = new ArrayList<>();
}
