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
          type="text"/>

        <button class="LogIn-Box cc-font" @click="Sign()">
          LOG IN
        </button>

        <div class="under-bar"></div>

        <div class="SignUp-Text cc-font cursorPointer" @click="goToPage('sign')" >New To Bridge-X? Sign up</div>

      </div>

    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { reactive } from 'vue'; // reactive를 사용합니다.

// 1. useRouter를 최상위 레벨에서 호출하고 변수에 할당
const router = useRouter();

// 2. loginData를 최상위 레벨에서 reactive로 정의
const loginData = reactive({
  ID: '',
  PW: ''
});

// 3. Sign 함수: 로그인 로직 처리
const Sign = () => {
  console.log('로그인 시도:', loginData.ID, loginData.PW);
  // 여기에 실제 API 호출 및 로그인 처리 로직을 구현하세요.

  // 예: 로그인 성공 시 메인 페이지로 이동
  // router.push({ name: 'main' });
}

// 4. goToPage 함수: 라우트 이동 처리
const goToPage = (routeName) => {
  // 정의된 router 변수를 사용하여 라우트 이동
  router.push({ name: routeName });
};

// <script setup>을 사용하면 별도로 return 할 필요 없이 최상위 변수와 함수가 템플릿에 노출됩니다.
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
  }
  .SignUp-Text {
  text-align: center;
  position: absolute;

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
