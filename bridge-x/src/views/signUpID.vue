<template>
  <div class="signup-page">
    <div class="signup-card">
      <form class="input-area" @submit.prevent="handleNext">
        <h1 class="title">BRIDGE-X</h1>

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

  // 전역 store에 데이터 저장
  signupFormData.username = formData.username;
  signupFormData.nickname = formData.nickname;
  signupFormData.password = formData.password;

  // 다음 단계로 이동
  router.push({ name: 'sign.private' });
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
    // 카드 내부 여백 (위아래는 적당히, 좌우는 넉넉히)
    padding: map.get($spacing, 'xl') map.get($spacing, '2xl');
    width: map.get($sizes, 'card-width');
    min-width: 400px;

    // 카드 내부 요소 정렬
    display: flex;
    flex-direction: column;
    align-items: center; // 가로축 중앙 정렬

    // 1. 폼 영역 스타일
    .input-area {
      width: 100%;
      display: flex;
      flex-direction: column;
      // [핵심] 입력창 사이의 간격을 좁게 설정 ('sm' 또는 'md')
      gap: map.get($spacing, 'sm');
    }

    // 2. 타이틀 스타일 (최상단 중앙)
    .title {
      position: relative;
      font-size: map.get($typography, 'title');
      font-weight: bold;
      letter-spacing: 5px;
      text-align: center; // 텍스트 중앙 정렬
      width: 100%;        // 가로 꽉 채우기

      // [핵심] 상단 여백 제거, 하단 여백 축소 (입력창과 가깝게)
      margin-top: 0;
      margin-bottom: map.get($spacing, 'lg');
    }

    // 3. 버튼 스타일
    .next-button {
      @include button-base;
      @include flex-center;
      background-color: map.get($colors, 'black');
      color: map.get($colors, 'white');

      padding: 12px 0; // 높이 조절
      border-radius: map.get($radius, 'md');
      font-size: map.get($typography, 'button');

      // 버튼 위쪽 여백도 조금 줄임
      margin-top: map.get($spacing, 'md');

      align-self: flex-end; // 버튼만 오른쪽 정렬 유지 (원하면 center로 변경 가능)
      width: map.get($sizes, 'button-width');
      height: map.get($sizes, 'button-height');

      cursor: pointer;
      border: none;

      &:hover {
        background-color: map.get($colors, 'gray-hover');
      }
    }
  }
}
</style>
