package com.project.sales_and_inventory_with_ai.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReportService {
    
    BigDecimal getTotalProfit(String period);
    
    Map<String, Object> getMaterialsStockReport(BigDecimal lowStockThreshold);
    
    List<Map<String, Object>> getTopSellingItems(String period, int limit);
    
    Map<String, Object> getSalesSummary(String period);
}
