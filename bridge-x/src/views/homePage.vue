<template>
  <div class="dashboard-container">

    <div class="left-section">
      <div class="content-wrapper">
        <p class="subtitle">맞춤형 운동 추천 솔루션</p>
        <h1 class="title cc-font" >B R I D G E - X</h1>
        <p class="footer-text">Team Braings - 2025</p>
      </div>
    </div>

    <div class="right-section">

      <div class="graph-area">
        <p class="graph-title">
          <span class="user-name">{{ userName }}</span>님의<br />
          연령대 평균 대비 그래프
        </p>

        <div class="chart-mockup">
          <div class="chart-label cc-font great-label">GREAT</div>
          <div class="chart-status ">우수</div>

          <div class="bar-container">
            <div class="bar-group">
              <div class="bar pink-bar" style="height: 10%;">21</div>
              <div class="bar green-bar" style="height: 40%;">108</div>
            </div>
             <div class="bar-group">
              <div class="bar pink-bar" style="height: 10%;">21</div>
              <div class="bar green-bar" style="height: 40%;">108</div>
            </div>
             <div class="bar-group">
              <div class="bar pink-bar" style="height: 10%;">21</div>
              <div class="bar green-bar" style="height: 40%;">108</div>
            </div>
                         <div class="bar-group">
              <div class="bar pink-bar" style="height: 10%;">21</div>
              <div class="bar green-bar" style="height: 40%;">108</div>
            </div>
                         <div class="bar-group">
              <div class="bar pink-bar" style="height: 10%;">21</div>
              <div class="bar green-bar" style="height: 40%;">108</div>
            </div>
                         <div class="bar-group">
              <div class="bar pink-bar" style="height: 10%;">21</div>
              <div class="bar green-bar" style="height: 40%;">108</div>
            </div>
            <div class="x-axis-line"></div>
            <div class="y-axis-line"></div>
          </div>
          <p class="under-bar"></p>
        </div>
      </div>

      <div class="recommendation-area">
        <p class="recommendation-title">
          <span class="user-name">{{ userName }}</span>님의<br />
          추천 운동
        </p>
        <div class="recommendation-box">
          <p class="recommendation-text">{{ exercise }}</p>
        </div>

      </div>

      <div class="side-icons">
        <div class="icon-placeholder cursorPointer myPage-icon"  @click="goToPage('myPage')"></div>
        <div class="icon-placeholder cursorPointer compass-icon" @click="goToPage('exerciseFacilities')"></div>
        <div class="icon-placeholder cursorPointer reviews-icon" @click="goToPage('reviews')"></div>
      </div>

    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue';;



export default {
  name: 'homePage',
  data() {
    const userName = ref('');
    const exerciseRecommendation = ref('');

    onMounted(() => {
      // A. userName 로드
      // 'username'이 localStorage에 없으면 'GUEST'를 기본값으로 사용합니다.
      const storedUserName = localStorage.getItem('username');
      userName.value = storedUserName || 'GUEST';

      // B. 추천 운동(exercise) 로드
      // 'exercise' 키로 저장된 값이 없으면 기본 안내 문구를 사용합니다.
      const storedExercise = localStorage.getItem('exercise');
      exerciseRecommendation.value = storedExercise || '회원님의 건강 정보에 기반한 맞춤 운동을 곧 준비해 드릴게요!';
    });

    const router = useRouter();

    const goToPage = (routeName) => {
      if (routeName === 'myPage') {
        if (localStorage.getItem('authToken')) {
            router.push({ name: routeName });
        } else {
            alert('로그인이 필요합니다.');
            router.push({ name: 'logIn' });
        }
      } else {
            router.push({ name: routeName });
      }
    }
    return {
      exercise : exerciseRecommendation,
      userName : userName,
      goToPage,
    }
  }
}


</script>

<style lang="scss" scoped>

.cursorPointer {
    cursor: pointer;
}

.cc-font {
  font-family: 'TheJamsilOTF6ExtraBold', sans-serif;
}

/* 뷰포트 너비(vw)와 높이(vh)를 사용하여 상대 크기 지정 */
.dashboard-container {
  display: flex;
  width: 100vw;
  height: 100vh;
  font-family: sans-serif; /* 기본 폰트 설정 */
  overflow: hidden;
}

/* 1. 왼쪽 섹션 (약 30% 비율) */
.left-section {
  width: 30vw;
  background-image: url('../img/gym.png');
  background-size: 100%;
  background-position: center;
  background-repeat: no-repeat;
  background-color: #1a1a1a;
  color: white;
  position: relative;
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

/* 2. 오른쪽 섹션 (약 70% 비율) */
.right-section {
  width: 70vw;
  background-color: white;
  position: relative;
  padding: 3vh 3vw;
}


/* 그래프 영역  수정할꺼임 ㅇㅇ */
.graph-area {
  position: absolute;
  top: 15vh;
  left: 3vw;
  width: 50vw; /* 오른쪽 섹션 내에서 상대적인 크기 */
  height: 45vh;
}

.graph-area .graph-title {
  font-size: 1.5vw;
  font-weight: 500;
  line-height: 4vh;
  margin-bottom: 2vh;
}

.user-name {
    font-weight: bold;
}

.chart-mockup {
  border-radius: 15px;
  position: relative;
  width: 100%;
  height: 80%;
  background-color: #252525;
  /* border-left: 0.1vw solid black;
  border-bottom: 0.1vw solid black; */
}

/* GREAT/우수 표시 */
.chart-mockup .great-label {
  position: absolute;
  top: 1vh;
  right: 2vw;
  font-size: 1.5vw;
  color: #89FFB1;
  padding: 0.5vh 0.5vw;
}

.chart-mockup .chart-status {
  position: absolute;
  top: 4vh;
  right: 4vw;
  font-size: 2.5vw;
  font-weight: 900;
  color: #89FFB1;
}

/* 모의 막대 그래프 컨테이너 */
.bar-container {
    display: flex;
    position: absolute;
    bottom: 3vh;
    left: 1vw;
    width: 100%;
    height: 100%;
    align-items: flex-end; /* 막대가 아래에서 위로 올라오게 */
    gap: 5%;
}

.bar-group {
    display: flex;
    height: 100%;
    width: 40%; /* 막대 그룹의 너비 */
    align-items: flex-end;
    justify-content: space-around;
}

.bar {
    width: 40%; /* 개별 막대의 너비 */
    border-radius: 5%;
    margin: 0 0.5%;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    color: #252525;
    font-size: 1vw;
    font-weight: bold;
    padding-top: 0.5vh;
}

.pink-bar {
  background-color: #FF8080;
}

.green-bar {
  background-color: #89FFB1;
}

.under-bar {
  justify-content: bottom;
  width: 51%;
  height: 2vh;
  background-color: #252525;
  position: fixed;
  right: 15.5vw;
  bottom: 6vh;
}


/* 추천 운동 영역 */
.recommendation-area {
  position: absolute;
  bottom: 15vh; /* 아래에서부터 위치 지정 */
  left: 3vw;
  width: 50vw;
}

.recommendation-area .recommendation-title {
  font-size: 1.5vw;
  font-weight: 500;
  line-height: 4vh;
  margin-bottom: 1.5vh;
}

.recommendation-box {
  background-color: #f0f0f0; /* 회색 배경 */
  padding: 3vh 2vw;
  border-radius: 0.5vw;
  box-shadow: 2px 2px 5px black;
}

.recommendation-text {
  font-size: 1.5vw;
  font-weight: bold;
  margin: 0;
}


/* 우측 사이드 아이콘 영역 (자리만 배치) */
.side-icons {
  position: absolute;
  top: 25vh;
  right: 5vw;
  display: flex;
  flex-direction: column;
  gap: 7vh;
}

.icon-placeholder {
  width: 4vw;
  height: 4vw;
  border: 0.2vw solid black; /* 아이콘 영역임을 나타냄 */
  border-radius: 50%; /* 원형 테두리 */
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.5vw;
}

.myPage-icon {
  background-image: url('../img/myPageIcon.png');
  background-size: 80%;
  background-position: center;
  background-repeat: no-repeat;
}
.compass-icon {
  background-image: url('../img/mapsIcon.png');
  background-size: 80%;
  background-position: center;
  background-repeat: no-repeat;
}
.reviews-icon {
  background-image: url('../img/reviewsIcon.png');
  background-size: 60%;
  background-position: center;
  background-repeat: no-repeat;
}

</style>
