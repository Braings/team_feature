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

        // [중요] 1. 프론트엔드에서 온 잡다한 데이터를 거르고, 딱 필요한 것만 추출 (Payload Sanitization)
        Map<String, Object> cleanData = new HashMap<>();
        cleanData.put("gender", requestData.get("gender"));
        cleanData.put("height", requestData.get("height"));
        cleanData.put("weight", requestData.get("weight"));
        cleanData.put("age", requestData.get("age"));

        // [디버깅] 실제로 Flask에 뭘 보내는지 로그로 확인해보세요.
        System.out.println(">>> Flask로 보내는 정제된 데이터: " + cleanData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // [중요] 2. requestData 대신 cleanData를 넣어서 보냄
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(cleanData, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskApiUrl, entity, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                result.put("recommendation", (String) response.getBody().get("recommendation"));
            } else {
                result.put("error", "Flask API 응답 실패");
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            result.put("error", "Error: " + e.getMessage());
        }

        return result;
    }
}
