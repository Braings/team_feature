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
          @input="validateField('height')"
          @blur="validateField('height')"
        />

        <FormField
          v-model="formData.weight"
          type="number"
          placeholder="WEIGHT (kg)"
          :error="errors.weight"
          @input="validateField('weight')"
          @blur="validateField('weight')"
        />

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
});

const isLoading = ref(false);
const errorMessage = ref('');

// ========================
// Validation Rules (수정됨)
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
      empty: '키(cm)를 입력해주세요.',
      pattern: '숫자(정수)만 입력 가능합니다.',
      custom: '정확한 키를 입력해주세요 (120 ~ 250cm)' // 메시지 수정
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
      empty: '몸무게(kg)를 입력해주세요.',
      pattern: '숫자(정수)만 입력 가능합니다.',
      custom: '정확한 몸무게를 입력해주세요 (30 ~ 200kg)'
    }
  },
};

// ========================
// Form Validation
// ========================
const { errors, validateField, validateForm } = useFormValidation(VALIDATION_RULES, formData);

// ========================
// Event Handlers
// ========================
const handleNext = async () => {
  if (!validateForm(formData)) {
    console.log('❌ 폼 검증 실패');
    return;
  }

  console.log('✓ 폼 검증 성공');

  // 전역 store에 건강정보 데이터 저장
  signupFormData.height = formData.height;
  signupFormData.weight = formData.weight;

  try {
    isLoading.value = true;
    errorMessage.value = '';

    // API로 회원가입 정보 전송
    const response = await submitSignup();
    console.log('✓ 회원가입 성공:', response);

    // 회원가입 완료 후 홈페이지로 이동
    signupFormData.reset(); // 폼 데이터 초기화

    // [중요] 성공 시 홈으로 이동
    router.push({ name: 'homePage' });

  } catch (error) {
    console.error('❌ 회원가입 실패:', error);
    errorMessage.value = error.message || '회원가입 중 오류가 발생했습니다.';
  } finally {
    isLoading.value = false;
  }
  // [삭제됨] 여기에 있던 router.push({ name: 'logIn' })은 버그를 유발하므로 삭제했습니다.
};
</script>

<style lang="scss" scoped>
@use 'sass:color';
@use 'sass:map';
@use '@/styles/_variables.scss' as *;

.signup-page {
  @include flex-center;
  min-height: 100vh;
  background-color: map.get($colors, 'dark');
  background-image: url('@/img/bgGym.png');
  background-size: cover;
  background-position: center;

  .signup-card {
    @include card;
    padding: map.get($spacing, 'xl') map.get($spacing, '2xl');
    width: map.get($sizes, 'card-width');
    min-width: 400px;

    display: flex;
    flex-direction: column;
    align-items: center;

    .input-area {
      width: 100%;
      display: flex;
      flex-direction: column;
      gap: map.get($spacing, 'sm');
    }

    .title {
      position: relative;
      font-size: map.get($typography, 'title');
      font-weight: bold;
      letter-spacing: 5px;
      text-align: center;
      width: 100%;
      margin-top: 0;
      margin-bottom: map.get($spacing, 'lg');
    }

    .next-button {
      @include button-base;
      @include flex-center;
      background-color: map.get($colors, 'black');
      color: map.get($colors, 'white');

      padding: 12px 0;
      border-radius: map.get($radius, 'md');
      font-size: map.get($typography, 'button');
      margin-top: map.get($spacing, 'md');

      align-self: flex-end;
      width: map.get($sizes, 'button-width');
      height: map.get($sizes, 'button-height');

      cursor: pointer;
      border: none;

      &:hover {
        background-color: map.get($colors, 'gray-hover');
      }

      &:disabled {
        background-color: #ccc;
        cursor: not-allowed;
      }
    }
  }
}

.error-message {
  color: #ff4d4d;
  font-size: 0.9rem;
  margin-bottom: 10px;
  text-align: center;
}
</style>
