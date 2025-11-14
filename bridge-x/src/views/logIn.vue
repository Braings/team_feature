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
        <div class="ID-Box">
          <p class="ID-Text">ID</p>
        </div>
        <div class="LogInToYourAccount cc-font">Log In To Your Account</div>
        <div class="PW-Box">
          <p class="PW-Text">PASSWARD</p>
        </div>

        <div class="LogIn-Box">
          <p class="LogIn-Text cc-font">LOG IN</p>
        </div>

        <div class="under-bar"></div>
        <div class="makeAccount">New to Bridge-X? Sign up</div>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  setup () {


    return {}
  }
}
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

    top: 27vh;
    right: 10vw;

    background-color: #f3f3f3;
    box-shadow: 2px 2px 5px black;
  }

  .ID-Box .ID-Text{
    position: absolute;
    top: 1vh;
    left: 0.5vw;
    font-size: 1vw;
    color: black;
    padding: 0.5vh 0.5vw;
  }

  .PW-Box{
    border-radius: 0;
    position: absolute;
    width: 30vw;
    height: 10vh;

    top: 40vh;
    right: 10vw;

    background-color: #f3f3f3;
    box-shadow: 2px 2px 5px black;
  }

  .PW-Box .PW-Text{
    position: absolute;
    top: 1vh;
    left: 0.5vw;
    font-size: 1vw;
    color: black;
    padding: 0.5vh 0.5vw;
  }

  .LogIn-Box{
    border-radius: 0;
    position: absolute;
    width: 30vw;
    height: 5vh;

    top: 53vh;
    right: 10vw;

    background-color: #252525;
    box-shadow: 2px 2px 5px black;
  }

  .LogIn-Box .LogIn-Text{
    text-align: center;
    font-size: 1.5vw;
    color: #89FFB1;
    padding: 0.5vh 0.5vw;
  }

  .makeAccount {
    position: absolute;
    top: 60vh;
    left: 18.5vw;
    font-size: 1vw;
    color: black;
    padding: 0.5vh 0.5vw;
  }

  .LogInToYourAccount {
    position: absolute;
    top: 15vh;
    left: 13vw;
    font-size: 2vw;
    color: black;
    padding: 0.5vh 0.5vw;
  }

  .under-bar {
    justify-content: bottom;
    width: 30vw;
    height: 2vh;
    background-color: #252525;
    position: absolute;
    right: 10vw;
    top: 90vh;
  }

</style>
