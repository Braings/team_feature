package com.bridgeX;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ExerciseController {

    @GetMapping("/exercise")
    public String exerciseForm() {
        return "exercise_form";
    }

    @PostMapping("/exercise")
    public String getRecommendation(
            @RequestParam("gender") String gender,
            @RequestParam("height") double height,
            @RequestParam("weight") double weight,
            @RequestParam("age") int age,
            Model model) {

        String flaskApiUrl = "http://127.0.0.1:5000/recommend";

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("gender", gender);
        requestBody.put("height", height);
        requestBody.put("weight", weight);
        requestBody.put("age", age);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Call Flask API
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskApiUrl, entity, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String recommendation = (String) response.getBody().get("recommendation");
                model.addAttribute("recommendation", recommendation);
            } else {
                model.addAttribute("error", "Failed to get recommendation from AI.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error calling AI API: " + e.getMessage());
        }

        // Add input values back to model to keep them in the form
        model.addAttribute("gender", gender);
        model.addAttribute("height", height);
        model.addAttribute("weight", weight);
        model.addAttribute("age", age);

        return "exercise_form";
    }
}
