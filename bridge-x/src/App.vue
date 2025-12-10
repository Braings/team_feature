<template>
  <div>
     <div :class="{ 'title': true, 'flame': true}"></div>
    <div class="router-wrapper">
      <router-view />
    </div>
    <div class="header-bar" :style="headerBarStyle">
      <div v-if="!isSignUpRoute" class="header-Text cc-font cursorPointer" @click="handleHeaderClick">
        {{ nickname }}
      </div>
      <div class="header-Text cc-font cursorPointer" @click="goToPage('homePage')">Home</div>
     </div>
  </div>
</template>


<script>
import { useRouter, useRoute } from 'vue-router';
import { provide, computed, ref, onMounted, watch } from 'vue';

import { post } from '@/api.js';
import { useAuth } from '@/composables/useAuth';

  
export default {
  setup () {
    const router = useRouter();
    const route = useRoute();

    const { isLoggedIn, logout } = useAuth();
    const nickname = computed(() =>
      isLoggedIn.value ? 'Logout' : 'Login'
    );

    const headerBarStyle = ref({
      backgroundColor: 'transparent' // 기본 배경색
    });

    const syncHeaderText = () => {
      if (isLoggedIn.value) {
        nickname.value = 'Logout';
      } else {
        nickname.value = 'Login';
      }
    };

    const updateHeaderColor = (colorValue) => {
      console.log(`헤더 색상 변경 요청 수신: ${colorValue}`);
      headerBarStyle.value.backgroundColor = colorValue;
    };

    const goToPage = (routeName) => {
      router.push({ name: routeName });
      console.log(`페이지 이동 요청: ${routeName}`);
    };

    // 기존 handleHeaderClick 전면 수정
    const handleHeaderClick = async () => {
      // 로그인 안 된 상태 -> 로그인 페이지로
      if (!isLoggedIn.value) {
        router.push({ name: 'logIn' });
        return;
      }

      // 로그인 된 상태 -> 로그아웃
      try {
        await post('/api/logout');
        logout();  // 반응형 상태 변경 + localStorage 정리
        router.push({ name: 'homePage' });
      } catch (e) {
        console.error(e);
        alert('로그아웃 실패. 다시 시도하여 주십시오.');
      }
    };

    const isSignUpRoute = computed(() => {
      return route.name && String(route.name).startsWith('sign');
    });

    const applyRouteStyle = (newRouteName) => {
      // Login / Logout 텍스트 동기화
      syncHeaderText();

      // 색상 로직 (기존 그대로)
      if (newRouteName === 'myPage') {
        headerBarStyle.value.color = 'white';
      } else if (String(newRouteName).startsWith('sign')) {
        headerBarStyle.value.color = 'white';
      } else {
        headerBarStyle.value.backgroundColor = 'transparent';
        headerBarStyle.value.color = 'black';
      }
    };

    // const userDisplay = computed(() =>nickname.value);

    // onMounted 변경
    onMounted(() => {
      applyRouteStyle(route.name);
    });

    provide('movePageKey', goToPage);
    provide('updateHeaderColorKey', updateHeaderColor);

    // 기존 watch는 일단 밀어버리고 새로
    watch(
      () => route.name,
      (newRouteName) => {
        console.log('라우트 변경 감지:', newRouteName);
        applyRouteStyle(newRouteName);
      }
    );
    /*
    // route 변경 시 강제 갱신
    watch(() => route.name, () => {
      console.log('라우트 변경:', route.name);
    });

    // 라우트 변경 감시 및 스타일 자동 변경 로직
    watch(() => route.name, (newRouteName) => {
      console.log('라우트 변경 감지:', newRouteName);

      // 조건 정의
      if (newRouteName === 'myPage') {
        headerBarStyle.value.color = 'white';
      } else if (String(newRouteName).startsWith('sign')) {
        headerBarStyle.value.color = 'white';
      } else {
        headerBarStyle.value.backgroundColor = 'transparent';
        headerBarStyle.value.color = 'black';
      }
    }, { immediate: true }); // 컴포넌트 마운트 시 초기 라우트 검사.
    */
    return {
      goToPage,
      handleHeaderClick,
      // userDisplay,
      headerBarStyle,
      isSignUpRoute,

      nickname
    }
  }
}
</script>

<style>

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

@font-face {
  font-family: 'SCDream4';
  src: url('../public/fonts/SCDream4.otf') format('opentype');
  font-weight: normal;
  font-style: normal;
}

@font-face {
  font-family: 'SCDream5';
  src: url('../public/fonts/SCDream5.otf') format('opentype');
  font-weight: normal;
  font-style: normal;
}

.router-wrapper {
  width: 100%;
  height: 100%;
}

.header-bar {
  position: absolute;
  top: 3vh;
  width: 97vw;
  height: 10vh;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  z-index: 1000;
}

.cursorPointer {
    cursor: pointer;
}

.cc-font {
  font-family: 'TheJamsilOTF6ExtraBold', sans-serif;
}

/* 상단 "Log In" */
.header-Text {
  top: 5vh;
  right: 5vw;
  font-size: 2vw;
  font-weight: 900;
  padding-inline-end: 2vw;
}

</style>
