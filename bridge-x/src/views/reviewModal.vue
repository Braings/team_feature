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
import { createReview, updateReview } from '@/api.js';

// Props 정의
const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true
  },
  reviewId: {
    type: [String, Number],
    default: null
  },
  // 수정 시 기존 데이터를 받기 위한 prop
  initialData: {
    type: Object,
    default: () => ({ title: '', content: '', tag: '' })
  }
});

const emit = defineEmits(['close', 'submit-success']);

// 리뷰 데이터 상태
const reviewData = reactive({
  content: props.initialData.content,
  title: props.initialData.title || '',
  tag: props.initialData.tag || '',
  recommend: props.initialData.recommend || 0,
  views: props.initialData.views || 0,
  nickname: props.initialData.nickname || '',
  username: props.initialData.username || '',
});

// 모달이 열릴 때 초기 데이터를 설정합니다.
watch(() => props.isOpen, (newVal) => {
    if (newVal) {
        // prop에서 현재 값을 가져와 reactive 상태에 복사
        reviewData.content = props.initialData.content;
        reviewData.title = props.initialData.title || '';
        reviewData.tag = props.initialData.tag || '';
        // 💡 'recommend' 필드 사용
        reviewData.recommend = props.initialData.recommend || 0;
        reviewData.views = props.initialData.views || 0;
        reviewData.nickname = props.initialData.nickname || '';
        reviewData.username = props.initialData.username || '';
    }
});


// 모달 닫기 함수
const closeModal = () => {
    emit('close');
};

// 리뷰 제출 로직
const submitReview = async () => {

    // 서버로 보낼 데이터
    const payload = {
      tag: reviewData.tag,
      title: reviewData.title,
      content: reviewData.content,
      // 💡 'recommend' 필드 사용
      recommend: reviewData.recommend,
      views: reviewData.views,
      nickname: reviewData.nickname,
      username: reviewData.username,
    };

    let result = null;

    try {
        if (props.reviewId) {
            // 수정
            result = await updateReview(props.reviewId, payload);
            console.log('리뷰 수정 성공:', result);
        } else {
            // 작성
            result = await createReview(payload);
            console.log('리뷰 작성 성공:', result);
        }

        // 부모 컴포넌트(reviewDetail.vue)에 서버 응답 결과 전달
        emit('submit-success', result);
        closeModal();

    } catch (error) {
        console.error('리뷰 제출 실패:', error);
        alert(`리뷰 제출에 실패했습니다. (${error.raw?.message || '서버 오류'})`);
    }
};
</script>

<style lang="scss" scoped>
@use 'sass:color';
@use 'sass:map';
@use '@/styles/_variables.scss'as*;

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
  background: map.get($colors, 'white');
  border-radius: map.get($radius, 'lg');
  width: 95%;
  max-width: 600px; /* 적절한 크기 설정 */
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.4);
  animation: modalFadeIn 0.3s ease-out;
}

.modal-header {
  padding: map.get($spacing, 'xl');
  border-bottom: 1px solid map.get($colors, 'border-light');
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
  color: map.get($colors, 'text-placeholder');
  padding: 0;

  &:hover {
    color: map.get($colors, 'black');
  }
}

.modal-body {
  padding: map.get($spacing, '3xl');
  width: 100%;
}

.form-group {
    margin-bottom: map.get($spacing, 'xl');
    width: 100%;
  .form-header{
    display: flex;
    gap: map.get($spacing, 'lg');
    margin-bottom: map.get($spacing, 'lg');
    .form-tag {
      flex: 1;
      padding: map.get($spacing, 'md');
      border: 1px solid map.get($colors, 'border');
      border-radius: map.get($radius, 'md');
      font-size: map.get($typography, 'base');
    }
    select {
      flex: 1;
      width: 30%;
      padding: map.get($spacing, 'md');
      border: 1px solid map.get($colors, 'border');
      border-radius: map.get($radius, 'md');
      font-size: map.get($typography, 'base');
    }
  }
  .form-title{
    flex: 3;
    padding: map.get($spacing, 'md');
    border: 1px solid map.get($colors, 'border');
    border-radius: map.get($radius, 'md');
    font-size: map.get($typography, 'base');
  }
  .form-content {
    position: relative;
    width: 100%;

    textarea {
    width: 100%;
    height: 150px;
    padding: map.get($spacing, 'md');
    border: 1px solid map.get($colors, 'border');
    border-radius: map.get($radius, 'md');
    font-size: map.get($typography, 'base');
    resize: vertical;
    box-sizing: border-box;
    }
  }
}

.form-group label {
    display: block;
    margin-bottom: map.get($spacing, 'md');
    font-weight: bold;
}

.modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: map.get($spacing, 'lg');
    padding-top: map.get($spacing, 'xl');
    border-top: 1px solid map.get($colors, 'border-light');
}

.submit-button, .cancel-button {
    padding: map.get($spacing, 'md') map.get($spacing, 'lg');
    border-radius: map.get($radius, 'md');
    font-weight: bold;
    cursor: pointer;
    border: none;
}

.submit-button {
    background-color: map.get($colors, 'black');
    color: map.get($colors, 'white');
}

.cancel-button {
    background-color: #e0e0e0;
    color: map.get($colors, 'black');
}

@keyframes modalFadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
