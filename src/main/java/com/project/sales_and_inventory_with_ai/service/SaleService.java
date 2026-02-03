package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.Sale;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {
    
    List<Sale> getAllSales();
    
    Sale getSaleById(Long id);
    
    Sale createSale(Sale sale);
    
    List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Sale> getSalesByFoodItem(Long foodItemId);
}
