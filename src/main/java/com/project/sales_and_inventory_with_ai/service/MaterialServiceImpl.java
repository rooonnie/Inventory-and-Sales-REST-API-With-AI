package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.Material;
import com.project.sales_and_inventory_with_ai.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found with id: " + id));
    }

    @Override
    public Material createMaterial(Material material) {
        if (material.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }
        if (material.getPricePerUnit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Price per unit must be greater than zero");
        }
        return materialRepository.save(material);
    }

    @Override
    public Material updateMaterial(Long id, Material material) {
        Material existingMaterial = getMaterialById(id);
        
        existingMaterial.setName(material.getName());
        existingMaterial.setUnit(material.getUnit());
        existingMaterial.setPricePerUnit(material.getPricePerUnit());
        existingMaterial.setQuantity(material.getQuantity());
        existingMaterial.setDatePurchased(material.getDatePurchased());
        
        return materialRepository.save(existingMaterial);
    }

    @Override
    public void deleteMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new RuntimeException("Material not found with id: " + id);
        }
        materialRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> searchMaterialsByName(String name) {
        return materialRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getLowStockMaterials(BigDecimal threshold) {
        return materialRepository.findByQuantityLessThan(threshold);
    }

    @Override
    public void deductStock(Long materialId, BigDecimal quantity) {
        Material material = getMaterialById(materialId);
        
        BigDecimal newQuantity = material.getQuantity().subtract(quantity);
        if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient stock for material: " + material.getName() + 
                                     ". Available: " + material.getQuantity() + 
                                     ", Required: " + quantity);
        }
        
        material.setQuantity(newQuantity);
        materialRepository.save(material);
    }
}
