import { ref, computed } from 'vue';

// 반응형 전역 상태로 사용하기 위함

const loggedIn = ref(localStorage.getItem('LogIn') === 'true');

export function useAuth() {
  const isLoggedIn = computed(() => loggedIn.value);

  const login = () => {
    loggedIn.value = true;
    localStorage.setItem('LogIn', 'true');
  };

  const logout = () => {
    loggedIn.value = false;
    localStorage.removeItem('LogIn');

    alert('로그아웃 되었습니다.'); // 로그아웃 시 알림이 없어서 추가
  };

  return { isLoggedIn, login, logout };
}
