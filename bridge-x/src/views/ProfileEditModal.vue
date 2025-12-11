<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">프로필 정보 수정</h3>
        <button class="close-button" @click="$emit('close')">&times;</button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="saveChanges">
          <div class="form-group">
            <label for="nickname">이름</label>
            <input type="text" id="nickname" v-model="editableProfile.nickname" required>
          </div>

          <div class="form-group">
            <label for="height">키 (cm)</label>
            <input type="number" id="height" v-model.number="editableProfile.height" min="0" step="0.1">
          </div>

          <div class="form-group">
            <label for="weight">체중 (kg)</label>
            <input type="number" id="weight" v-model.number="editableProfile.weight" min="0" step="0.1">
          </div>

          <div class="form-group">
            <label for="email">이메일</label>
            <input type="email" id="email" :value="editableProfile.email" disabled class="disabled-input">
          </div>

          <div class="modal-footer">
            <button type="submit" class="save-button">저장</button>
            <button type="button" class="cancel-button" @click="$emit('close')">취소</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch } from 'vue';

const props = defineProps({
  isOpen: { // 모달 표시 여부
    type: Boolean,
    required: true
  },
  profileData: { // 부모로부터 받은 현재 프로필 데이터
    type: Object,
    required: true
  }
});

const emit = defineEmits(['close', 'update-profile']);

// prop을 직접 수정하지 않기 위해 reactive 복사본을 만듭니다.
const editableProfile = reactive({
  nickname: props.profileData.nickname || '',
  email: props.profileData.email || '',
  height: props.profileData.height || 0,
  weight: props.profileData.weight || 0
});

// 모달이 열릴 때(isOpen이 true가 될 때)마다 최신 데이터를 복사해 옵니다.
watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    editableProfile.nickname = props.profileData.nickname;
    editableProfile.email = props.profileData.email;
    editableProfile.height = props.profileData.height;
    editableProfile.weight = props.profileData.weight;
  }
});

// 변경 사항 저장 함수
const saveChanges = () => {
  emit('update-profile', { ...editableProfile });
};
</script>

<style lang="scss" scoped>
@use 'sass:color';
@use 'sass:map';
@use '@/styles/_variables.scss'as*;

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.modal-content {
  background: map.get($colors, 'white');
  border-radius: map.get($radius, 'lg');
  width: 90%;
  max-width: 450px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

.modal-header {
  padding: map.get($spacing, 'xl');
  border-bottom: 1px solid map.get($colors, 'border-light');
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-body {
  padding: map.get($spacing, 'xl');
}

.form-group { margin-bottom: map.get($spacing, 'lg'); }
.form-group label {
  display: block;
  margin-bottom: map.get($spacing, 'sm');
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: map.get($spacing, 'md');
  border: 1px solid map.get($colors, 'border');
  border-radius: map.get($radius, 'md');
  box-sizing: border-box;
}

.disabled-input {
    background-color: #f0f0f0;
    color: map.get($colors, 'text-placeholder');
}

.modal-footer {
  margin-top: map.get($spacing, 'xl');
  display: flex;
  justify-content: flex-end;
  gap: map.get($spacing, 'lg');
}


.save-button,
.cancel-button {
  flex: 1;
  padding: map.get($spacing, 'lg') map.get($spacing, 'md');
  border: none;
  border-radius: map.get($radius, 'md');
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.2s ease;
}

.save-button {
  background-color: map.get($colors, 'black');
  color: map.get($colors, 'white');

  &:hover {
    background-color: map.get($colors, 'gray-hover');
  }
}

.cancel-button {
  background-color: #f5f5f5;
  color: map.get($colors, 'black');
  border: 1px solid map.get($colors, 'border');

  &:hover {
    background-color: #e0e0e0;
  }
}
</style>
