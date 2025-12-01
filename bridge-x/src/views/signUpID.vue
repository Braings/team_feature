<template>
  <div class="signup-page">
    <div class="signup-card">
      <h1 class="title">BRIDGE-X</h1>

      <form class="input-area" @submit.prevent="handleNext">
        <FormField
          v-model="formData.username"
          label="username"
          type="text"
          placeholder="ID"
          :error="errors.username"
          @input="validateField('username')"
          @blur="validateField('username')"
        />

        <FormField
          v-model="formData.nickname"
          label="nickname"
          type="text"
          placeholder="nickname"
          :error="errors.nickname"
          @input="validateField('nickname')"
          @blur="validateField('nickname')"
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
import { signupFormData } from '@/stores/signupStore';

// ========================
// Data
// ========================
const formData = reactive({
  username: '',
  nickname: '',
  password: '',
  passwordCheck: ''
});

// ========================
// Validation Rules
// ========================
const VALIDATION_RULES = {
  username: {
    minLength: 4,
    maxLength: 20,
    pattern: /^[a-zA-Z0-9_]+$/,
    messages: {
      empty: 'username을 입력하세요.',
      minLength: '최소 4자 이상이어야 합니다.',
      maxLength: '최대 20자 이하여야 합니다.',
      pattern: '영문, 숫자, 언더스코어(_)만 사용 가능합니다.'
    }
  },
  nickname: {
    minLength: 4,
    maxLength: 20,
    pattern: /^[a-zA-Z0-9_]+$/,
    messages: {
      empty: 'nickname을 입력하세요.',
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
const { errors, validateField, validateForm } = useFormValidation(VALIDATION_RULES, formData);

// ========================
// Event Handlers
// ========================
const onPasswordInput = () => {
  validateField('password');
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
  console.log('Username:', formData.username);

  // 전역 store에 데이터 저장
  signupFormData.username = formData.username;
  signupFormData.password = formData.nickname;
  signupFormData.password = formData.password;
  signupFormData.password = formData.passwordCheck;

  // 다음 단계로 이동
  router.push({ name: 'sign.private' });
};
</script>

<style lang="scss" scoped>
@import '@/styles/_variables.scss';

.signup-page {
  @include flex-center;
  min-height: 100vh;
  background-color: map-get($colors, 'dark');

  .signup-card {
    @include card;
    padding: map-get($spacing, '2xl') map-get($spacing, '3xl');
    width: map-get($sizes, 'card-width');
    display: flex;
    flex-direction: column;
    align-items: center;

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

      &:hover {
        background-color: map-get($colors, 'gray-hover');
      }
    }
  }
}
</style>
