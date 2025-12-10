<template>
  <div>
    <div :class="{ 'title': true, 'flame': true}"></div>

    <div class="router-wrapper">
      <router-view />
    </div>

    <div class="header-bar" :style="headerBarStyle">
      <div v-if="!isSignUpRoute" class="header-Text cc-font cursorPointer" @click="handleHeaderClick">
        {{ userDisplay }}
      </div>

      <div class="header-Text cc-font cursorPointer" @click="goToPage('homePage')">Home</div>
    </div>
  </div>
</template>

<script>
import { useRouter, useRoute } from 'vue-router';
import { computed, ref, onMounted, watch } from 'vue';

export default {
  setup () {
    const router = useRouter();
    const route = useRoute();

    // 상태 변수
    const nickname = ref('Log In'); // 기본값
    const headerBarStyle = ref({
      backgroundColor: 'transparent',
      color: 'black'
    });

    // === 1. 로그인 상태 확인 함수 ===
    const checkLoginStatus = () => {
      const isLoggedIn = localStorage.getItem('LogIn');
      const storedNickname = localStorage.getItem('nickname');

      if (isLoggedIn && storedNickname) {
        nickname.value = storedNickname; // 로그인 상태면 닉네임 표시
      } else {
        nickname.value = 'Log In'; // 아니면 로그인 버튼 표시
      }
    };

    // === 2. 헤더 버튼 클릭 핸들러 (로그인/로그아웃 분기) ===
    const handleHeaderClick = () => {
      if (nickname.value === 'Log In') {
        // 로그인 안 된 상태 -> 로그인 페이지로 이동
        router.push({ name: 'logIn' });
      } else {
        // 로그인 된 상태 -> 로그아웃 수행
        if(confirm("로그아웃 하시겠습니까?")) {
          localStorage.removeItem('LogIn');
          localStorage.removeItem('nickname'); // 닉네임 삭제
          // localStorage.clear(); // 필요시 전체 삭제

          checkLoginStatus(); // UI 즉시 업데이트 (Log In으로 변경)

          alert("로그아웃 되었습니다.");
          router.push({ name: 'homePage' }); // 홈으로 이동

          // 강제 새로고침이 필요하다면 아래 주석 해제
          // window.location.reload();
        }
      }
    };

    const goToPage = (routeName) => {
      router.push({ name: routeName });
    };

    // 현재 라우트가 회원가입 관련인지 확인 (헤더 버튼 숨김용)
    const isSignUpRoute = computed(() => {
      return route.name && route.name.toString().startsWith('sign');
    });

    // 화면 표시 텍스트 (회원가입 페이지면 숨김 처리)
    const userDisplay = computed(() => {
      return isSignUpRoute.value ? '' : nickname.value;
    });

    // === 3. 라우터 변경 감지 (페이지 이동 시마다 로그인 상태 체크) ===
    watch(() => route.path, () => {
      checkLoginStatus(); // 페이지 이동할 때마다 스토리지 확인해서 헤더 갱신

      // 헤더 스타일 변경 로직 (기존 코드 유지)
      if (route.name && route.name.toString().startsWith('sign')) {
        headerBarStyle.value.color = 'white';
      } else {
        headerBarStyle.value.backgroundColor = 'transparent';
        headerBarStyle.value.color = 'black';
      }
    });

    // 앱 시작 시 최초 확인
    onMounted(() => {
      checkLoginStatus();
    });

    return {
      goToPage,
      handleHeaderClick,
      userDisplay,
      headerBarStyle,
      isSignUpRoute
    }
  }
}
</script>

<style>
/* 폰트 정의 */
@font-face {
  font-family: 'TheJamsilOTF6ExtraBold';
  src: url('../fonts/The Jamsil OTF 6 ExtraBold.otf') format('opentype');
  font-weight: normal;
  font-style: normal;
}

@font-face {
  font-family: 'SCDream3';
  src: url('../public/fonts/SCDream3.otf') format('opentype');
  font-weight: normal;
  font-style: normal;
}

/* 기존 스타일 유지 */
.router-wrapper {
  width: 100%;
  height: 100vh;
  position: relative;
  z-index: 1;
}

.header-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 80px;
  display: flex;
  justify-content: flex-end; /* 오른쪽 정렬 */
  align-items: center;
  padding: 0 50px; /* 좌우 여백 */
  box-sizing: border-box;
  z-index: 1000; /* 최상단 */
  transition: background-color 0.3s, color 0.3s;
}

.header-Text {
  font-size: 1.2rem;
  margin-left: 40px; /* 버튼 사이 간격 */
  font-weight: bold;
  text-transform: uppercase;
}

.cursorPointer {
  cursor: pointer;
}

.cc-font {
  font-family: 'TheJamsilOTF6ExtraBold', sans-serif;
}

/* 배경 애니메이션 (기존 코드 유지) */
.title {
  position: fixed;
  width: 100%;
  height: 100%;
  z-index: 0;
}
.flame {
  background: white; /* 기본 배경 */
}
</style>
