package com.project.sales_and_inventory_with_ai.ai.controller;

import com.project.sales_and_inventory_with_ai.ai.dto.ChatRequest;
import com.project.sales_and_inventory_with_ai.ai.dto.ChatResponse;
import com.project.sales_and_inventory_with_ai.ai.service.AIService;
import com.project.sales_and_inventory_with_ai.ai.service.OllamaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;
    private final OllamaService ollamaService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            // Validate Ollama is available
            if (!ollamaService.isAvailable()) {
                return ResponseEntity.ok(new ChatResponse(
                    "Sorry, ang AI service ay hindi available ngayon. Siguraduhin na naka-run ang Ollama.",
                    "ERROR",
                    null,
                    false
                ));
            }

            ChatResponse response = aiService.processMessage(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.ok(new ChatResponse(
                "Sorry, may problema sa pag-process ng request. Error: " + e.getMessage(),
                "ERROR",
                null,
                false
            ));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        boolean ollamaAvailable = ollamaService.isAvailable();
        
        status.put("ollamaAvailable", ollamaAvailable);
        status.put("status", ollamaAvailable ? "READY" : "OFFLINE");
        status.put("message", ollamaAvailable 
            ? "AI service is ready!" 
            : "Ollama is not running. Please start Ollama first.");
        
        return ResponseEntity.ok(status);
    }

    @GetMapping("/help")
    public ResponseEntity<Map<String, Object>> getHelp() {
        Map<String, Object> help = new HashMap<>();
        
        help.put("description", "AI-powered inventory and sales assistant (Taglish)");
        help.put("examples", new String[]{
            "bumili ako ng 10 eggs",
            "magkano kinita ko today?",
            "ano ang low stock items?",
            "ano ang best selling items ngayong linggo?",
            "pa-show ng lahat ng materials",
            "nagbenta ako ng 5 chocolate cakes"
        });
        help.put("capabilities", new String[]{
            "Add materials to inventory",
            "View profit reports",
            "Check stock levels",
            "View top-selling items",
            "Record sales"
        });
        
        return ResponseEntity.ok(help);
    }
}
