package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.Material;
import com.project.sales_and_inventory_with_ai.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceImplTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialServiceImpl materialService;

    private Material testMaterial;

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
    }

    @Test
    void getAllMaterials_ShouldReturnAllMaterials() {
        // Arrange
        List<Material> materials = Arrays.asList(testMaterial);
        when(materialRepository.findAll()).thenReturn(materials);

        // Act
        List<Material> result = materialService.getAllMaterials();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Flour", result.get(0).getName());
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    void getMaterialById_WhenExists_ShouldReturnMaterial() {
        // Arrange
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));

        // Act
        Material result = materialService.getMaterialById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Flour", result.getName());
        assertEquals(new BigDecimal("50.00"), result.getPricePerUnit());
        verify(materialRepository, times(1)).findById(1L);
    }

    @Test
    void getMaterialById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(materialRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            materialService.getMaterialById(999L);
        });
        assertTrue(exception.getMessage().contains("Material not found"));
    }

    @Test
    void createMaterial_WithValidData_ShouldSaveMaterial() {
        // Arrange
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);

        // Act
        Material result = materialService.createMaterial(testMaterial);

        // Assert
        assertNotNull(result);
        assertEquals("Flour", result.getName());
        verify(materialRepository, times(1)).save(testMaterial);
    }

    @Test
    void createMaterial_WithNegativeQuantity_ShouldThrowException() {
        // Arrange
        testMaterial.setQuantity(new BigDecimal("-10"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            materialService.createMaterial(testMaterial);
        });
        assertTrue(exception.getMessage().contains("Quantity cannot be negative"));
    }

    @Test
    void createMaterial_WithZeroPrice_ShouldThrowException() {
        // Arrange
        testMaterial.setPricePerUnit(BigDecimal.ZERO);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            materialService.createMaterial(testMaterial);
        });
        assertTrue(exception.getMessage().contains("Price per unit must be greater than zero"));
    }

    @Test
    void updateMaterial_WhenExists_ShouldUpdateMaterial() {
        // Arrange
        Material updatedMaterial = new Material(
            1L,
            "Updated Flour",
            "kg",
            new BigDecimal("55.00"),
            new BigDecimal("150"),
            LocalDate.now()
        );
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(materialRepository.save(any(Material.class))).thenReturn(updatedMaterial);

        // Act
        Material result = materialService.updateMaterial(1L, updatedMaterial);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Flour", result.getName());
        assertEquals(new BigDecimal("55.00"), result.getPricePerUnit());
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void deleteMaterial_WhenExists_ShouldDeleteMaterial() {
        // Arrange
        when(materialRepository.existsById(1L)).thenReturn(true);
        doNothing().when(materialRepository).deleteById(1L);

        // Act
        materialService.deleteMaterial(1L);

        // Assert
        verify(materialRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMaterial_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(materialRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            materialService.deleteMaterial(999L);
        });
        assertTrue(exception.getMessage().contains("Material not found"));
    }

    @Test
    void deductStock_WithSufficientQuantity_ShouldDeductSuccessfully() {
        // Arrange
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);

        // Act
        materialService.deductStock(1L, new BigDecimal("50"));

        // Assert
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void deductStock_WithInsufficientQuantity_ShouldThrowException() {
        // Arrange
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            materialService.deductStock(1L, new BigDecimal("150"));
        });
        assertTrue(exception.getMessage().contains("Insufficient stock"));
    }

    @Test
    void getLowStockMaterials_ShouldReturnMaterialsBelowThreshold() {
        // Arrange
        Material lowStockMaterial = new Material(
            2L,
            "Sugar",
            "kg",
            new BigDecimal("60.00"),
            new BigDecimal("5"),
            LocalDate.now()
        );
        List<Material> lowStockMaterials = Arrays.asList(lowStockMaterial);
        when(materialRepository.findByQuantityLessThan(new BigDecimal("10")))
            .thenReturn(lowStockMaterials);

        // Act
        List<Material> result = materialService.getLowStockMaterials(new BigDecimal("10"));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getQuantity().compareTo(new BigDecimal("10")) < 0);
    }
}
