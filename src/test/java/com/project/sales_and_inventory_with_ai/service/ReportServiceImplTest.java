package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.Material;
import com.project.sales_and_inventory_with_ai.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private ReportServiceImpl reportService;

    private List<Material> testMaterials;

    @BeforeEach
    void setUp() {
        Material material1 = new Material(
            1L,
            "Flour",
            "kg",
            new BigDecimal("50.00"),
            new BigDecimal("100"),
            LocalDate.now()
        );
        Material material2 = new Material(
            2L,
            "Sugar",
            "kg",
            new BigDecimal("60.00"),
            new BigDecimal("5"),
            LocalDate.now()
        );
        testMaterials = Arrays.asList(material1, material2);
    }

    @Test
    void getTotalProfit_ForToday_ShouldReturnProfit() {
        // Arrange
        BigDecimal expectedProfit = new BigDecimal("1000.00");
        when(saleRepository.calculateTotalProfit(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(expectedProfit);

        // Act
        BigDecimal result = reportService.getTotalProfit("today");

        // Assert
        assertNotNull(result);
        assertEquals(expectedProfit, result);
        verify(saleRepository, times(1)).calculateTotalProfit(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getTotalProfit_ForWeek_ShouldReturnProfit() {
        // Arrange
        BigDecimal expectedProfit = new BigDecimal("5000.00");
        when(saleRepository.calculateTotalProfit(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(expectedProfit);

        // Act
        BigDecimal result = reportService.getTotalProfit("week");

        // Assert
        assertNotNull(result);
        assertEquals(expectedProfit, result);
    }

    @Test
    void getTotalProfit_WithInvalidPeriod_ShouldThrowException() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reportService.getTotalProfit("invalid");
        });
        assertTrue(exception.getMessage().contains("Invalid period"));
    }

    @Test
    void getMaterialsStockReport_ShouldReturnStockReport() {
        // Arrange
        List<Material> lowStockMaterials = Arrays.asList(testMaterials.get(1));
        when(materialService.getAllMaterials()).thenReturn(testMaterials);
        when(materialService.getLowStockMaterials(new BigDecimal("10")))
            .thenReturn(lowStockMaterials);

        // Act
        Map<String, Object> result = reportService.getMaterialsStockReport(new BigDecimal("10"));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.get("totalMaterials"));
        assertEquals(1, result.get("lowStockCount"));
        assertEquals(new BigDecimal("10"), result.get("lowStockThreshold"));
        assertNotNull(result.get("materials"));
        assertNotNull(result.get("lowStockMaterials"));
        verify(materialService, times(1)).getAllMaterials();
        verify(materialService, times(1)).getLowStockMaterials(new BigDecimal("10"));
    }

    @Test
    void getMaterialsStockReport_WithNullThreshold_ShouldUseDefault() {
        // Arrange
        when(materialService.getAllMaterials()).thenReturn(testMaterials);
        when(materialService.getLowStockMaterials(BigDecimal.TEN))
            .thenReturn(new ArrayList<>());

        // Act
        Map<String, Object> result = reportService.getMaterialsStockReport(null);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.TEN, result.get("lowStockThreshold"));
        verify(materialService, times(1)).getLowStockMaterials(BigDecimal.TEN);
    }

    @Test
    void getTopSellingItems_ShouldReturnTopItems() {
        // Arrange
        Object[] item1 = {1L, "Chocolate Cake", new BigDecimal("50")};
        Object[] item2 = {2L, "Vanilla Cupcake", new BigDecimal("30")};
        List<Object[]> topItems = Arrays.asList(item1, item2);
        
        when(saleRepository.findTopSellingItems(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(topItems);

        // Act
        List<Map<String, Object>> result = reportService.getTopSellingItems("today", 10);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).get("foodItemId"));
        assertEquals("Chocolate Cake", result.get(0).get("foodItemName"));
        assertEquals(new BigDecimal("50"), result.get(0).get("totalQuantitySold"));
        verify(saleRepository, times(1)).findTopSellingItems(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getSalesSummary_ShouldReturnSummary() {
        // Arrange
        BigDecimal totalProfit = new BigDecimal("1000.00");
        BigDecimal totalRevenue = new BigDecimal("5000.00");
        
        when(saleRepository.calculateTotalProfit(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(totalProfit);
        when(saleRepository.calculateTotalRevenue(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(totalRevenue);

        // Act
        Map<String, Object> result = reportService.getSalesSummary("today");

        // Assert
        assertNotNull(result);
        assertEquals("today", result.get("period"));
        assertEquals(totalProfit, result.get("totalProfit"));
        assertEquals(totalRevenue, result.get("totalRevenue"));
        assertEquals(new BigDecimal("4000.00"), result.get("totalCost"));
        assertNotNull(result.get("startDate"));
        assertNotNull(result.get("endDate"));
    }
}
