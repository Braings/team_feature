<template>
  <div class="signup-page">
    <div class="signup-card">
      <h1 class="title">BRIDGE-X</h1>

      <form class="input-area" @submit.prevent="handleNext">
        <FormField
          v-model="formData.id"
          label="ID"
          type="text"
          placeholder="ID"
          :error="errors.id"
          @blur="validateField('id')"
        />

        <FormField
          v-model="formData.password"
          label="PASSWORD"
          type="password"
          placeholder="PASSWORD"
          :error="errors.password"
          @input="onPasswordInput"
          @blur="validateField('password')"
        />

        <FormField
          v-model="formData.passwordCheck"
          label="PASSWORD Check"
          type="password"
          placeholder="PASSWORD Check"
          :error="errors.passwordCheck"
          @input="validateField('passwordCheck')"
          @blur="validateField('passwordCheck')"
        />

        <button type="submit" class="next-button">NEXT</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import router from '@/router';
import FormField from '@/components/FormField.vue';
import { useFormValidation } from '@/composables/useFormValidation';

// ========================
// Data
// ========================
const formData = reactive({
  id: '',
  password: '',
  passwordCheck: ''
});

// ========================
// Validation Rules
// ========================
const VALIDATION_RULES = {
  id: {
    minLength: 4,
    maxLength: 20,
    pattern: /^[a-zA-Z0-9_]+$/,
    messages: {
      empty: 'ID를 입력하세요.',
      minLength: '최소 4자 이상이어야 합니다.',
      maxLength: '최대 20자 이하여야 합니다.',
      pattern: '영문, 숫자, 언더스코어(_)만 사용 가능합니다.'
    }
  },
  password: {
    minLength: 8,
    maxLength: 20,
    pattern: /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])/,
    messages: {
      empty: '비밀번호를 입력하세요.',
      minLength: '최소 8자 이상이어야 합니다.',
      maxLength: '최대 20자 이하여야 합니다.',
      pattern: '영문, 숫자, 특수문자(!@#$%^&*)를 모두 포함해야 합니다.'
    }
  },
  passwordCheck: {
    validate: (value) => value === formData.password,
    messages: {
      empty: '비밀번호 확인을 입력하세요.',
      custom: '비밀번호가 일치하지 않습니다.'
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
const onPasswordInput = () => {
  if (formData.passwordCheck) {
    validateField('passwordCheck', formData.passwordCheck);
  }
};

const handleNext = async () => {
  if (!validateForm(formData)) {
    console.log('❌ 폼 검증 실패');
    return;
  }

  console.log('✓ 폼 검증 성공');
  console.log('ID:', formData.id);

  // TODO: API 호출 및 에러 처리
  router.push({ name: 'sign.private' });
};
</script>

<style lang="scss" scoped>
// ========================
// Variables
// ========================
$colors: (
  'dark': #121212,
  'white': #fff,
  'border': #ccc,
  'text-placeholder': #888,
  'error': #e74c3c,
  'black': #000,
  'gray-hover': #333
);

$spacing: (
  'xl': 20px,
  '2xl': 40px,
  '3xl': 50px
);

$radius: (
  'md': 5px,
  'xl': 20px
);

$sizes: (
  'button-width': 120px,
  'button-height': 60px,
  'card-width': 400px
);

$typography: (
  'title': 3rem,
  'button': 1.5rem
);

$shadows: (
  'card': 0 4px 15px rgba(0, 0, 0, 0.2)
);

// ========================
// Mixins
// ========================
@mixin flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

@mixin card {
  background-color: map-get($colors, 'white');
  border-radius: map-get($radius, 'xl');
  box-shadow: map-get($shadows, 'card');
}

// ========================
// Styles
// ========================
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
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
}

.next-button {
  @include flex-center;
  background-color: map-get($colors, 'black');
  color: map-get($colors, 'white');
  padding: 15px 30px;
  border: none;
  border-radius: map-get($radius, 'md');
  font-size: map-get($typography, 'button');
  font-weight: bold;
  cursor: pointer;
  margin-top: map-get($spacing, 'xl');
  align-self: flex-end;
  width: map-get($sizes, 'button-width');
  height: map-get($sizes, 'button-height');
  transition: background-color 0.2s ease;

  &:hover {
    background-color: map-get($colors, 'gray-hover');
  }

  &:focus {
    outline: none;
  }
}
</style>
