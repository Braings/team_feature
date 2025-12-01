<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">{{ reviewId ? '리뷰 수정' : '리뷰 작성' }}</h3>
        <button class="close-button" @click="closeModal">&times;</button>
      </div>

      <div class="modal-body">
        <form @submit.prevent="submitReview">
          <div class="form-group">
            <div class="form-header">
              <select class="form-tag" v-model="reviewData.tag" required>
                <option value="" disabled selected hidden>말머리 선택</option>
                <option value="질문">질문</option>
                <option value="정보">정보</option>
                <option value="잡담">잡담</option>
              </select>
              <input class="form-title" id="title" v-model="reviewData.title" placeholder="제목을 입력하세요" required/>
            </div>
            <div class="form-content">
              <textarea id="content" v-model="reviewData.content" required></textarea>
            </div>
          </div>

          <div class="modal-footer">
            <button type="submit" class="submit-button">
              {{ reviewId ? '수정 완료' : '작성 완료' }}
            </button>
            <button type="button" class="cancel-button" @click="closeModal">취소</button>
          </div>
        </form>
      </div>

    </div>
  </div>
</template>

<script setup>
import { reactive, watch } from 'vue';

// 1. Props 정의 (부모로부터 상태를 받습니다)
const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true
  },
  // 수정 기능을 위해 reviewId를 받을 수도 있습니다.
  reviewId: {
    type: [String, Number],
    default: null
  },
  // 수정 시 기존 데이터를 받기 위한 prop (필요 시)
  initialData: {
    type: Object,
    default: () => ({ rating: 5, content: '' })
  }
});

// Events 정의
const emit = defineEmits(['close', 'submit-success']);

// 리뷰 데이터 상태
const reviewData = reactive({
  rating: props.initialData.rating,
  content: props.initialData.content,
  title: props.initialData.title || '',
  tag: props.initialData.tag || '',
});

// 4. 모달이 열릴 때 초기 데이터를 설정합니다.
watch(() => props.isOpen, (newVal) => {
    if (newVal) {
        reviewData.rating = props.initialData.rating;
        reviewData.content = props.initialData.content;
        reviewData.title = props.initialData.title || '';
        reviewData.tag = props.initialData.tag || '';
    }
});


// 모달 닫기 함수
const closeModal = () => {
    // 부모 컴포넌트에 모달을 닫으라고 알립니다.
    emit('close');
};

// 리뷰 제출 로직
const submitReview = () => {
  emit('update-profile', { ...reviewData });
  console.log('리뷰 제출 데이터:', reviewData);

  // TODO: API 호출 로직 (POST 또는 PUT)

  // 성공적으로 제출되었다고 가정하고 부모에게 알립니다.
  emit('submit-success', reviewData);

  // 제출 후 모달을 닫습니다.
  closeModal();
};
</script>

<style lang="scss" scoped>
@import '@/styles/_variables.scss';

/* --- 모달 기본 스타일 --- */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000; /* 다른 요소보다 위에 표시 */
}

.modal-content {
  background: map-get($colors, 'white');
  border-radius: map-get($radius, 'lg');
  width: 95%;
  max-width: 600px; /* 적절한 크기 설정 */
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.4);
  animation: modalFadeIn 0.3s ease-out;
}

.modal-header {
  padding: map-get($spacing, 'xl');
  border-bottom: 1px solid map-get($colors, 'border-light');
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-title {
  margin: 0;
  font-size: 1.5rem;
  font-weight: bold;
}

.close-button {
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
  color: map-get($colors, 'text-placeholder');
  padding: 0;

  &:hover {
    color: map-get($colors, 'black');
  }
}

.modal-body {
  padding: map-get($spacing, '3xl');
  width: 100%;
}

.form-group {
    margin-bottom: map-get($spacing, 'xl');
    width: 100%;
  .form-header{
    display: flex;
    gap: map-get($spacing, 'lg');
    margin-bottom: map-get($spacing, 'lg');
    .form-tag {
      flex: 1;
      padding: map-get($spacing, 'md');
      border: 1px solid map-get($colors, 'border');
      border-radius: map-get($radius, 'md');
      font-size: map-get($typography, 'base');
    }
    select {
      flex: 1;
      width: 30%;
      padding: map-get($spacing, 'md');
      border: 1px solid map-get($colors, 'border');
      border-radius: map-get($radius, 'md');
      font-size: map-get($typography, 'base');
    }
  }
  .form-title{
    flex: 3;
    padding: map-get($spacing, 'md');
    border: 1px solid map-get($colors, 'border');
    border-radius: map-get($radius, 'md');
    font-size: map-get($typography, 'base');
  }
  .form-content {
    position: relative;
    width: 100%;

    textarea {
    width: 100%;
    height: 150px;
    padding: map-get($spacing, 'md');
    border: 1px solid map-get($colors, 'border');
    border-radius: map-get($radius, 'md');
    font-size: map-get($typography, 'base');
    resize: vertical;
    box-sizing: border-box;
    }
  }
}

.form-group label {
    display: block;
    margin-bottom: map-get($spacing, 'md');
    font-weight: bold;
}

.modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: map-get($spacing, 'lg');
    padding-top: map-get($spacing, 'xl');
    border-top: 1px solid map-get($colors, 'border-light');
}

.submit-button, .cancel-button {
    padding: map-get($spacing, 'md') map-get($spacing, 'lg');
    border-radius: map-get($radius, 'md');
    font-weight: bold;
    cursor: pointer;
    border: none;
}

.submit-button {
    background-color: map-get($colors, 'black');
    color: map-get($colors, 'white');
}

.cancel-button {
    background-color: #e0e0e0;
    color: map-get($colors, 'black');
}

@keyframes modalFadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
