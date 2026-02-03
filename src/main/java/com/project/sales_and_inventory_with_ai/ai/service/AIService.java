package com.project.sales_and_inventory_with_ai.ai.service;

import com.project.sales_and_inventory_with_ai.ai.dto.ChatRequest;
import com.project.sales_and_inventory_with_ai.ai.dto.ChatResponse;

public interface AIService {
    
    ChatResponse processMessage(ChatRequest request);
    
    String generatePrompt(String userMessage);
}
