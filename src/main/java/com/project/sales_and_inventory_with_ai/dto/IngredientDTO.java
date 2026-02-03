package com.project.sales_and_inventory_with_ai.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    
    private Long id;
    
    @NotNull(message = "Material ID is required")
    private Long materialId;
    
    private String materialName;
    
    @NotNull(message = "Quantity required is required")
    @DecimalMin(value = "0.01", message = "Quantity required must be greater than zero")
    private BigDecimal quantityRequired;
}
