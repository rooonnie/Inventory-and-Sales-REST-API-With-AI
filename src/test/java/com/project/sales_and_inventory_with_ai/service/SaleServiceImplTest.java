package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.FoodItem;
import com.project.sales_and_inventory_with_ai.entity.Ingredient;
import com.project.sales_and_inventory_with_ai.entity.Material;
import com.project.sales_and_inventory_with_ai.entity.Sale;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private FoodItemService foodItemService;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private SaleServiceImpl saleService;

    private Sale testSale;
    private FoodItem testFoodItem;
    private Material testMaterial;
    private Ingredient testIngredient;

    @BeforeEach
    void setUp() {
        testMaterial = new Material(
            1L,
            "Flour",
            "kg",
            new BigDecimal("50.00"),
            new BigDecimal("100"),
            LocalDate.now()
        );

        testFoodItem = new FoodItem();
        testFoodItem.setId(1L);
        testFoodItem.setName("Chocolate Cake");
        testFoodItem.setPricePerServing(new BigDecimal("150.00"));
        testFoodItem.setIngredients(new ArrayList<>());

        testIngredient = new Ingredient();
        testIngredient.setId(1L);
        testIngredient.setMaterial(testMaterial);
        testIngredient.setFoodItem(testFoodItem);
        testIngredient.setQuantityRequired(new BigDecimal("0.5"));
        testFoodItem.getIngredients().add(testIngredient);

        testSale = new Sale();
        testSale.setId(1L);
        testSale.setFoodItem(testFoodItem);
        testSale.setQuantitySold(new BigDecimal("2"));
        testSale.setSalePrice(new BigDecimal("150.00"));
        testSale.setSaleDate(LocalDateTime.now());
    }

    @Test
    void getAllSales_ShouldReturnAllSales() {
        // Arrange
        List<Sale> sales = Arrays.asList(testSale);
        when(saleRepository.findAll()).thenReturn(sales);

        // Act
        List<Sale> result = saleService.getAllSales();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(saleRepository, times(1)).findAll();
    }

    @Test
    void createSale_WithValidData_ShouldCalculateProfitAndDeductStock() {
        // Arrange
        when(foodItemService.getFoodItemById(1L)).thenReturn(testFoodItem);
        when(saleRepository.save(any(Sale.class))).thenReturn(testSale);
        doNothing().when(materialService).deductStock(any(Long.class), any(BigDecimal.class));

        // Act
        Sale result = saleService.createSale(testSale);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getProfit());
        assertNotNull(result.getCostOfIngredients());
        verify(materialService, times(1)).deductStock(eq(1L), any(BigDecimal.class));
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void createSale_WithZeroQuantity_ShouldThrowException() {
        // Arrange
        testSale.setQuantitySold(BigDecimal.ZERO);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            saleService.createSale(testSale);
        });
        assertTrue(exception.getMessage().contains("Quantity sold must be greater than zero"));
    }

    @Test
    void createSale_WithZeroPrice_ShouldThrowException() {
        // Arrange
        testSale.setSalePrice(BigDecimal.ZERO);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            saleService.createSale(testSale);
        });
        assertTrue(exception.getMessage().contains("Sale price must be greater than zero"));
    }

    @Test
    void getSalesByDateRange_ShouldReturnFilteredSales() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        List<Sale> sales = Arrays.asList(testSale);
        when(saleRepository.findBySaleDateBetween(startDate, endDate)).thenReturn(sales);

        // Act
        List<Sale> result = saleService.getSalesByDateRange(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(saleRepository, times(1)).findBySaleDateBetween(startDate, endDate);
    }

    @Test
    void createSale_ShouldSetSaleDateIfNotProvided() {
        // Arrange
        testSale.setSaleDate(null);
        when(foodItemService.getFoodItemById(1L)).thenReturn(testFoodItem);
        when(saleRepository.save(any(Sale.class))).thenReturn(testSale);
        doNothing().when(materialService).deductStock(any(Long.class), any(BigDecimal.class));

        // Act
        Sale result = saleService.createSale(testSale);

        // Assert
        assertNotNull(result);
        verify(saleRepository, times(1)).save(any(Sale.class));
    }
}
