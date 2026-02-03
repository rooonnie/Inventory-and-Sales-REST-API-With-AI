package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.FoodItem;
import com.project.sales_and_inventory_with_ai.entity.Ingredient;
import com.project.sales_and_inventory_with_ai.entity.Sale;
import com.project.sales_and_inventory_with_ai.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final FoodItemService foodItemService;
    private final MaterialService materialService;

    @Override
    @Transactional(readOnly = true)
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Sale getSaleById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
    }

    @Override
    public Sale createSale(Sale sale) {
        // Validate sale data
        if (sale.getQuantitySold().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Quantity sold must be greater than zero");
        }
        if (sale.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Sale price must be greater than zero");
        }
        
        // Get food item with ingredients
        FoodItem foodItem = foodItemService.getFoodItemById(sale.getFoodItem().getId());
        sale.setFoodItem(foodItem);
        
        // Set sale date if not provided
        if (sale.getSaleDate() == null) {
            sale.setSaleDate(LocalDateTime.now());
        }
        
        // Calculate cost of ingredients and deduct stock
        BigDecimal totalCost = BigDecimal.ZERO;
        
        for (Ingredient ingredient : foodItem.getIngredients()) {
            // Calculate quantity needed for this sale
            BigDecimal quantityNeeded = ingredient.getQuantityRequired().multiply(sale.getQuantitySold());
            
            // Deduct stock
            materialService.deductStock(ingredient.getMaterial().getId(), quantityNeeded);
            
            // Calculate cost
            BigDecimal ingredientCost = ingredient.getMaterial().getPricePerUnit().multiply(quantityNeeded);
            totalCost = totalCost.add(ingredientCost);
        }
        
        sale.setCostOfIngredients(totalCost);
        
        // Calculate profit: (sale_price * quantity_sold) - cost_of_ingredients
        BigDecimal totalRevenue = sale.getSalePrice().multiply(sale.getQuantitySold());
        BigDecimal profit = totalRevenue.subtract(totalCost);
        sale.setProfit(profit);
        
        return saleRepository.save(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findBySaleDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> getSalesByFoodItem(Long foodItemId) {
        return saleRepository.findByFoodItemId(foodItemId);
    }
}
