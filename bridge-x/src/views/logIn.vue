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
          v-model="formData.username"
          type="text"
          placeholder="USERNAME"
          :error="errors.username"
          @blur="validateField('username', formData)"
          @input="onUsernameInput"
        />

        <FormField
          v-model="passwordDisplay"
          type="password"
          placeholder="PASSWORD"
          :error="errors.password"
          @blur="validateField('password', formData)"
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
import { ref, reactive } from 'vue';
import { loginFormData, submitLogin } from '@/stores/loginStore';
import { getUserProfile } from '@/api'; // [기능 유지] 프로필 조회 API
import FormField from '@/components/FormField.vue'; // 경로 확인 필요

const router = useRouter();
const loading = ref(false);
const passwordDisplay = ref('');

// 폼 데이터 (Store 연동)
const formData = loginFormData;

// 에러 상태
const errors = reactive({
  username: '',
  password: ''
});

// 유효성 검사
const validateField = (field, data) => {
  if (field === 'username') {
    errors.username = !data.username ? '아이디를 입력해주세요.' : '';
  }
  if (field === 'password') {
    errors.password = !data.password ? '비밀번호를 입력해주세요.' : '';
  }
};

const onUsernameInput = () => {
  errors.username = '';
};

const onPasswordInput = () => {
  errors.password = '';
  formData.password = passwordDisplay.value;
};

// 회원가입 페이지 이동
const goToSignUp = () => {
  router.push({ name: 'sign.id' });
};

// [기능 유지] 로그인 처리 로직
const handleLogin = async () => {
  validateField('username', formData);
  validateField('password', formData);

  if (errors.username || errors.password) return;

  loading.value = true;

  try {
    // 1. 로그인 요청
    await submitLogin();

    // 2. 로그인 성공 플래그
    localStorage.setItem('LogIn', 'true');

    // 3. 사용자 정보 가져오기 & 저장 (헤더 닉네임 표시용)
    const userProfile = await getUserProfile();
    if (userProfile) {
      localStorage.setItem('username', userProfile.username || '');
      localStorage.setItem('nickname', userProfile.nickname || '');
    }

    // 4. 홈으로 이동
    router.push({ name: 'homePage' });

  } catch (error) {
    console.error(error);
    alert(error.message || "아이디 또는 비밀번호를 확인해주세요.");
  } finally {
    loading.value = false;
  }
};
</script>

<style lang="scss" scoped>
/* 원래 스타일 복구 */
.login-container {
  display: flex;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  font-family: sans-serif;
}

/* 왼쪽 섹션 스타일 (이미지, 오버레이, 텍스트 위치) */
.left-section {
  width: 50vw;
  height: 100vh;
  background-image: url('@/img/logInGym.jpg'); /* 이미지 경로 확인 */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-color: #333;
  position: relative;
  color: rgb(255, 255, 255);
}

/* 어두운 오버레이 효과 */
.left-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
}

/* 텍스트 래퍼 (왼쪽 하단 고정) */
.content-wrapper {
  position: absolute;
  bottom: 11vh;
  left: 3vw;
  z-index: 1; /* 오버레이보다 위에 표시 */
}

.subtitle {
  font-size: 1.2vw;

  font-weight: 300;
}

.title {
  font-size: 3.5vw;

  font-weight: 900;
  // line-height: 1.2;
  // letter-spacing: 2px;
}

.footer-text {
  font-size: 0.8vw;
  opacity: 0.8;
  margin-left: 0.3vw;
}

/* 오른쪽 섹션 스타일 */
.right-section {
  width: 50vw;
  height: 100vh;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  padding: 40px;
  position: relative;
}

.login-header {
  font-size: 2rem;
  font-weight: 800;
  margin-bottom: 40px;
  letter-spacing: 1px;
  color: #333;
}

.form-area {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 350px;
  gap: 20px;
}

.login-button {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: black;
  color: white;
  padding: 15px;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: bold;
  margin-top: 20px;
  width: 100%;
  border: none;
  cursor: pointer;
}

.login-button:hover:not(:disabled) {
  opacity: 0.8;
}

.login-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.divider {
  width: 80%;
  max-width: 350px;
  height: 1px;
  background-color: #eee;
  margin: 30px 0;
}

.signup-prompt {
  font-size: 0.9rem;
  color: #666;
  cursor: pointer;
}

.signup-link {
  font-weight: bold;
  color: black;
  text-decoration: underline;
}

.signup-link:hover {
  color: #007bff;
}
</style>
