<template>
  <div>
    <div :class="{ 'title': true, 'flame': true}"></div>
    <router-view />
    <div v-if="!isSignUpRoute" class="header-bar">
      <div class="header-Text cc-font cursorPointer" @click="handleHeaderClick">{{ userDisplay }}</div>
      <div class="header-Text cc-font cursorPointer" @click="goToPage('homePage')">Home</div>
    </div>
  </div>



</template>

<script>
import { useRouter, useRoute } from 'vue-router';
import { provide, computed, ref, onMounted } from 'vue';

export default {
  setup () {
    const router = useRouter();
    const route = useRoute();
    const userId = ref('Log In');

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

    // 회원가입 관련 라우트에서 헤더 숨김
    const isSignUpRoute = computed(() => {
      return route.name && String(route.name).startsWith('sign');
    });

    return {
      goToPage,
      handleHeaderClick,
      userDisplay,
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
