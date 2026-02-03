package com.project.sales_and_inventory_with_ai.controller;

import com.project.sales_and_inventory_with_ai.dto.DTOMapper;
import com.project.sales_and_inventory_with_ai.dto.FoodItemDTO;
import com.project.sales_and_inventory_with_ai.entity.FoodItem;
import com.project.sales_and_inventory_with_ai.service.FoodItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/food-items")
@RequiredArgsConstructor
public class FoodItemController {

    private final FoodItemService foodItemService;
    private final DTOMapper dtoMapper;

    @GetMapping
    public ResponseEntity<List<FoodItemDTO>> getAllFoodItems(@RequestParam(required = false) String search) {
        List<FoodItem> foodItems;
        
        if (search != null && !search.isEmpty()) {
            foodItems = foodItemService.searchFoodItemsByName(search);
        } else {
            foodItems = foodItemService.getAllFoodItems();
        }
        
        List<FoodItemDTO> foodItemDTOs = foodItems.stream()
                .map(dtoMapper::toFoodItemDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(foodItemDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDTO> getFoodItemById(@PathVariable Long id) {
        FoodItem foodItem = foodItemService.getFoodItemById(id);
        return ResponseEntity.ok(dtoMapper.toFoodItemDTO(foodItem));
    }

    @PostMapping
    public ResponseEntity<FoodItemDTO> createFoodItem(@Valid @RequestBody FoodItemDTO foodItemDTO) {
        FoodItem foodItem = dtoMapper.toFoodItemEntity(foodItemDTO);
        FoodItem savedFoodItem = foodItemService.createFoodItem(foodItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toFoodItemDTO(savedFoodItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItemDTO> updateFoodItem(
            @PathVariable Long id,
            @Valid @RequestBody FoodItemDTO foodItemDTO) {
        FoodItem foodItem = dtoMapper.toFoodItemEntity(foodItemDTO);
        FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItem);
        return ResponseEntity.ok(dtoMapper.toFoodItemDTO(updatedFoodItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        foodItemService.deleteFoodItem(id);
        return ResponseEntity.noContent().build();
    }
}
