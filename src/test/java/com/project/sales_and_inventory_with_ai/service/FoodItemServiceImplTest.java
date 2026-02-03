package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.FoodItem;
import com.project.sales_and_inventory_with_ai.entity.Ingredient;
import com.project.sales_and_inventory_with_ai.entity.Material;
import com.project.sales_and_inventory_with_ai.repository.FoodItemRepository;
import com.project.sales_and_inventory_with_ai.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodItemServiceImplTest {

    @Mock
    private FoodItemRepository foodItemRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private FoodItemServiceImpl foodItemService;

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
        testIngredient.setQuantityRequired(new BigDecimal("0.5"));
    }

    @Test
    void getAllFoodItems_ShouldReturnAllFoodItems() {
        // Arrange
        List<FoodItem> foodItems = Arrays.asList(testFoodItem);
        when(foodItemRepository.findAllWithIngredients()).thenReturn(foodItems);

        // Act
        List<FoodItem> result = foodItemService.getAllFoodItems();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Chocolate Cake", result.get(0).getName());
        verify(foodItemRepository, times(1)).findAllWithIngredients();
    }

    @Test
    void getFoodItemById_WhenExists_ShouldReturnFoodItem() {
        // Arrange
        when(foodItemRepository.findByIdWithIngredients(1L)).thenReturn(Optional.of(testFoodItem));

        // Act
        FoodItem result = foodItemService.getFoodItemById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Chocolate Cake", result.getName());
        verify(foodItemRepository, times(1)).findByIdWithIngredients(1L);
    }

    @Test
    void getFoodItemById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(foodItemRepository.findByIdWithIngredients(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            foodItemService.getFoodItemById(999L);
        });
        assertTrue(exception.getMessage().contains("Food item not found"));
    }

    @Test
    void createFoodItem_WithValidData_ShouldSaveFoodItem() {
        // Arrange
        testIngredient.setFoodItem(testFoodItem);
        testFoodItem.getIngredients().add(testIngredient);
        
        when(foodItemRepository.save(any(FoodItem.class))).thenReturn(testFoodItem);
        when(materialService.getMaterialById(1L)).thenReturn(testMaterial);
        when(ingredientRepository.saveAll(any())).thenReturn(Arrays.asList(testIngredient));

        // Act
        FoodItem result = foodItemService.createFoodItem(testFoodItem);

        // Assert
        assertNotNull(result);
        assertEquals("Chocolate Cake", result.getName());
        verify(foodItemRepository, times(1)).save(any(FoodItem.class));
    }

    @Test
    void createFoodItem_WithZeroPrice_ShouldThrowException() {
        // Arrange
        testFoodItem.setPricePerServing(BigDecimal.ZERO);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            foodItemService.createFoodItem(testFoodItem);
        });
        assertTrue(exception.getMessage().contains("Price per serving must be greater than zero"));
    }

    @Test
    void deleteFoodItem_WhenExists_ShouldDeleteFoodItem() {
        // Arrange
        when(foodItemRepository.existsById(1L)).thenReturn(true);
        doNothing().when(foodItemRepository).deleteById(1L);

        // Act
        foodItemService.deleteFoodItem(1L);

        // Assert
        verify(foodItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteFoodItem_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(foodItemRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            foodItemService.deleteFoodItem(999L);
        });
        assertTrue(exception.getMessage().contains("Food item not found"));
    }
}
