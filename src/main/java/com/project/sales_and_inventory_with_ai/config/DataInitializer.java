package com.project.sales_and_inventory_with_ai.config;

import com.project.sales_and_inventory_with_ai.entity.FoodItem;
import com.project.sales_and_inventory_with_ai.entity.Ingredient;
import com.project.sales_and_inventory_with_ai.entity.Material;
import com.project.sales_and_inventory_with_ai.entity.Sale;
import com.project.sales_and_inventory_with_ai.repository.FoodItemRepository;
import com.project.sales_and_inventory_with_ai.repository.MaterialRepository;
import com.project.sales_and_inventory_with_ai.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final MaterialRepository materialRepository;
    private final FoodItemRepository foodItemRepository;
    private final SaleRepository saleRepository;

    @Override
    public void run(String... args) {
        if (materialRepository.count() == 0) {
            log.info("Initializing sample data...");
            initializeSampleData();
            log.info("Sample data initialized successfully!");
        } else {
            log.info("Database already contains data. Skipping initialization.");
        }
    }

    private void initializeSampleData() {
        // Create Materials
        Material flour = createMaterial("Flour", "kg", new BigDecimal("50.00"), new BigDecimal("100"), LocalDate.now().minusDays(5));
        Material sugar = createMaterial("Sugar", "kg", new BigDecimal("60.00"), new BigDecimal("50"), LocalDate.now().minusDays(3));
        Material eggs = createMaterial("Eggs", "pieces", new BigDecimal("8.00"), new BigDecimal("200"), LocalDate.now().minusDays(2));
        Material milk = createMaterial("Milk", "liters", new BigDecimal("80.00"), new BigDecimal("30"), LocalDate.now().minusDays(1));
        Material butter = createMaterial("Butter", "kg", new BigDecimal("300.00"), new BigDecimal("20"), LocalDate.now().minusDays(4));
        Material chocolate = createMaterial("Chocolate", "kg", new BigDecimal("400.00"), new BigDecimal("15"), LocalDate.now().minusDays(6));
        Material vanilla = createMaterial("Vanilla Extract", "ml", new BigDecimal("5.00"), new BigDecimal("500"), LocalDate.now().minusDays(7));
        Material cheese = createMaterial("Cheese", "kg", new BigDecimal("350.00"), new BigDecimal("25"), LocalDate.now().minusDays(2));
        Material tomato = createMaterial("Tomato Sauce", "kg", new BigDecimal("120.00"), new BigDecimal("40"), LocalDate.now().minusDays(3));
        Material coffee = createMaterial("Coffee Beans", "kg", new BigDecimal("600.00"), new BigDecimal("10"), LocalDate.now().minusDays(8));

        log.info("Created {} materials", 10);

        // Create Food Items with Ingredients
        FoodItem chocolateCake = new FoodItem();
        chocolateCake.setName("Chocolate Cake");
        chocolateCake.setPricePerServing(new BigDecimal("150.00"));
        chocolateCake.setIngredients(new ArrayList<>());
        
        Ingredient cakeFlour = new Ingredient(null, chocolateCake, flour, new BigDecimal("0.5"));
        Ingredient cakeSugar = new Ingredient(null, chocolateCake, sugar, new BigDecimal("0.3"));
        Ingredient cakeEggs = new Ingredient(null, chocolateCake, eggs, new BigDecimal("4"));
        Ingredient cakeChocolate = new Ingredient(null, chocolateCake, chocolate, new BigDecimal("0.2"));
        Ingredient cakeButter = new Ingredient(null, chocolateCake, butter, new BigDecimal("0.15"));
        
        chocolateCake.getIngredients().add(cakeFlour);
        chocolateCake.getIngredients().add(cakeSugar);
        chocolateCake.getIngredients().add(cakeEggs);
        chocolateCake.getIngredients().add(cakeChocolate);
        chocolateCake.getIngredients().add(cakeButter);
        
        chocolateCake = foodItemRepository.save(chocolateCake);

        FoodItem vanillaCupcake = new FoodItem();
        vanillaCupcake.setName("Vanilla Cupcake");
        vanillaCupcake.setPricePerServing(new BigDecimal("50.00"));
        vanillaCupcake.setIngredients(new ArrayList<>());
        
        Ingredient cupcakeFlour = new Ingredient(null, vanillaCupcake, flour, new BigDecimal("0.1"));
        Ingredient cupcakeSugar = new Ingredient(null, vanillaCupcake, sugar, new BigDecimal("0.05"));
        Ingredient cupcakeEggs = new Ingredient(null, vanillaCupcake, eggs, new BigDecimal("1"));
        Ingredient cupcakeVanilla = new Ingredient(null, vanillaCupcake, vanilla, new BigDecimal("5"));
        Ingredient cupcakeMilk = new Ingredient(null, vanillaCupcake, milk, new BigDecimal("0.05"));
        
        vanillaCupcake.getIngredients().add(cupcakeFlour);
        vanillaCupcake.getIngredients().add(cupcakeSugar);
        vanillaCupcake.getIngredients().add(cupcakeEggs);
        vanillaCupcake.getIngredients().add(cupcakeVanilla);
        vanillaCupcake.getIngredients().add(cupcakeMilk);
        
        vanillaCupcake = foodItemRepository.save(vanillaCupcake);

        FoodItem cheesePizza = new FoodItem();
        cheesePizza.setName("Cheese Pizza");
        cheesePizza.setPricePerServing(new BigDecimal("200.00"));
        cheesePizza.setIngredients(new ArrayList<>());
        
        Ingredient pizzaFlour = new Ingredient(null, cheesePizza, flour, new BigDecimal("0.3"));
        Ingredient pizzaCheese = new Ingredient(null, cheesePizza, cheese, new BigDecimal("0.2"));
        Ingredient pizzaTomato = new Ingredient(null, cheesePizza, tomato, new BigDecimal("0.15"));
        
        cheesePizza.getIngredients().add(pizzaFlour);
        cheesePizza.getIngredients().add(pizzaCheese);
        cheesePizza.getIngredients().add(pizzaTomato);
        
        cheesePizza = foodItemRepository.save(cheesePizza);

        FoodItem cappuccino = new FoodItem();
        cappuccino.setName("Cappuccino");
        cappuccino.setPricePerServing(new BigDecimal("80.00"));
        cappuccino.setIngredients(new ArrayList<>());
        
        Ingredient cappuccinoCoffee = new Ingredient(null, cappuccino, coffee, new BigDecimal("0.02"));
        Ingredient cappuccinoMilk = new Ingredient(null, cappuccino, milk, new BigDecimal("0.15"));
        Ingredient cappuccinoSugar = new Ingredient(null, cappuccino, sugar, new BigDecimal("0.01"));
        
        cappuccino.getIngredients().add(cappuccinoCoffee);
        cappuccino.getIngredients().add(cappuccinoMilk);
        cappuccino.getIngredients().add(cappuccinoSugar);
        
        cappuccino = foodItemRepository.save(cappuccino);

        log.info("Created {} food items with ingredients", 4);

        // Create Sample Sales
        createSale(chocolateCake, new BigDecimal("2"), new BigDecimal("150.00"), LocalDateTime.now().minusDays(2), new BigDecimal("145.00"), new BigDecimal("5.00"));
        createSale(vanillaCupcake, new BigDecimal("10"), new BigDecimal("50.00"), LocalDateTime.now().minusDays(1), new BigDecimal("450.00"), new BigDecimal("50.00"));
        createSale(cheesePizza, new BigDecimal("5"), new BigDecimal("200.00"), LocalDateTime.now().minusDays(1), new BigDecimal("850.00"), new BigDecimal("150.00"));
        createSale(cappuccino, new BigDecimal("15"), new BigDecimal("80.00"), LocalDateTime.now().minusHours(5), new BigDecimal("1170.00"), new BigDecimal("30.00"));
        createSale(chocolateCake, new BigDecimal("3"), new BigDecimal("150.00"), LocalDateTime.now().minusHours(3), new BigDecimal("217.50"), new BigDecimal("232.50"));
        createSale(vanillaCupcake, new BigDecimal("8"), new BigDecimal("50.00"), LocalDateTime.now().minusHours(1), new BigDecimal("360.00"), new BigDecimal("40.00"));

        log.info("Created {} sample sales", 6);
    }

    private Material createMaterial(String name, String unit, BigDecimal pricePerUnit, BigDecimal quantity, LocalDate datePurchased) {
        Material material = new Material();
        material.setName(name);
        material.setUnit(unit);
        material.setPricePerUnit(pricePerUnit);
        material.setQuantity(quantity);
        material.setDatePurchased(datePurchased);
        return materialRepository.save(material);
    }

    private Sale createSale(FoodItem foodItem, BigDecimal quantitySold, BigDecimal salePrice, LocalDateTime saleDate, BigDecimal costOfIngredients, BigDecimal profit) {
        Sale sale = new Sale();
        sale.setFoodItem(foodItem);
        sale.setQuantitySold(quantitySold);
        sale.setSalePrice(salePrice);
        sale.setSaleDate(saleDate);
        sale.setCostOfIngredients(costOfIngredients);
        sale.setProfit(profit);
        return saleRepository.save(sale);
    }
}
