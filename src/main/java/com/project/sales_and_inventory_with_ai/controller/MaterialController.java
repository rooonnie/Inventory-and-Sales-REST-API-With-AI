package com.project.sales_and_inventory_with_ai.controller;

import com.project.sales_and_inventory_with_ai.dto.DTOMapper;
import com.project.sales_and_inventory_with_ai.dto.MaterialDTO;
import com.project.sales_and_inventory_with_ai.entity.Material;
import com.project.sales_and_inventory_with_ai.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;
    private final DTOMapper dtoMapper;

    @GetMapping
    public ResponseEntity<List<MaterialDTO>> getAllMaterials(@RequestParam(required = false) String search) {
        List<Material> materials;
        
        if (search != null && !search.isEmpty()) {
            materials = materialService.searchMaterialsByName(search);
        } else {
            materials = materialService.getAllMaterials();
        }
        
        List<MaterialDTO> materialDTOs = materials.stream()
                .map(dtoMapper::toMaterialDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(materialDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialDTO> getMaterialById(@PathVariable Long id) {
        Material material = materialService.getMaterialById(id);
        return ResponseEntity.ok(dtoMapper.toMaterialDTO(material));
    }

    @PostMapping
    public ResponseEntity<MaterialDTO> createMaterial(@Valid @RequestBody MaterialDTO materialDTO) {
        Material material = dtoMapper.toMaterialEntity(materialDTO);
        Material savedMaterial = materialService.createMaterial(material);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toMaterialDTO(savedMaterial));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialDTO> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody MaterialDTO materialDTO) {
        Material material = dtoMapper.toMaterialEntity(materialDTO);
        Material updatedMaterial = materialService.updateMaterial(id, material);
        return ResponseEntity.ok(dtoMapper.toMaterialDTO(updatedMaterial));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
