package com.project.sales_and_inventory_with_ai.repository;

import com.project.sales_and_inventory_with_ai.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    // Find sales by date range
    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find sales by food item
    List<Sale> findByFoodItemId(Long foodItemId);
    
    // Calculate total profit for a date range
    @Query("SELECT COALESCE(SUM(s.profit), 0) FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalProfit(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
    // Get top-selling items by quantity for a date range
    @Query("SELECT s.foodItem.id, s.foodItem.name, SUM(s.quantitySold) as totalQuantity " +
           "FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate " +
           "GROUP BY s.foodItem.id, s.foodItem.name " +
           "ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingItems(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    // Get total sales revenue for a date range
    @Query("SELECT COALESCE(SUM(s.salePrice * s.quantitySold), 0) FROM Sale s " +
           "WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalRevenue(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
}
