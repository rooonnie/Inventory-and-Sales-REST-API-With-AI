package com.project.sales_and_inventory_with_ai.service;

import com.project.sales_and_inventory_with_ai.entity.Material;

import java.math.BigDecimal;
import java.util.List;

public interface MaterialService {
    
    List<Material> getAllMaterials();
    
    Material getMaterialById(Long id);
    
    Material createMaterial(Material material);
    
    Material updateMaterial(Long id, Material material);
    
    void deleteMaterial(Long id);
    
    List<Material> searchMaterialsByName(String name);
    
    List<Material> getLowStockMaterials(BigDecimal threshold);
    
    void deductStock(Long materialId, BigDecimal quantity);
}
