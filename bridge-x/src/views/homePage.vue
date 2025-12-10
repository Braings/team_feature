<template>
  <div class="dashboard-container">

    <div class="left-section">
      <div class="content-wrapper">
        <p class="subtitle">맞춤형 운동 추천 솔루션</p>
        <h1 class="title cc-font">B R I D G E - X</h1>
        <p class="footer-text">Team Braings - 2025</p>
      </div>
    </div>

    <div class="right-section">

      <div class="graph-area">
        <p class="graph-title">
          <span class="user-name">{{ userName }}</span>님의<br />
          20대 평균 대비 건강 지표
        </p>

        <div class="chart-mockup">
          <div class="chart-label cc-font great-label">{{ totalGrade }}</div>
          <div class="chart-status">{{ totalGradeText }}</div>

          <div class="bar-container">
            <div class="bar-group" v-for="(item, index) in chartData" :key="index">

              <div class="bar pink-bar" :style="{ height: item.standardHeight + '%' }">
                <span class="bar-value">{{ item.standardValue }}</span>
              </div>

              <div class="bar green-bar" :style="{ height: item.userHeight + '%' }">
                <span class="bar-value">{{ item.userValue }}</span>
              </div>

              <div class="bar-name">{{ item.label }}</div>
            </div>

            <div class="x-axis-line"></div>
          </div>

          <p class="under-bar"></p>
        </div>

        <div class="legend-area">
          <span class="legend-item"><span class="dot pink"></span>20대 평균</span>
          <span class="legend-item"><span class="dot green"></span>나의 기록</span>
        </div>
      </div>

      <div class="recommendation-area">
        <p class="recommendation-title">
          <span class="user-name">{{ userName }}</span>님의<br />
          추천 운동
        </p>

        <div class="recommendation-box cursorPointer" @click="goToPage('recommend')">
          <p class="recommendation-text">{{ exerciseRecommendation }}</p>
        </div>
      </div>

      <div class="side-icons">
        <div class="icon-placeholder cursorPointer myPage-icon" @click="goToPage('myPage')"></div>
        <div class="icon-placeholder cursorPointer compass-icon" @click="goToPage('exerciseFacilities')"></div>
        <div class="icon-placeholder cursorPointer reviews-icon" @click="goToPage('reviews')"></div>
      </div>

    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { ref, reactive, onMounted, computed } from 'vue';
import { getUserProfile, getUserBodyInfo } from '@/api.js';

// [상수] 국민체력 100 성인(20대) 기준 데이터 (3등급/평균 근사치)
const STANDARDS_20S = {
  MALE: { grip: 43.6, situp: 52, flex: 13.9, jump: 236 },
  FEMALE: { grip: 26.8, situp: 37, flex: 19.3, jump: 167 }
};

export default {
  name: 'homePage',
  setup() {
    const router = useRouter();

    // 상태 변수
    const userName = ref('GUEST');
    const exerciseRecommendation = ref('데이터 분석 중...');

    // 사용자 신체 정보 (초기값 0)
    const userStats = reactive({
      sex: 'MALE',
      grip: 0,
      situp: 0,
      flex: 0,
      jump: 0
    });

    // 1. 데이터 로드 (API 호출)
    const fetchUserData = async () => {
      const isLoggedIn = localStorage.getItem('LogIn');

      if (!isLoggedIn) {
        userName.value = 'GUEST';
        exerciseRecommendation.value = '로그인이 필요합니다.';
        return;
      }

      try {
        // (1) 프로필 정보 (이름)
        const profileData = await getUserProfile();
        userName.value = profileData.nickname || profileData.username || '사용자';

        // (2) 신체 능력 정보
        const bodyData = await getUserBodyInfo();

        if (bodyData) {
          // 성별 정규화 (DB값에 따라 대문자로 통일)
          const rawSex = bodyData.sex || bodyData.gender || 'MALE';
          userStats.sex = rawSex.toUpperCase();

          // 수치 데이터 (없으면 0 처리)
          userStats.grip = parseFloat(bodyData.user_grip) || 0;
          userStats.flex = parseFloat(bodyData.user_flex) || 0;
          userStats.situp = parseFloat(bodyData.user_situp) || 0;
          userStats.jump = parseFloat(bodyData.user_jump) || 0;

          exerciseRecommendation.value = 'AI 추천 운동 보기 >';
        } else {
          exerciseRecommendation.value = '신체 정보를 입력해주세요 >';
        }

      } catch (error) {
        console.error("데이터 로드 실패:", error);
        // 에러 시 로그아웃 처리 로직이 필요하다면 여기에 추가
      }
    };

    onMounted(() => {
      fetchUserData();
    });

    // 2. 차트 데이터 계산 (Computed)
    const chartData = computed(() => {
      // 성별에 따른 기준 데이터 선택
      const isFemale = userStats.sex === 'FEMALE' || userStats.sex === 'F';
      const standard = isFemale ? STANDARDS_20S.FEMALE : STANDARDS_20S.MALE;

      // 항목별 데이터 매핑
      const items = [
        { label: '악력', user: userStats.grip, std: standard.grip },
        { label: '유연성', user: userStats.flex, std: standard.flex },
        { label: '윗몸', user: userStats.situp, std: standard.situp },
        { label: '제자리', user: userStats.jump, std: standard.jump },
      ];

      return items.map(item => {
        // 그래프 높이 계산 (최대값을 기준으로 상대 비율 %)
        // 기준값과 내 값 중 큰 것의 1.2배를 100% 높이로 설정하여 그래프가 꽉 차지 않게 여유를 줌
        const maxValue = Math.max(item.user, item.std) * 1.2;
        const base = maxValue > 0 ? maxValue : 100; // 0 나누기 방지

        return {
          label: item.label,
          userValue: item.user,
          standardValue: item.std,
          // CSS 높이 (%) 계산
          userHeight: Math.min((item.user / base) * 100, 100).toFixed(1),
          standardHeight: Math.min((item.std / base) * 100, 100).toFixed(1)
        };
      });
    });

    // 3. 종합 등급 계산 (평균 이상 항목 개수)
    const totalGrade = computed(() => {
      // 데이터가 아예 없으면 표시 안 함
      if (userStats.grip === 0 && userStats.jump === 0) return '-';

      const isFemale = userStats.sex === 'FEMALE' || userStats.sex === 'F';
      const standard = isFemale ? STANDARDS_20S.FEMALE : STANDARDS_20S.MALE;

      let score = 0;
      if (userStats.grip >= standard.grip) score++;
      if (userStats.flex >= standard.flex) score++;
      if (userStats.situp >= standard.situp) score++;
      if (userStats.jump >= standard.jump) score++;

      if (score >= 3) return 'GREAT';
      if (score === 2) return 'GOOD';
      return 'NICE';
    });

    const totalGradeText = computed(() => {
      const grade = totalGrade.value;
      if (grade === 'GREAT') return '우수';
      if (grade === 'GOOD') return '양호';
      if (grade === 'NICE') return '노력';
      return '분석중';
    });

    // 4. 페이지 이동 함수
    const goToPage = (routeName) => {
      if (routeName === 'myPage' || routeName === 'recommend') {
        if (localStorage.getItem('LogIn')) {
          // recommend는 별도 페이지가 없다면 알림 처리 혹은 라우터 이동
          if(routeName === 'recommend') {
             // alert('추천 운동 페이지로 이동합니다.');
             // router.push({ name: 'recommendation' }); // 실제 라우터 이름 확인 필요
          } else {
             router.push({ name: routeName });
          }
        } else {
          alert('로그인이 필요합니다.');
          router.push({ name: 'logIn' });
        }
      } else {
        router.push({ name: routeName });
      }
    };

    return {
      userName,
      exerciseRecommendation,
      chartData,
      totalGrade,
      totalGradeText,
      goToPage
    };
  }
}
</script>

<style lang="scss" scoped>
@use 'sass:color';
@use 'sass:map';

.cursorPointer {
    cursor: pointer;
}

.cc-font {
  font-family: 'TheJamsilOTF6ExtraBold', sans-serif;
}

/* 전체 컨테이너 */
.dashboard-container {
  display: flex;
  width: 100vw;
  height: 100vh;
  font-family: sans-serif;
  overflow: hidden;
}

/* 왼쪽 섹션 */
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

/* 텍스트 래퍼 (왼쪽 하단 고정) */
.content-wrapper {
  position: absolute;
  bottom: 8.5vh;
  left: 1.5vw;
  z-index: 1; /* 오버레이보다 위에 표시 */
}

.subtitle {
  font-size: 1vw;

  font-weight: 300;
}

.title {
  font-size: 3vw;

  font-weight: 900;
  // line-height: 1.2;
  // letter-spacing: 2px;
}

.footer-text {
  font-size: 0.6vw;
  opacity: 0.8;
  margin-left: 0.3vw;
}

/* 오른쪽 섹션 */
.right-section {
  width: 70vw;
  background-color: white;
  position: relative;
  padding: 3vh 3vw;
}

/* 1. 그래프 영역 */
.graph-area {
  position: absolute;
  top: 15vh;
  left: 3vw;
  width: 50vw;
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
  height: 85%; /* 높이 살짝 조정 */
  background-color: #252525;
  box-shadow: 0 4px 10px rgba(0,0,0,0.3);
}

/* 등급 텍스트 */
.chart-mockup .great-label {
  position: absolute;
  top: 2vh;
  right: 2vw;
  font-size: 1.5vw;
  color: #89FFB1;
  letter-spacing: 1px;
}

.chart-mockup .chart-status {
  position: absolute;
  top: 5vh;
  right: 4vw;
  font-size: 2.5vw;
  font-weight: 900;
  color: #89FFB1;
}

/* 그래프 바 컨테이너 */
.bar-container {
    display: flex;
    position: absolute;
    bottom: 3vh;
    left: 2%;
    width: 96%;
    height: 70%;
    align-items: flex-end;
    justify-content: space-around;
}

.bar-group {
    display: flex;
    height: 100%;
    width: 20%;
    align-items: flex-end;
    justify-content: center;
    gap: 8px; /* 막대 사이 간격 */
    position: relative;
}

.bar {
    width: 35%;
    border-radius: 4px 4px 0 0;
    display: flex;
    justify-content: center;
    align-items: flex-start; /* 값 텍스트 상단 */
    font-size: 0.8vw;
    font-weight: bold;
    padding-top: 5px;
    transition: height 0.8s ease-in-out; /* 부드러운 애니메이션 */
    min-height: 25px; /* 값이 0이어도 텍스트가 보이도록 최소 높이 */
}

.bar-value {
    color: #252525;
    z-index: 1;
}

.bar-name {
    position: absolute;
    bottom: -2.5vh;
    color: #cecece;
    font-size: 0.9vw;
    font-weight: 500;
}

.pink-bar { background-color: #FF8080; }
.green-bar { background-color: #89FFB1; }

.under-bar {
  /* 하단 장식 바 (필요 없으면 삭제 가능) */
  display: none;
}

/* 범례 영역 */
.legend-area {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 15px;
  font-size: 0.8vw;
  color: #555;
  font-weight: bold;
}
.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
}
.dot {
  width: 12px;
  height: 12px;
  border-radius: 3px;
  display: inline-block;
}
.dot.pink { background-color: #FF8080; }
.dot.green { background-color: #89FFB1; }


/* 2. 추천 운동 영역 */
.recommendation-area {
  position: absolute;
  bottom: 12vh;
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
  background-color: #f0f0f0;
  padding: 3vh 2vw;
  border-radius: 0.5vw;
  box-shadow: 2px 2px 5px rgba(0,0,0,0.2);
  transition: transform 0.2s;
}

.recommendation-box:hover {
    transform: translateY(-2px);
    background-color: #e8e8e8;
}

.recommendation-text {
  font-size: 1.5vw;
  font-weight: bold;
  margin: 0;
  color: #333;
}


/* 3. 사이드 아이콘 */
.side-icons {
  position: absolute;
  top: 25vh;
  right: 5vw;
  display: flex;
  flex-direction: column;
  gap: 6vh;
}

.icon-placeholder {
  width: 4vw;
  height: 4vw;
  border: 0.15vw solid black;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: transform 0.2s;
  background-color: white;
}
.icon-placeholder:hover {
    transform: scale(1.1);
}

.myPage-icon {
  background-image: url('@/img/myPageIcon.png');
  background-size: 70%;
  background-position: center;
  background-repeat: no-repeat;
}
.compass-icon {
  background-image: url('@/img/mapsIcon.png');
  background-size: 70%;
  background-position: center;
  background-repeat: no-repeat;
}
.reviews-icon {
  background-image: url('@/img/reviewsIcon.png');
  background-size: 60%;
  background-position: center;
  background-repeat: no-repeat;
}
</style>
