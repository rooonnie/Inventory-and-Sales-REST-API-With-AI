package com.project.sales_and_inventory_with_ai.controller;

import com.project.sales_and_inventory_with_ai.dto.DTOMapper;
import com.project.sales_and_inventory_with_ai.dto.SaleDTO;
import com.project.sales_and_inventory_with_ai.entity.Sale;
import com.project.sales_and_inventory_with_ai.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;
    private final DTOMapper dtoMapper;

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getSales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Sale> sales;
        
        if (startDate != null && endDate != null) {
            sales = saleService.getSalesByDateRange(startDate, endDate);
        } else {
            sales = saleService.getAllSales();
        }
        
        List<SaleDTO> saleDTOs = sales.stream()
                .map(dtoMapper::toSaleDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(saleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        Sale sale = saleService.getSaleById(id);
        return ResponseEntity.ok(dtoMapper.toSaleDTO(sale));
    }

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody SaleDTO saleDTO) {
        Sale sale = dtoMapper.toSaleEntity(saleDTO);
        Sale savedSale = saleService.createSale(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toSaleDTO(savedSale));
    }
}
