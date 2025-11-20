<template>
  <div class="login-container">
    <div class="left-section">
      <div class="content-wrapper">
        <p class="subtitle">맞춤형 운동 추천 솔루션</p>
        <h1 class="title">B R I D G E - X</h1>
        <p class="footer-text">Team Braings - 2025</p>
      </div>
    </div>

    <div class="right-section">
      <div class="login-header">Log In To Your Account</div>

      <form class="form-area" @submit.prevent="handleLogin">
        <FormField
          v-model="formData.id"
          type="text"
          placeholder="USERNAME"
          :error="errors.id"
          @blur="validateField('id')"
          @input="onIdInput"
        />

        <FormField
          v-model="passwordDisplay"
          type="password"
          placeholder="PASSWORD"
          :error="errors.password"
          @blur="validateField('password')"
          @input="onPasswordInput"
        />

        <button type="submit" class="login-button" :disabled="loading">
          <span v-if="loading">LOGGING IN...</span>
          <span v-else>LOG IN</span>
        </button>
      </form>

      <div class="divider"></div>

      <div class="signup-prompt" @click="goToSignUp">
        New To Bridge-X?&nbsp;&nbsp;<span class="signup-link">Sign up</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { reactive, ref, computed } from 'vue';
import { post } from '@/api.js';
import FormField from '@/components/FormField.vue';
import { useFormValidation } from '@/composables/useFormValidation';

// ========================
// Data & State
// ========================
const router = useRouter();
const formData = reactive({
  id: '',
  password: ''
});

const loading = ref(false);

// ========================
// Validation Rules
// ========================
const VALIDATION_RULES = {
  id: {
    minLength: 4,
    pattern: /^[a-zA-Z0-9_]*$/,
    messages: {
      empty: '아이디를 입력하세요.',
      minLength: '아이디는 4자 이상이어야 합니다.',
      pattern: '영문, 숫자, 언더스코어(_)만 사용 가능합니다.'
    }
  },
  password: {
    minLength: 8,
    messages: {
      empty: '비밀번호를 입력하세요.',
      minLength: '비밀번호는 8자 이상이어야 합니다.'
    }
  }
};

// ========================
// Form Validation
// ========================
const { errors, validateField, validateForm } = useFormValidation(VALIDATION_RULES);

// ========================
// Password Display Logic
// ========================
const passwordDisplay = computed({
  get: () => '*'.repeat(formData.password.length),
  set: (newVal) => {
    const prevLength = formData.password.length;
    const newLength = newVal.length;

    if (newLength > prevLength) {
      formData.password += newVal[newLength - 1];
    } else if (newLength < prevLength) {
      formData.password = formData.password.slice(0, newLength);
    }
  }
});

// ========================
// Event Handlers
// ========================
const onIdInput = () => {
  // 영문, 숫자, 언더스코어만 필터링
  formData.id = formData.id.replace(/[^\x20-\x7E]/g, '');
};

const onPasswordInput = () => {
  // 비밀번호에서 비ASCII 문자 제거
  formData.password = formData.password.replace(/[^\x20-\x7E]/g, '');
};

const handleLogin = async () => {
  if (!validateForm(formData)) {
    console.log('❌ 폼 검증 실패');
    return;
  }

  loading.value = true;

  try {
    const body = { id: formData.id, password: formData.password };
    const res = await post('/auth/login', body);

    if (res && res.token) {
      localStorage.setItem('authToken', res.token);
      router.push({ name: 'homePage' });
      return;
    }

    if (res && (res.success || res.ok)) {
      router.push({ name: 'homePage' });
      return;
    }

    errors.password = res?.message || '로그인에 실패했습니다.';
  } catch (err) {
    console.error('로그인 에러:', err);
    errors.password = err?.message || '서버 오류가 발생했습니다.';
  } finally {
    loading.value = false;
  }
};

const goToSignUp = () => {
  router.push({ name: 'sign.id' });
};
</script>

<style lang="scss" scoped>
@import '@/styles/_variables.scss';

.login-container {
  display: flex;
  width: 100vw;
  height: 100vh;
  font-family: 'CC', sans-serif;
  overflow: hidden;
  background-color: map-get($colors, 'white');
}

// ========================
// Left Section (Banner)
// ========================
.left-section {
  width: 50vw;
  background-color: map-get($colors, 'dark-gray');
  color: map-get($colors, 'white');
  position: relative;
  background-image: url('@/img/gym.png');
  background-size: 100%;
  background-position: center;
  background-repeat: no-repeat;
}

.content-wrapper {
  position: absolute;
  bottom: 0;
  left: 0;
  padding: 0 3vw 3vh 3vw;
}

.subtitle {
  font-size: 1.2vw;
  margin-bottom: 0.5vh;
}

.title {
  font-size: 2.5vw;
  font-weight: bold;
  margin: 0;
  line-height: 5vh;
  letter-spacing: 2px;
}

.footer-text {
  font-size: 0.8vw;
  margin-top: 5vh;
}

// ========================
// Right Section (Form)
// ========================
.right-section {
  width: 50vw;
  background-color: map-get($colors, 'white');
  position: relative;
  @include flex-center;
  flex-direction: column;
  padding: map-get($spacing, '3xl');
}

.login-header {
  font-size: map-get($typography, 'title');
  font-weight: bold;
  margin-bottom: map-get($spacing, '3xl');
  letter-spacing: 2px;
}

.form-area {
  @include flex-column;
  width: 100%;
  max-width: 350px;
  gap: map-get($spacing, 'xl');
}

.login-button {
  @include button-base;
  @include flex-center;
  background-color: map-get($colors, 'black');
  color: map-get($colors, 'white');
  padding: map-get($spacing, 'lg') map-get($spacing, 'xl');
  border-radius: map-get($radius, 'md');
  font-size: map-get($typography, 'button');
  margin-top: map-get($spacing, 'xl');
  width: 100%;

  &:hover:not(:disabled) {
    background-color: map-get($colors, 'gray-hover');
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}

.divider {
  width: 100%;
  max-width: 350px;
  height: 1px;
  background-color: map-get($colors, 'border');
  margin: map-get($spacing, '2xl') 0;
}

.signup-prompt {
  font-size: map-get($typography, 'base');
  cursor: pointer;
  transition: opacity 0.2s ease;

  &:hover {
    opacity: 0.8;
  }
}

.signup-link {
  color: map-get($colors, 'error');
  font-weight: bold;
}
</style>
