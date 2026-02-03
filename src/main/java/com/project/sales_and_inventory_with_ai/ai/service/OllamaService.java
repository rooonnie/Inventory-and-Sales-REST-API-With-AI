package com.project.sales_and_inventory_with_ai.ai.service;

import com.project.sales_and_inventory_with_ai.ai.dto.OllamaRequest;
import com.project.sales_and_inventory_with_ai.ai.dto.OllamaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class OllamaService {

    private final WebClient webClient;
    private final String model;
    private final int timeout;

    public OllamaService(
            @Value("${ollama.base-url}") String baseUrl,
            @Value("${ollama.model}") String model,
            @Value("${ollama.timeout}") int timeout) {
        
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.model = model;
        this.timeout = timeout;
    }

    public String chat(String prompt) {
        log.debug("Sending request to Ollama with model: {}", model);
        
        OllamaRequest request = new OllamaRequest();
        request.setModel(model);
        request.setPrompt(prompt);
        request.setStream(false);
        request.setOptions(new OllamaRequest.OllamaOptions(0.7, 500));

        try {
            OllamaResponse response = webClient.post()
                    .uri("/api/generate")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(OllamaResponse.class)
                    .timeout(Duration.ofSeconds(timeout))
                    .block();

            if (response != null && response.getResponse() != null) {
                log.debug("Received response from Ollama");
                return response.getResponse().trim();
            }
            
            throw new RuntimeException("No response from Ollama");
            
        } catch (Exception e) {
            log.error("Error calling Ollama API: {}", e.getMessage());
            throw new RuntimeException("Failed to communicate with AI: " + e.getMessage());
        }
    }

    public boolean isAvailable() {
        try {
            webClient.get()
                    .uri("/")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return true;
        } catch (Exception e) {
            log.warn("Ollama is not available: {}", e.getMessage());
            return false;
        }
    }
}
