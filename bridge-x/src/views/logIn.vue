<template>
  <div>
    <div class="backbon">
      <div class="left-section">
        <div class="content-wrapper">
          <p class="subtitle">맞춤형 운동 추천 솔루션</p>
          <h1 class="title cc-font" >B R I D G E - X</h1>
          <p class="footer-text">Team Braings - 2025</p>
        </div>
      </div>
      <div class="right-section">

        <div class="LogInToYourAccount cc-font">Log In To Your Account</div>

        <input
          v-model="loginData.ID"
          placeholder="ID"
          class="ID-Box cc-font"
          type="text"/>

        <input
          v-model="loginData.PW"
          placeholder="PASSWORD"
          class="PW-Box cc-font"
          type="password"/>

        <button class="LogIn-Box cc-font" @click="Sign()" :disabled="loading">
          <span v-if="loading">LOGGING IN...</span>
          <span v-else>LOG IN</span>
        </button>

        <div v-if="error" class="error-text cc-font" style="position:absolute; top:66vh; right:10vw; color: #e74c3c;">{{ error }}</div>

        <div class="under-bar"></div>

        <div class="SignUp-Text cc-font cursorPointer" @click="goToPage('sign')" >New To Bridge-X? Sign up</div>

      </div>

    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { reactive, ref } from 'vue';
import { post } from '../api.js';

// 라우터와 상태
const router = useRouter();
const loginData = reactive({ ID: '', PW: '' });
const loading = ref(false);
const error = ref(null);

// 로그인 함수: 실제 API 호출을 수행합니다.
// API 경로와 응답 형식은 백엔드에 맞게 변경하세요.
const Sign = async () => {
  error.value = null;
  if (!loginData.ID || !loginData.PW) {
    error.value = '아이디와 비밀번호를 입력하세요.';
    return;
  }

  loading.value = true;
  try {
    // 예시: POST /auth/login { id, password }
    const body = { id: loginData.ID, password: loginData.PW };
    const res = await post('/auth/login', body);

    // 백엔드 응답에 따라 다릅니다. 토큰을 반환하면 저장 후 이동.
    if (res && res.token) {
      localStorage.setItem('authToken', res.token);
      router.push({ name: 'homePage' });
      return;
    }

    // 성공 플래그로 처리하는 경우
    if (res && (res.success || res.ok)) {
      router.push({ name: 'homePage' });
      return;
    }

    // 기본: 에러 메시지 표시
    error.value = res?.message || '로그인에 실패했습니다.';
  } catch (err) {
    console.error('로그인 에러', err);
    error.value = err?.message || '서버 오류가 발생했습니다.';
  } finally {
    loading.value = false;
  }
};

// 페이지 이동
const goToPage = (routeName) => router.push({ name: routeName });
</script>

<style lang="scss" scoped>
  .cursorPointer {
    cursor: pointer;
  }

  .cc-font {
    font-family: 'CC', sans-serif;
  }

  .backbon{
    display: flex;
    width: 100vw;
    height: 100vh;
    font-family: sans-serif; /* 기본 폰트 설정 */
    overflow: hidden;
    background-color: white;
  }

  // 왼쪽 섹션
  .left-section {
    width: 50vw;
    background-color: #1a1a1a;
    color: white;
    position: relative;
    background-image: url('../img/gym.png');
    background-size: 100%;
    background-position: center;
    background-repeat: no-repeat;

  }

  .left-section .content-wrapper {
    position: absolute;
    bottom: 0;
    left: 0;
    padding: 0 3vw 3vh 3vw;
  }

  .left-section .subtitle {
    font-size: 1.2vw;
    margin-bottom: 0.5vh;
  }

  .left-section .title {
    font-size: 2.5vw;
    font-weight: bold;
    margin: 0;
    line-height: 5vh;
  }

  .left-section .footer-text {
    font-size: 0.8vw;
    margin-top: 5vh;
  }

  // 오른쪽 세션

  .right-section{
    height: 100vh;
    width: 50vw;
    background-color: white;
    position: relative;

  }
  .ID-Box{
    border-radius: 0;
    position: absolute;
    width: 30vw;
    height: 10vh;

    top: 34vh;
    right: 10vw;

    padding: 15px 10px;

    border-radius: 0.5vw;
    background-color: #f3f3f3;
    box-shadow: 2px 2px 5px black;
  }

  .PW-Box{
    border-radius: 0;
    position: absolute;
    width: 30vw;
    height: 10vh;

    top: 47vh;
    right: 10vw;

    padding: 15px 10px;

    border-radius: 0.5vw;
    background-color: #f3f3f3;
    box-shadow: 2px 2px 5px black;
  }

  .LogIn-Box{
    border-radius: 0;
    position: absolute;
    width: 30vw;
    height: 5vh;

    top: 60vh;
    right: 10vw;

    border-radius: 0.5vw;
    background-color: #252525;
    box-shadow: 2px 2px 5px black;

    text-align: center;

    font-size: 1.5vw;
    color: #89FFB1;
    padding: 0.5vh 0.5vw;
    transition: background-color 0.3s ease;
  }

  .SignUp-Text {
  text-align: center;
  position: absolute;
  display: flex;

  top: 67vh;

  left: 0;
  right: 0;

  font-size: 1vw;
  color: black;
  font-weight: 100;

  width: 10vw;
  margin: 0 auto;
  }



  .LogInToYourAccount {
    position: absolute;
    top: 22vh;
    left: 13vw;
    font-size: 2vw;
    color: black;
    padding: 0.5vh 0.5vw;
  }

  // .under-bar {
  //   justify-content: bottom;
  //   width: 30vw;
  //   height: 2vh;
  //   background-color: #252525;
  //   position: absolute;
  //   right: 10vw;
  //   top: 90vh;
  // }

</style>
