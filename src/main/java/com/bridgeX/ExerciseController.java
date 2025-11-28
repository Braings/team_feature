package com.bridgeX;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public String exerciseForm(Model model) {
        // Mock Data (임의 지정)
        model.addAttribute("gender", "M");
        model.addAttribute("height", 175.0);
        model.addAttribute("weight", 70.0);
        model.addAttribute("age", 25);
        return "exercise_form";
    }

    @PostMapping("/api/exercise/recommend")
    @ResponseBody
    public Map<String, String> getRecommendationJson(@RequestBody Map<String, Object> requestData) {
        String flaskApiUrl = "http://127.0.0.1:5000/recommend";
        Map<String, String> result = new HashMap<>();

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);

        // Call Flask API
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskApiUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String recommendation = (String) response.getBody().get("recommendation");
                result.put("recommendation", recommendation);
            } else {
                result.put("error", "Failed to get recommendation from AI.");
            }
        } catch (Exception e) {
            result.put("error", "Error calling AI API: " + e.getMessage());
        }

        return result;
    }
}
