package com.project.sales_and_inventory_with_ai.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String message;
    private String action;
    private Object data;
    private boolean needsConfirmation;
}
