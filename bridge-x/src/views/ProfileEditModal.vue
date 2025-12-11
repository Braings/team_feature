<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <div class="modal-header">
        <h3>프로필 정보 수정</h3>
        <button class="close-btn" @click="close">&times;</button>
      </div>

      <form @submit.prevent="handleSubmit" class="modal-body">

        <div class="form-group">
          <label class="username">이름 (수정 불가)</label>
          <input
            type="text"
            :value="formData.username"
            disabled
            class="input-disabled"
          />
        </div>

        <div class="form-group-row">
          <div class="form-group">
            <label for="nickname">닉네임</label>
            <input id="nickname" v-model="formData.nickname" type="text" placeholder="닉네임 입력" />
          </div>
          <div class="form-group">
            <label for="email">이메일</label>
            <input id="email" v-model="formData.email" type="email" placeholder="이메일 입력" />
          </div>
        </div>

        <div class="form-group-row">
          <div class="form-group">
            <label for="birthday">생년월일</label>
            <input id="birthday" v-model="formData.birthday" type="date" />
          </div>
          <div class="form-group">
            <label for="sex">성별</label>
            <select id="sex" v-model="formData.sex">
              <option value="">선택</option>
              <option value="MALE">남성</option>
              <option value="FEMALE">여성</option>
            </select>
          </div>
        </div>

        <hr class="divider" />
        <h4 class="sub-title">신체 정보</h4>

        <div class="form-group-row">
          <div class="form-group">
            <label for="height">키 (cm)</label>
            <input id="height" v-model="formData.height" type="number" step="0.1" />
          </div>
          <div class="form-group">
            <label for="weight">체중 (kg)</label>
            <input id="weight" v-model="formData.weight" type="number" step="0.1" />
          </div>
        </div>

        <hr class="divider" />
        <h4 class="sub-title">운동 능력 (측정값)</h4>

        <div class="form-group-grid">
          <div class="form-group">
            <label>악력 (kg)</label>
            <input v-model="formData.user_grip" type="number" step="0.1" />
          </div>
          <div class="form-group">
            <label>유연성 (cm)</label>
            <input v-model="formData.user_flex" type="number" step="0.1" />
          </div>
          <div class="form-group">
            <label>윗몸일으키기 (회)</label>
            <input v-model="formData.user_situp" type="number" />
          </div>
          <div class="form-group">
            <label>제자리멀리뛰기 (cm)</label>
            <input v-model="formData.user_jump" type="number" />
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn-cancel" @click="close">취소</button>
          <button type="submit" class="btn-save">저장하기</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch } from 'vue';

const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
  profileData: {
    type: Object,
    default: () => ({}),
  },
});

const emit = defineEmits(['close', 'update-profile']);

// 폼 데이터 상태
const formData = reactive({
  username: '', // 읽기 전용
  nickname: '',
  email: '',
  birthday: '',
  sex: '',
  height: '',
  weight: '',
  user_grip: '',
  user_flex: '',
  user_situp: '',
  user_jump: '',
});

// 모달이 열릴 때마다 부모(MyPage)에서 받은 데이터로 폼 초기화
watch(
  () => props.isOpen,
  (newVal) => {
    if (newVal) {
      // profileData의 값을 formData로 복사 (깊은 복사가 안전하지 않다면 개별 할당)
      Object.assign(formData, props.profileData);

      // null 값 처리 (input창에 undefined 안 뜨게)
      Object.keys(formData).forEach(key => {
        if (formData[key] === 'N/A' || formData[key] === null) {
            formData[key] = '';
        }
      });
    }
  }
);

const close = () => {
  emit('close');
};

const handleSubmit = () => {
  // 숫자형 데이터 변환 (필요시)
  const payload = {
    ...formData,
    // 필요하다면 숫자 필드를 Number()로 변환해서 전송
    height: formData.height ? Number(formData.height) : null,
    weight: formData.weight ? Number(formData.weight) : null,
    user_grip: formData.user_grip ? Number(formData.user_grip) : null,
    user_flex: formData.user_flex ? Number(formData.user_flex) : null,
    user_situp: formData.user_situp ? Number(formData.user_situp) : null,
    user_jump: formData.user_jump ? Number(formData.user_jump) : null,
  };

  // username은 업데이트 대상에서 제외하려면 여기서 delete payload.username 해도 됨
  // 하지만 백엔드에서 무시한다면 그대로 보내도 무방함.

  emit('update-profile', payload);
};
</script>

<style lang="scss" scoped>
@use 'sass:map';

// 간단한 모달 스타일 (MyPage의 스타일 변수 활용 가능하면 활용)
.modal-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0,0,0,0.2);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;

  h3 { margin: 0; font-size: 1.5rem; color: #333; }
  .close-btn { background: none; border: none; font-size: 1.5rem; cursor: pointer; }
}

.modal-body {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.sub-title {
    margin: 0.5rem 0 0.5rem;
    color: #555;
    font-size: 1.1rem;
}

.divider {
    border: 0;
    border-top: 1px solid #eee;
    margin: 0.5rem 0;
}

// 폼 스타일
.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;

  label { font-size: 0.9rem; font-weight: bold; color: #666; }
  input, select {
    padding: 0.6rem;
    border: 1px solid #ddd;
    border-radius: 6px;
    font-size: 1rem;
    &:focus { outline: 2px solid #333; }
    width: 16vw;
  }

  .input-disabled {
      background-color: #f0f0f0;
      color: #888;
      cursor: not-allowed;
      border: 1px solid #eee;
      width: 100%;
  }
}

.form-group-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.form-group-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1rem;
}

.modal-footer {
  margin-top: 1.5rem;
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.btn-cancel {
  padding: 0.6rem 1.2rem;
  border: 1px solid #ddd;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  &:hover { background: #f9f9f9; }
}

.btn-save {
  padding: 0.6rem 1.2rem;
  background: #333;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  &:hover { background: #555; }
}
</style>
