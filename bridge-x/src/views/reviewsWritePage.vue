<template>
  <div class="write-page">
    <div class="container">
      <button class="back" @click="goBack">◀ 목록으로</button>


    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
// API 함수 임포트
import { getReviewDetail, createReview, updateReview } from '@/api.js';

const router = useRouter();
const route = useRoute();

// URL 파라미터에서 게시글 ID 추출. ID가 있으면 수정 모드
const postId = route.params.id || route.params.username || '';
// 수정 모드 여부를 판단하는 계산된 속성
const isEditMode = computed(() => !!postId);

// 폼 데이터 초기 상태 (author는 기본 '익명')
const form = ref({ tag: '', title: '', content: '', author: '익명' });
// 로딩 상태 관리
const loading = ref(false);
// 에러 메시지 관리
const error = ref(null);

// 게시글 로드 함수 (수정 모드일 때만 실행)
async function loadPost() {
  if (!postId) return; // postId가 없으면 작성 모드이므로 로드하지 않음

  try {
    const data = await getReviewDetail(postId); // API 호출로 기존 데이터 가져오기
    const post = data.data || data;
    // 폼에 기존 데이터 할당
    form.value = {
      tag: post.tag || '',
      title: post.title || '',
      content: post.content || '',
      author: post.author || '익명'
    };
  } catch (err) {
    console.error('게시물 로드 실패:', err);
    error.value = '게시물 로드 중 오류 발생';
    // 로드 실패 시 대체 데이터 할당
    form.value = {
      tag: '정보',
      title: `샘플 편집 제목 (${postId})`,
      content: '기존 글 내용 로드 실패.',
      author: '운영자1'
    };
  }
}

// 폼 제출 처리 함수
async function submit() {
  // 제목 및 내용 필수 검증
  if (!form.value.title.trim() || !form.value.content.trim()) {
    alert('제목과 내용을 입력하세요.');
    return;
  }
  // 말머리 필수 검증
  if (!form.value.tag) {
    alert('말머리를 선택해주세요.');
    return;
  }

  loading.value = true;
  error.value = null;

  try {
    if (isEditMode.value) {
      // 수정 모드: updateReview API 호출
      await updateReview(postId, form.value);
      alert('수정되었습니다.');
      // 수정 후 상세 페이지로 이동
      router.push({ name: 'reviewDetail', params: { id: postId } });
    } else {
      // 작성 모드: createReview API 호출
      const res = await createReview(form.value);

      // 응답에서 새로 생성된 게시글 ID/Username 추출
      const newId = res.data?.id || res.id || res.data?.username || res.username;

      alert('작성되었습니다.');

      // 새 ID가 있으면 상세 페이지로, 없으면 목록으로 이동
      if (newId) {
          router.push({ name: 'reviewDetail', params: { id: newId } });
      } else {
          router.push({ name: 'reviews' });
      }

    }
  } catch (err) {
    console.error('제출 실패:', err);
    error.value = err.message || '알 수 없는 오류가 발생했습니다.';
    alert('제출 실패: ' + error.value);
  } finally {
    loading.value = false; // 로딩 상태 해제
  }
}

// 이전 페이지 (목록)로 이동
function goBack() {
  router.push({ name: 'reviews' }).catch(err => {
    // 중복 내비게이션 오류는 무시
    if (err.name !== 'NavigationDuplicated') {
      console.error(err);
    }
  });
}

// 컴포넌트 마운트 시 loadPost 함수 실행
onMounted(() => {
  loadPost();
});
</script>

<style lang="scss" scoped>
// 스타일 파일 임포트
@use '@/styles/variables' as *;
@use 'sass:map';

// CSS 스타일 정의 (SCSS 사용)
.write-page {
  padding: 3vh 4vw;
  .container {
    max-width: 900px;
    margin: 0 auto;
  }
  .back {
    background: transparent; border: none;
    color: map.get($colors,'muted');
    cursor: pointer;
    margin-bottom: 1rem;
  }

  .write-form {
    display:flex;
    flex-direction:column;
    gap:1rem;
    background: map.get($colors,'white');
    padding: map.get($spacing,'lg');
    border-radius: map.get($radius,'md');
    box-shadow: map.get($shadows,'sm');

    // 폼 비활성화 시 스타일
    &:disabled {
      opacity: 0.6;
      pointer-events: none;
    }

    .row {
      display:flex;
      gap:1rem;
      select, input {
        flex:1;
        padding:0.6rem;
        border:1px solid map.get($colors,'border');
        border-radius:4px;
        &:disabled {
          background: map.get($colors, 'light');
        }
      }
    }
    textarea {
      max-width:100%;
      padding:0.8rem;
      border:1px solid map.get($colors,'border');
      border-radius:6px;
      &:disabled {
        background: map.get($colors, 'light');
      }
    }
    .actions {
      display:flex;
      gap:0.8rem;
      .btn {
        @include button-base; // SCSS 믹스인 사용
        &:disabled {
          cursor: not-allowed;
          opacity: 0.5;
        }
      }
      .primary {
        background: map.get($colors,'black');
        color:white;
      }
    }
  }
}
</style>
