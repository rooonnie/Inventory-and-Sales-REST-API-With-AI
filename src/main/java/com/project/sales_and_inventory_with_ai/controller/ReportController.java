package com.project.sales_and_inventory_with_ai.controller;

import com.project.sales_and_inventory_with_ai.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/profit")
    public ResponseEntity<Map<String, Object>> getProfitReport(
            @RequestParam(defaultValue = "today") String period) {
        
        BigDecimal totalProfit = reportService.getTotalProfit(period);
        Map<String, Object> summary = reportService.getSalesSummary(period);
        
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/materials")
    public ResponseEntity<Map<String, Object>> getMaterialsReport(
            @RequestParam(required = false, defaultValue = "10") BigDecimal lowStockThreshold) {
        
        Map<String, Object> report = reportService.getMaterialsStockReport(lowStockThreshold);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/top-items")
    public ResponseEntity<List<Map<String, Object>>> getTopSellingItems(
            @RequestParam(defaultValue = "today") String period,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<Map<String, Object>> topItems = reportService.getTopSellingItems(period, limit);
        return ResponseEntity.ok(topItems);
    }
}
