package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.Material;
import com.project.sales_and_inventory_with_ai.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final SaleRepository saleRepository;
    private final MaterialService materialService;

    @Override
    public BigDecimal getTotalProfit(String period) {
        LocalDateTime[] dateRange = getDateRangeForPeriod(period);
        return saleRepository.calculateTotalProfit(dateRange[0], dateRange[1]);
    }

    @Override
    public Map<String, Object> getMaterialsStockReport(BigDecimal lowStockThreshold) {
        List<Material> allMaterials = materialService.getAllMaterials();
        List<Material> lowStockMaterials = materialService.getLowStockMaterials(
                lowStockThreshold != null ? lowStockThreshold : BigDecimal.TEN
        );
        
        Map<String, Object> report = new HashMap<>();
        report.put("totalMaterials", allMaterials.size());
        report.put("lowStockCount", lowStockMaterials.size());
        report.put("materials", allMaterials);
        report.put("lowStockMaterials", lowStockMaterials);
        report.put("lowStockThreshold", lowStockThreshold != null ? lowStockThreshold : BigDecimal.TEN);
        
        return report;
    }

    @Override
    public List<Map<String, Object>> getTopSellingItems(String period, int limit) {
        LocalDateTime[] dateRange = getDateRangeForPeriod(period);
        List<Object[]> results = saleRepository.findTopSellingItems(dateRange[0], dateRange[1]);
        
        return results.stream()
                .limit(limit > 0 ? limit : 10)
                .map(result -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("foodItemId", result[0]);
                    item.put("foodItemName", result[1]);
                    item.put("totalQuantitySold", result[2]);
                    return item;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getSalesSummary(String period) {
        LocalDateTime[] dateRange = getDateRangeForPeriod(period);
        
        BigDecimal totalProfit = saleRepository.calculateTotalProfit(dateRange[0], dateRange[1]);
        BigDecimal totalRevenue = saleRepository.calculateTotalRevenue(dateRange[0], dateRange[1]);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("period", period);
        summary.put("startDate", dateRange[0]);
        summary.put("endDate", dateRange[1]);
        summary.put("totalProfit", totalProfit);
        summary.put("totalRevenue", totalRevenue);
        summary.put("totalCost", totalRevenue.subtract(totalProfit));
        
        return summary;
    }

    private LocalDateTime[] getDateRangeForPeriod(String period) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate;
        
        switch (period.toLowerCase()) {
            case "today":
                startDate = endDate.toLocalDate().atStartOfDay();
                break;
            case "2days":
                startDate = endDate.minusDays(2);
                break;
            case "week":
                startDate = endDate.minusWeeks(1);
                break;
            case "month":
                startDate = endDate.minusMonths(1);
                break;
            case "year":
                startDate = endDate.minusYears(1);
                break;
            default:
                throw new RuntimeException("Invalid period. Valid values: today, 2days, week, month, year");
        }
        
        return new LocalDateTime[]{startDate, endDate};
    }
}
