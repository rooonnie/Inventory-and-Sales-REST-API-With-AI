package com.project.sales_and_inventory_with_ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "food_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "price_per_serving", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerServing;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>();

    // Helper method to add ingredient
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setFoodItem(this);
    }

    // Helper method to remove ingredient
    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
        ingredient.setFoodItem(null);
    }
}
