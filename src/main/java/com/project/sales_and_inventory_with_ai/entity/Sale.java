package com.project.sales_and_inventory_with_ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    @Column(name = "quantity_sold", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantitySold;

    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal profit;

    @Column(name = "cost_of_ingredients", precision = 10, scale = 2)
    private BigDecimal costOfIngredients;
}
