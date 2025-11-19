<template>
  <div class="signup-page">
    <div class="signup-card">
      <h1 class="title">BRIDGE-X</h1>

      <div class="input-area">
        <input
          v-model="formData.id"
          type="text"
          placeholder="ID"
          class="input-field"
        />

        <input
          v-model="formData.password"
          type="password"
          placeholder="PASSWORD"
          class="input-field"
        />

        <input
          v-model="formData.passwordCheck"
          type="password"
          placeholder="PASSWORD Check"
          class="input-field"
        />
      </div>

      <button
        @click="handleNext"
        class="next-button"
      >
        NEXT
      </button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue';

// 입력 필드의 데이터를 저장할 반응형 상태 (reactive 사용)
const formData = reactive({
  id: '',
  password: '',
  passwordCheck: ''
});

// NEXT 버튼 클릭 시 실행될 함수
const handleNext = () => {
  // 현재 입력된 데이터를 콘솔에 출력 (디버깅 목적)
  console.log('--- NEXT 버튼 클릭됨 ---');
  console.log('ID:', formData.id);
  console.log('PASSWORD:', formData.password);
  console.log('PASSWORD Check:', formData.passwordCheck);

  // TODO:
  // 1. 비밀번호 일치 여부 확인 (formData.password === formData.passwordCheck)
  // 2. 입력값 유효성 검사 (길이, 형식 등)
  // 3. 서버로 데이터 전송 (API 호출)
  // 4. 다음 단계로 이동 (라우터 사용)
};

// 컴포넌트 외부에서 사용할 데이터가 있다면 `defineExpose`를 사용하지만,
// 이 간단한 폼에서는 필요하지 않습니다.
</script>

<style scoped>
/* 전체 페이지 스타일: 검은색 배경, 카드 중앙 정렬 */
.signup-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh; /* 화면 전체 높이 사용 */
  background-color: #121212; /* 검은색 계열 배경 */
}

/* 카드 컨테이너 스타일: 흰색 배경, 둥근 모서리, 그림자 */
.signup-card {
  background-color: white;
  padding: 40px 50px;
  border-radius: 20px;
  width: 400px; /* 적당한 너비 */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 타이틀 스타일 */
.title {
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 40px;
  letter-spacing: 5px; /* 이미지처럼 넓은 간격 */
}

/* 입력 필드 영역 스타일 */
.input-area {
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* 입력 필드 왼쪽 정렬 */
  width: 100%;
}

/* 개별 입력 필드 스타일 */
.input-field {
  width: 100%;
  padding: 15px 10px;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box; /* 패딩이 너비에 포함되도록 */
  font-size: 1rem;
  text-transform: uppercase; /* 이미지처럼 대문자 Placeholder 느낌 */
}
.input-field::placeholder {
    color: #888;
}

/* NEXT 버튼 스타일 */
.next-button {
  background-color: black;
  color: white;
  padding: 15px 30px;
  border: none;
  border-radius: 5px;
  font-size: 1.5rem;
  font-weight: bold;
  cursor: pointer;
  margin-top: 10px;
  align-self: flex-end; /* 오른쪽 아래에 위치하도록 */
  /* 이미지 레이아웃을 위해 너비와 높이를 조정 */
  width: 120px;
  height: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.next-button:hover {
  background-color: #333;
}
</style>
