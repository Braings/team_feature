<template>
  <div>
    <div :class="{ 'title': true, 'flame': true}"></div>
    <router-view />
  </div>
</template>

<script>
import { useRouter } from 'vue-router';
import { provide } from 'vue'; // provide를 import 해야 합니다.

export default {
  setup () {
// 1. 라우터 인스턴스 획득
    const router = useRouter();

    // 2. 페이지 이동 함수 정의
    const goToPage = (routeName) => {
      // 라우터 이름을 이용해 페이지 이동
      router.push({ name: routeName });
      console.log(`페이지 이동 요청: ${routeName}`);
    };

    // 3. goToPage 함수를 'movePageKey'라는 이름(key)으로 제공(provide)
    // 이 key는 다른 컴포넌트에서 주입받을 때 사용됩니다.
    provide('movePageKey', goToPage);

    return {
      goToPage
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

</style>
