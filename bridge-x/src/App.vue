<template>
  <div>
     <div :class="{ 'title': true, 'flame': true}"></div>
    <div class="router-wrapper">
      <router-view />
    </div>
    <div class="header-bar" :style="headerBarStyle">
      <div v-if="!isSignUpRoute" class="header-Text cc-font cursorPointer" @click="handleHeaderClick">{{ userDisplay }}</div>
      <div class="header-Text cc-font cursorPointer" @click="goToPage('homePage')">Home</div>
     </div>
  </div>
</template>


<script>
import { useRouter, useRoute } from 'vue-router';
import { provide, computed, ref, onMounted, watch } from 'vue';

export default {
  setup () {
    const router = useRouter();
    const route = useRoute();
    const userId = ref('Log In');

    const headerBarStyle = ref({
      backgroundColor: 'transparent' // 기본 배경색
    });

    const updateHeaderColor = (colorValue) => {
      console.log(`헤더 색상 변경 요청 수신: ${colorValue}`);
      headerBarStyle.value.backgroundColor = colorValue;
    };

    const goToPage = (routeName) => {
      router.push({ name: routeName });
      console.log(`페이지 이동 요청: ${routeName}`);
    };

    const handleHeaderClick = () => {
      if (userId.value === 'Log In') {
        router.push({ name: 'logIn' });
      } else {
        // 로그아웃 처리
        localStorage.removeItem('authToken');
        localStorage.removeItem('userId');
        userId.value = 'Log In';
        router.push({ name: 'homePage' });
      }
    };

    const userDisplay = computed(() => userId.value);

    onMounted(() => {
      const stored = localStorage.getItem('userId');
      if (stored) {
        userId.value = stored;
      }
    });

    provide('movePageKey', goToPage);
    provide('updateHeaderColorKey', updateHeaderColor);

    const isSignUpRoute = computed(() => {
      return route.name && String(route.name).startsWith('sign');
    });

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

@font-face {
  font-family: 'CC';
  src: url('/fonts/The Jamsil OTF 6 ExtraBold.otf') format('opentype');
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
  font-family: 'CC', sans-serif;
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
