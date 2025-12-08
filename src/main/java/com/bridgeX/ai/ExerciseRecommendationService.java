package com.bridgeX.ai;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExerciseRecommendationService {

    private final CsvService csvService;

    public ExerciseRecommendationService(CsvService csvService) {
        this.csvService = csvService;
    }

    public String getRecommendation(String gender, double height, double weight, int age) {
        String ageGroup = getAgeGroup(age);
        String question = String.format("키 %.1fcm, 몸무게 %.1fkg인 %d세(%s) %s의 추천 운동을 알려줘.", 
                height, weight, age, ageGroup, gender);

        String averageStats = getAverageStats(gender, ageGroup);
        String similarExercises = getSimilarExercises(gender, ageGroup, height, weight);

        String prompt = String.format("""
                사용자 정보: %s
                
                %s
            
                다음은 내 CSV 데이터에서 찾은, 사용자의 요청과 유사한 신체 조건을 가진 사람들의 실제 운동 추천 내역입니다.
                
                ---[참고 데이터: 유사 사례 운동 추천]----
                %s
                
                위 정보를 바탕으로 사용자에게 적합한 운동을 추천해주세요.
                특히, 위에서 제공된 '평균 운동 수행 능력'을 언급하며 사용자가 목표로 삼을 수 있는 수치를 제시해주세요.
                만약 유사한 운동 데이터가 없다면, 일반적인 운동을 추천하되, 사용자 정보에 맞춰 조언해주세요.
                """, question, averageStats, similarExercises);

        return "AI-based recommendation is currently disabled.";
    }

    private String getAgeGroup(int age) {
        if (age < 19) return "청소년";
        if (age < 65) return "성인";
        return "어르신";
    }

    private String getAverageStats(String gender, String ageGroup) {
        try {
            List<Map<String, String>> data = csvService.readCsv();
            
            // Filter by age group and gender
            List<Map<String, String>> groupData = data.stream()
                .filter(row -> ageGroup.equals(row.get("AGRDE_FLAG_NM")) && gender.equals(row.get("SEXDSTN_FLAG_CD")))
                .collect(Collectors.toList());

            if (groupData.isEmpty()) {
                return "해당 연령대 및 성별의 평균 운동 데이터를 찾을 수 없습니다.";
            }

            double avgGrip = groupData.stream().mapToDouble(r -> parseDouble(r.get("MESURE_IEM_007_VALUE"))).average().orElse(0.0);
            double avgFlex = groupData.stream().mapToDouble(r -> parseDouble(r.get("MESURE_IEM_012_VALUE"))).average().orElse(0.0);
            double avgSitup = groupData.stream().mapToDouble(r -> parseDouble(r.get("MESURE_IEM_019_VALUE"))).average().orElse(0.0);
            double avgJump = groupData.stream().mapToDouble(r -> parseDouble(r.get("MESURE_IEM_022_VALUE"))).average().orElse(0.0);

            return String.format("""
                [동일 연령대(%s) 및 성별(%s) 평균 운동 수행 능력]
                - 악력: %.1f kg
                - 앉아윗몸앞으로굽히기 (유연성): %.1f cm
                - 교차윗몸일으키기 (근지구력): %.1f 회
                - 제자리멀리뛰기 (순발력): %.1f cm
                """, ageGroup, gender, avgGrip, avgFlex, avgSitup, avgJump);

        } catch (IOException e) {
            return "데이터 로드 중 오류 발생: " + e.getMessage();
        }
    }

    private String getSimilarExercises(String gender, String ageGroup, double height, double weight) {
        try {
            List<Map<String, String>> data = csvService.readCsv();
            double hMin = height - 5;
            double hMax = height + 5;
            double wMin = weight - 5;
            double wMax = weight + 5;

            List<String> exercises = data.stream()
                .filter(row -> ageGroup.equals(row.get("AGRDE_FLAG_NM")) && gender.equals(row.get("SEXDSTN_FLAG_CD")))
                .filter(row -> {
                    double h = parseDouble(row.get("MESURE_IEM_001_VALUE"));
                    double w = parseDouble(row.get("MESURE_IEM_002_VALUE"));
                    return h >= hMin && h <= hMax && w >= wMin && w <= wMax;
                })
                .map(row -> row.get("MVM_PRSCRPTN_CN"))
                .distinct()
                .collect(Collectors.toList());

            if (exercises.isEmpty()) {
                return "유사한 사용자를 CSV에서 찾지 못했습니다.";
            }

            return String.join("\n\n", exercises);

        } catch (IOException e) {
            return "데이터 로드 중 오류 발생: " + e.getMessage();
        }
    }

    private double parseDouble(String value) {
        if (value == null || value.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
