package com.project.sales_and_inventory_with_ai.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OllamaResponse {
    private String model;
    private String response;
    private boolean done;
}
