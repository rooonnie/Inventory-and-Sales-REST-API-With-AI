package com.project.sales_and_inventory_with_ai.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OllamaRequest {
    private String model;
    private String prompt;
    private boolean stream = false;
    private OllamaOptions options;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OllamaOptions {
        private Double temperature = 0.7;
        private Integer numPredict = 500;
    }
}
