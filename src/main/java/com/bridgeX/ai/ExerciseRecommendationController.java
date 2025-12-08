package com.bridgeX.ai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommend")
public class ExerciseRecommendationController {

    private final ExerciseRecommendationService recommendationService;

    public ExerciseRecommendationController(ExerciseRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/exercise")
    public String getExerciseRecommendation(
            @RequestParam String gender,
            @RequestParam double height,
            @RequestParam double weight,
            @RequestParam int age) {
        
        return recommendationService.getRecommendation(gender, height, weight, age);
    }
}
