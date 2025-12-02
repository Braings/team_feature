<template>
  <div class="exercise-form-container">
    <h2>맞춤형 운동 추천</h2>
    <form @submit.prevent="getRecommendation" class="recommendation-form">

      <div class="form-group">
        <label for="gender">성별:</label>
        <select id="gender" v-model="formData.gender" required>
          <option value="M">남성 (M)</option>
          <option value="F">여성 (F)</option>
        </select>
      </div>

      <div class="form-group">
        <label for="height">키 (cm):</label>
        <input type="number" id="height" v-model.number="formData.height" min="100" max="250" required>
      </div>

      <div class="form-group">
        <label for="weight">몸무게 (kg):</label>
        <input type="number" id="weight" v-model.number="formData.weight" min="30" max="300" required>
      </div>

      <div class="form-group">
        <label for="age">나이:</label>
        <input type="number" id="age" v-model.number="formData.age" min="1" max="120" required>
      </div>

      <button type="submit" :disabled="isLoading">
        {{ isLoading ? '추천 중...' : '운동 추천 받기' }}
      </button>

      <p v-if="error" class="error-message">오류: {{ error }}</p>
    </form>

    <div v-if="recommendation" class="recommendation-result">
      <h3>추천 결과:</h3>
      <p>{{ recommendation }}</p>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  name: 'ExerciseForm',
  setup() {
    // 1. 상태 정의 (Spring Controller의 Mock Data 기반으로 초기값 설정)
    const formData = ref({
      gender: 'M',
      height: 175.0,
      weight: 70.0,
      age: 25
    });

    const recommendation = ref('');
    const isLoading = ref(false);
    const error = ref(null);

    // Spring Controller의 엔드포인트와 동일
    const API_ENDPOINT = '/api/exercise/recommend';

    // 2. 추천 로직 (API 호출)
    const getRecommendation = async () => {
      isLoading.value = true;
      error.value = null;
      recommendation.value = ''; // 기존 결과 초기화

      try {
        // fetch API를 사용하여 Spring Boot 백엔드로 POST 요청을 보냅니다.
        const response = await fetch(API_ENDPOINT, {
          method: 'POST',
          headers: {
            // Spring Controller가 @RequestBody를 사용하므로, JSON 형식으로 보냅니다.
            'Content-Type': 'application/json',
          },
          // 폼 데이터를 JSON 문자열로 변환하여 전송합니다.
          body: JSON.stringify(formData.value),
        });

        if (!response.ok) {
          throw new Error(`HTTP Error! Status: ${response.status}. 서버에서 에러가 발생했습니다.`);
        }

        const data = await response.json(); // 서버 응답 (JSON) 파싱

        // Spring Controller가 반환하는 Map<String, String> 응답 구조에 맞춰 처리
        if (data.recommendation) {
          recommendation.value = data.recommendation;
        } else if (data.error) {
          error.value = data.error;
        } else {
          error.value = "서버로부터 예상치 못한 응답을 받았습니다.";
        }

      } catch (err) {
        console.error("API 호출 오류:", err);
        error.value = `API 호출 중 오류가 발생했습니다: ${err.message}`;
      } finally {
        isLoading.value = false;
      }
    };

    return {
      formData,
      recommendation,
      isLoading,
      error,
      getRecommendation,
    };
  },
};
</script>

<style scoped>
/* 기본적인 스타일링 */
.exercise-form-container {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.recommendation-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group {
  display: flex;
  align-items: center;
}

.form-group label {
  flex: 1;
  font-weight: bold;
  margin-right: 15px;
}

.form-group input, .form-group select {
  flex: 2;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  padding: 12px 20px;
  background-color: #42b983; /* Vue Green */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
}

button:disabled {
  background-color: #a0a0a0;
  cursor: not-allowed;
}

.error-message {
  color: red;
  font-weight: bold;
  margin-top: 10px;
}

.recommendation-result {
  margin-top: 30px;
  padding: 15px;
  border: 1px solid #42b983;
  border-radius: 4px;
  background-color: #e6fff0;
}
</style>
