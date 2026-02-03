package com.project.sales_and_inventory_with_ai.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sales_and_inventory_with_ai.ai.dto.ChatRequest;
import com.project.sales_and_inventory_with_ai.ai.dto.ChatResponse;
import com.project.sales_and_inventory_with_ai.entity.Material;
import com.project.sales_and_inventory_with_ai.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIServiceImpl implements AIService {

    private final OllamaService ollamaService;
    private final MaterialService materialService;
    private final FoodItemService foodItemService;
    private final SaleService saleService;
    private final ReportService reportService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ChatResponse processMessage(ChatRequest request) {
        log.info("Processing message: {}", request.getMessage());
        
        // Generate prompt with system context
        String prompt = generatePrompt(request.getMessage());
        
        // Get AI response
        String aiResponse = ollamaService.chat(prompt);
        
        // Parse AI response and execute actions
        return parseAndExecute(aiResponse, request.getMessage());
    }

    @Override
    public String generatePrompt(String userMessage) {
        StringBuilder prompt = new StringBuilder();
        
        // System role and context
        prompt.append("You are an AI assistant for a Filipino small business inventory and sales management system.\n");
        prompt.append("You can understand Tagalog and English (Taglish).\n\n");
        
        prompt.append("Your capabilities:\n");
        prompt.append("1. ADD_MATERIAL - Add materials to inventory\n");
        prompt.append("2. VIEW_MATERIALS - Show all materials\n");
        prompt.append("3. RECORD_SALE - Record a sale\n");
        prompt.append("4. VIEW_PROFIT - Show profit for a period\n");
        prompt.append("5. VIEW_STOCK - Show stock levels\n");
        prompt.append("6. VIEW_TOP_ITEMS - Show best-selling items\n\n");
        
        prompt.append("Response format (JSON only, no additional text):\n");
        prompt.append("{\n");
        prompt.append("  \"action\": \"ACTION_NAME\",\n");
        prompt.append("  \"parameters\": {...},\n");
        prompt.append("  \"message\": \"Filipino/Taglish response to user\",\n");
        prompt.append("  \"needsMoreInfo\": true/false\n");
        prompt.append("}\n\n");
        
        prompt.append("Examples:\n");
        prompt.append("User: 'bumili ako ng 5 eggs'\n");
        prompt.append("Response: {\"action\":\"ADD_MATERIAL\",\"parameters\":{\"name\":\"Egg\",\"quantity\":5,\"unit\":\"pieces\"},\"message\":\"Magkano po ang presyo per piece?\",\"needsMoreInfo\":true}\n\n");
        
        prompt.append("User: 'magkano kinita ko today?'\n");
        prompt.append("Response: {\"action\":\"VIEW_PROFIT\",\"parameters\":{\"period\":\"today\"},\"message\":\"Tingnan natin ang profit mo ngayong araw...\",\"needsMoreInfo\":false}\n\n");
        
        prompt.append("User message: ").append(userMessage).append("\n");
        prompt.append("Response (JSON only):");
        
        return prompt.toString();
    }

    private ChatResponse parseAndExecute(String aiResponse, String originalMessage) {
        try {
            // Clean the response - remove markdown code blocks if present
            String cleanedResponse = aiResponse
                    .replaceAll("```json\\s*", "")
                    .replaceAll("```\\s*", "")
                    .trim();
            
            // Parse AI response
            @SuppressWarnings("unchecked")
            Map<String, Object> responseMap = objectMapper.readValue(cleanedResponse, Map.class);
            
            String action = (String) responseMap.get("action");
            String message = (String) responseMap.get("message");
            @SuppressWarnings("unchecked")
            Map<String, Object> parameters = (Map<String, Object>) responseMap.getOrDefault("parameters", new HashMap<>());
            boolean needsMoreInfo = (Boolean) responseMap.getOrDefault("needsMoreInfo", false);
            
            if (needsMoreInfo) {
                // AI needs more information, just return the message
                return new ChatResponse(message, action, null, true);
            }
            
            // Execute action
            Object result = executeAction(action, parameters);
            
            return new ChatResponse(message, action, result, false);
            
        } catch (JsonProcessingException e) {
            log.error("Failed to parse AI response: {}", e.getMessage());
            return new ChatResponse(
                "Sorry, hindi ko naintindihan yung request. Pwede mo bang ulitin?",
                "ERROR",
                null,
                false
            );
        }
    }

    private Object executeAction(String action, Map<String, Object> parameters) {
        try {
            switch (action) {
                case "VIEW_MATERIALS":
                    return materialService.getAllMaterials();
                    
                case "VIEW_PROFIT":
                    String period = (String) parameters.getOrDefault("period", "today");
                    return reportService.getSalesSummary(period);
                    
                case "VIEW_STOCK":
                    BigDecimal threshold = new BigDecimal(
                        parameters.getOrDefault("threshold", "10").toString()
                    );
                    return reportService.getMaterialsStockReport(threshold);
                    
                case "VIEW_TOP_ITEMS":
                    String topPeriod = (String) parameters.getOrDefault("period", "today");
                    int limit = Integer.parseInt(parameters.getOrDefault("limit", "5").toString());
                    return reportService.getTopSellingItems(topPeriod, limit);
                    
                case "ADD_MATERIAL":
                    // This would need complete parameters - for now just return info needed
                    return parameters;
                    
                default:
                    return "Action not yet implemented: " + action;
            }
        } catch (Exception e) {
            log.error("Error executing action {}: {}", action, e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
