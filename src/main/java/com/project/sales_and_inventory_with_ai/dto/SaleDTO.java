package com.project.sales_and_inventory_with_ai.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    
    private Long id;
    
    @NotNull(message = "Food item ID is required")
    private Long foodItemId;
    
    private String foodItemName;
    
    @NotNull(message = "Quantity sold is required")
    @DecimalMin(value = "0.01", message = "Quantity sold must be greater than zero")
    private BigDecimal quantitySold;
    
    @NotNull(message = "Sale price is required")
    @DecimalMin(value = "0.01", message = "Sale price must be greater than zero")
    private BigDecimal salePrice;
    
    private LocalDateTime saleDate;
    
    private BigDecimal profit;
    
    private BigDecimal costOfIngredients;
}
