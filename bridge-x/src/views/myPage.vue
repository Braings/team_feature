<template>
  <div class="my-page">
    <div class="profile-container">
      <div class="profile-header">
        <h1 class="profile-title">Welcome to Bridge-X</h1>
      </div>

      <div class="profile-card">
        <!-- 프로필 사진 -->
        <div class="profile-avatar">
          <div class="avatar-placeholder">{{ userInitial }}</div>
        </div>

        <!-- 기본 정보 -->
        <div class="info-section">
          <h2 class="section-title">기본 정보</h2>
          <div class="info-grname">
            <div class="info-item">
              <span class="label">이름</span>
              <span class="value">{{ userProfile.username || 'N/A' }}</span>
            </div>
            <div class="info-item">
              <span class="label">생년월일</span>
              <span class="value">{{ formatBirthday(userProfile.birthday) }}</span>
            </div>
            <div class="info-item">
              <span class="label">성별</span>
              <span class="value">{{ formatGender(userProfile.sex) }}</span>
            </div>
          </div>
        </div>

        <!-- 건강 정보 -->
        <div class="info-section">
          <h2 class="section-title">건강 정보</h2>
          <div class="health-grname">
            <div class="health-item">
              <span class="label">키</span>
              <span class="value">{{ userProfile.height || '-' }} cm</span>
            </div>
            <div class="health-item">
              <span class="label">체중</span>
              <span class="value">{{ userProfile.weight || '-' }} kg</span>
            </div>
            <div class="health-item">
              <span class="label">BMI</span>
              <span class="value">{{ calculateBMI() }}</span>
            </div>
          </div>
        </div>

        <!-- 연락처 정보 -->
        <div class="info-section">
          <h2 class="section-title">연락처</h2>
          <div class="contact-item">
            <span class="label">이메일</span>
            <span class="value">{{ userProfile.email || 'N/A' }}</span>
          </div>
        </div>

        <!-- 추천운동 영역 -->
        <div class="info-section">
          <h2 class="section-title">추천 운동</h2>
          <div class="contact-item">
            <span class="value">{{ userExercise.Recommend || 'N/A' }}</span>
          </div>
        </div>
        <!-- 버튼 영역 -->
        <div class="button-area">
          <button class="edit-button">정보 수정</button>
          <button class="logout-button" @click="handleLogout">로그아웃</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { signupFormData } from '@/stores/signupStore';

const router = useRouter();

const userProfile = reactive({
  username: '',
  email: '',
  birthday: '',
  sex: '',
  height: '',
  weight: ''
});

const userExercise = reactive({
  recommend: ''
});

const userInitial = computed(() => {
  const username = localStorage.getItem('username') || 'U';
  return username.charAt(0).toUpperCase();
});

onMounted(() => {
  // 저장된 회원가입 정보를 global store에서 로드
  userProfile.username = signupFormData.username || localStorage.getItem('username') || 'User';
  userProfile.email = signupFormData.email || 'N/A';
  userProfile.birthday = signupFormData.birthday || '';
  userProfile.sex = signupFormData.sex || '';
  userProfile.height = signupFormData.height || '';
  userProfile.weight = signupFormData.weight || '';
});

const formatBirthday = (birthday) => {
  if (!birthday) return 'N/A';
  const [year, month, day] = birthday.split('-');
  return year ? `${year}년 ${month}월 ${day}일` : 'N/A';
};

const formatGender = (sex) => {
  return sex === 'male' ? '남성' : sex === 'female' ? '여성' : 'N/A';
};

const calculateBMI = () => {
  const h = parseFloat(userProfile.height);
  const w = parseFloat(userProfile.weight);
  if (h && w) {
    const bmi = (w / ((h / 100) ** 2)).toFixed(1);
    return `${bmi}`;
  }
  return '-';
};

const handleLogout = () => {
  localStorage.removeItem('authToken');
  localStorage.removeItem('username');
  router.push({ name: 'homePage' });
};
</script>

<style lang="scss" scoped>
@import '@/styles/_variables.scss';

.my-page {
  min-height: 100vh;
  background: linear-gradient(135deg, map-get($colors, 'dark') 0%, #1a1a1a 100%);
  padding: map-get($spacing, '3xl') map-get($spacing, 'xl');
}

.profile-container {
  max-width: 600px;
  margin: 0 auto;
}

.profile-header {
  text-align: center;
  margin-bottom: map-get($spacing, '3xl');
  color: map-get($colors, 'white');
}

.profile-title {
  font-size: 2.5rem;
  font-weight: bold;
  letter-spacing: 3px;
  margin: 0;
}

.profile-card {
  @include card;
  padding: map-get($spacing, '3xl');
  animation: slideUp 0.5s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.profile-avatar {
  display: flex;
  justify-content: center;
  margin-bottom: map-get($spacing, '3xl');
}

.avatar-placeholder {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  font-weight: bold;
  color: white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.info-section {
  margin-bottom: map-get($spacing, '2xl');
  padding-bottom: map-get($spacing, '2xl');
  border-bottom: 1px solname map-get($colors, 'border-light');

  &:last-of-type {
    border-bottom: none;
  }
}

.section-title {
  font-size: 1.1rem;
  font-weight: bold;
  margin-bottom: map-get($spacing, 'xl');
  color: map-get($colors, 'black');
}

.info-grname {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: map-get($spacing, 'lg');
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: map-get($spacing, 'sm');
}

.label {
  font-size: 0.9rem;
  color: map-get($colors, 'text-placeholder');
  font-weight: 500;
}

.value {
  font-size: 1rem;
  color: map-get($colors, 'black');
  font-weight: bold;
}

.health-grname {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: map-get($spacing, 'lg');
}

.health-item {
  background: #f5f5f5;
  padding: map-get($spacing, 'lg');
  border-radius: map-get($radius, 'md');
  display: flex;
  flex-direction: column;
  gap: map-get($spacing, 'sm');
  text-align: center;
}

.contact-item {
  display: flex;
  flex-direction: column;
  gap: map-get($spacing, 'sm');
}

.button-area {
  display: flex;
  gap: map-get($spacing, 'lg');
  margin-top: map-get($spacing, '2xl');
}

.edit-button,
.logout-button {
  flex: 1;
  padding: map-get($spacing, 'lg') map-get($spacing, 'md');
  border: none;
  border-radius: map-get($radius, 'md');
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.2s ease;
}

.edit-button {
  background-color: map-get($colors, 'black');
  color: map-get($colors, 'white');

  &:hover {
    background-color: map-get($colors, 'gray-hover');
  }
}

.logout-button {
  background-color: #f5f5f5;
  color: map-get($colors, 'black');
  border: 1px solid map-get($colors, 'border');

  &:hover {
    background-color: #e0e0e0;
  }
}
</style>
