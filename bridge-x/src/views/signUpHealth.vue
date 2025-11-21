<template>
  <div class="signup-page">
    <div class="signup-card">
      <h1 class="title">BRIDGE-X</h1>

      <form class="input-area" @submit.prevent="handleNext">
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>

        <FormField
          v-model="formData.height"
          type="number"
          placeholder="HEIGHT (cm)"
          :error="errors.height"
          @blur="validateField('height')"
        />

        <FormField
          v-model="formData.weight"
          type="number"
          placeholder="WEIGHT (kg)"
          :error="errors.weight"
          @blur="validateField('weight')"
        />

        <div class="form-group">
          <label class="label">Experience Level</label>
          <select
            v-model="formData.experienceLevel"
            class="select-field"
            @blur="validateField('experienceLevel')"
          >
            <option value="">Select Level</option>
            <option value="beginner">Beginner</option>
            <option value="intermediate">Intermediate</option>
            <option value="advanced">Advanced</option>
          </select>
          <div v-if="errors.experienceLevel" class="error-text">{{ errors.experienceLevel }}</div>
        </div>

        <button type="submit" class="next-button" :disabled="isLoading">
          {{ isLoading ? 'LOADING...' : 'COMPLETE' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import router from '@/router';
import FormField from '@/components/FormField.vue';
import { useFormValidation } from '@/composables/useFormValidation';
import { signupFormData, submitSignup } from '@/stores/signupStore';

// ========================
// Data
// ========================
const formData = reactive({
  height: '',
  weight: '',
  experienceLevel: ''
});

const isLoading = ref(false);
const errorMessage = ref('');

// ========================
// Validation Rules
// ========================
const VALIDATION_RULES = {
  height: {
    minLength: 2,
    maxLength: 3,
    pattern: /^\d{2,3}$/,
    validate: (value) => {
      const num = Number(value);
      return num >= 120 && num <= 250;
    },
    messages: {
      empty: '키를 입력하세요.',
      pattern: '숫자만 입력 가능합니다.',
      custom: '키는 120~250cm 범위여야 합니다.'
    }
  },
  weight: {
    minLength: 2,
    maxLength: 3,
    pattern: /^\d{2,3}$/,
    validate: (value) => {
      const num = Number(value);
      return num >= 30 && num <= 200;
    },
    messages: {
      empty: '몸무게를 입력하세요.',
      pattern: '숫자만 입력 가능합니다.',
      custom: '몸무게는 30~200kg 범위여야 합니다.'
    }
  },
  experienceLevel: {
    messages: {
      empty: '운동 경험 레벨을 선택하세요.'
    }
  }
};

// ========================
// Form Validation
// ========================
const { errors, validateField, validateForm } = useFormValidation(VALIDATION_RULES);

// ========================
// Event Handlers
// ========================
const handleNext = async () => {
  if (!validateForm(formData)) {
    console.log('❌ 폼 검증 실패');
    return;
  }

  console.log('✓ 폼 검증 성공');
  console.log('Health Info:', formData);

  // 전역 store에 건강정보 데이터 저장
  signupFormData.height = formData.height;
  signupFormData.weight = formData.weight;
  signupFormData.experienceLevel = formData.experienceLevel;

  try {
    isLoading.value = true;
    errorMessage.value = '';

    // API로 회원가입 정보 전송
    const response = await submitSignup();
    console.log('✓ 회원가입 성공:', response);

    // 회원가입 완료 후 홈페이지로 이동
    signupFormData.reset(); // 폼 데이터 초기화
    router.push({ name: 'homePage' });
  } catch (error) {
    console.error('❌ 회원가입 실패:', error);
    errorMessage.value = error.message || '회원가입 중 오류가 발생했습니다.';
  } finally {
    isLoading.value = false;
  }
};
</script>

<style lang="scss" scoped>
@import '@/styles/_variables.scss';

.signup-page {
  @include flex-center;
  min-height: 100vh;
  background-color: map-get($colors, 'dark');
}

.signup-card {
  @include card;
  padding: map-get($spacing, '2xl') map-get($spacing, '3xl');
  width: map-get($sizes, 'card-width');
  display: flex;
  flex-direction: column;
  align-items: center;
}

.title {
  font-size: map-get($typography, 'title');
  font-weight: bold;
  margin-bottom: map-get($spacing, '2xl');
  letter-spacing: 5px;
}

.input-area {
  @include flex-column;
  width: 100%;
}

.form-group {
  width: 100%;
  margin-bottom: map-get($spacing, 'xl');
}

.label {
  display: block;
  margin-bottom: map-get($spacing, 'sm');
  font-size: map-get($typography, 'base');
  font-weight: 500;
  color: map-get($colors, 'black');
}

.select-field {
  width: 100%;
  padding: map-get($spacing, 'lg') map-get($spacing, 'md');
  border: 1px solid map-get($colors, 'border');
  border-radius: map-get($radius, 'md');
  font-size: map-get($typography, 'base');
  background-color: map-get($colors, 'white');
  cursor: pointer;

  &:focus {
    outline: none;
    border-color: darken(map-get($colors, 'border'), 10%);
  }
}

.error-text {
  @include error-text;
}

.error-message {
  width: 100%;
  padding: 12px;
  margin-bottom: map-get($spacing, 'xl');
  background-color: #ffebee;
  border-left: 4px solid #e74c3c;
  border-radius: 4px;
  color: #c0392b;
  font-size: 14px;
  font-weight: 500;
}

.next-button {
  @include button-base;
  @include flex-center;
  background-color: map-get($colors, 'black');
  color: map-get($colors, 'white');
  padding: 15px 30px;
  border-radius: map-get($radius, 'md');
  font-size: map-get($typography, 'button');
  margin-top: map-get($spacing, 'xl');
  align-self: flex-end;
  width: map-get($sizes, 'button-width');
  height: map-get($sizes, 'button-height');
  transition: all 0.2s ease;

  &:hover:not(:disabled) {
    background-color: map-get($colors, 'gray-hover');
  }

  &:disabled {
    background-color: #999;
    cursor: not-allowed;
    opacity: 0.7;
  }
}
</style>
