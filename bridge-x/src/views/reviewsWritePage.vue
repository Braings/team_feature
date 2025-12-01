<template>
  <div class="gallery-page">
    <div class="gallery-main">
      <header class="gallery-header">
        <div class="gallery-meta">
          <h2 class="gallery-title">Bridge-X</h2>
          <p class="gallery-sub">for public sports facilities</p>
        </div>
      </header>

      <section class="controls">
        <button class="back" @click="goBack">◀ 목록으로</button>
      </section>


      <div class="review-write-wrapper">
        <div class="write-page">
          <form class="write-form" @submit.prevent="submit">
        <div class="row">
          <select v-model="form.tag" :disabled="loading">
            <option value="">말머리 선택</option>
            <option value="질문">질문</option>
            <option value="정보">정보</option>
            <option value="잡담">잡담</option>
          </select>

          <input v-model="form.title" placeholder="제목을 입력하세요" :disabled="loading" />
        </div>

        <textarea v-model="form.content" placeholder="내용을 입력하세요" rows="12" :disabled="loading"></textarea>

        <div class="actions">
          <button type="submit" class="btn primary" :disabled="loading">
            {{ loading ? '처리 중...' : (isEditMode ? '수정완료' : '작성완료') }}
          </button>
          <button type="button" class="btn" @click="goBack" :disabled="loading">취소</button>
        </div>
      </form>
      </div>

        </div>


      <!-- <template v-else>
        <div v-if="loading" class="list-status">게시물을 로드하는 중...</div>
        <div v-else-if="error" class="list-status">게시물을 로드하는 데 실패했습니다: {{ error }}</div>
        <div v-else class="post-list">

        </div>
      </template> -->


    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getReviews, getReviewDetail, createReview, updateReview } from '@/api.js';

const route = useRoute();
const router = useRouter();


// 목록 관련 상태
const search = ref({ type: 'all', query: '' });
const selectedCategory = ref('all');
const posts = ref([]); // 전체 목록 데이터 (클라이언트 측 필터링용)
const page = ref(1);
const pageSize = 10;
const loading = ref(false); // 목록 로딩 상태
const error = ref(null);


// 상세 보기 관련 상태 (라우트 파라미터 사용)
// 라우트 파라미터를 사용하여 상세 보기 여부를 결정합니다.
const username = computed(() => route.params.username || '');
const post = ref(null); // 상세 게시물 데이터


// URL 파라미터에서 게시글 ID 추출. ID가 있으면 수정 모드
const postId = route.params.id || route.params.username || '';
// 수정 모드 여부를 판단하는 계산된 속성
const isEditMode = computed(() => !!postId);


// 폼 데이터 초기 상태 (author는 기본 '익명')
const form = ref({ tag: '', title: '', content: '', author: '익명' });


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

// 목록 로직
async function loadReviews() {
  // 상세 보기 모드일 때는 목록 로딩을 건너뜁니다.
  if (username.value) return;

  loading.value = true;
  error.value = null;

  search.value.type = route.query.searchType || 'all';
  search.value.query = route.query.query || '';

  selectedCategory.value = route.query.category || 'all';
  try {
    // API 호출 (실제 환경에서는 서버가 필터링 및 페이지네이션을 처리해야 합니다)
    const data = await getReviews({
      page: page.value,
      limit: pageSize,
      category: selectedCategory.value,
      searchType: search.value.type,
      query: search.value.query
    });
    posts.value = data.data || data || [];
  } catch (err) {
    console.error('리뷰 목록 로드 실패:', err);
    error.value = err.message;
    // 폴백: 샘플 데이터
    posts.value = Array.from({ length: 100 }).map((_, i) => ({
      username: `author${i + 1}`,
      no: 100 - i,
      tag: i % 5 === 0 ? '질문' : i % 3 === 0 ? '정보' : (i % 7 === 0 ? '잡담' : '리뷰'),
      title: `샘플 게시물 제목 ${i + 1}`,
      author: `운영자${(i % 6) + 1}`,
      date: '25/11/' + ((i % 30) + 1).toString().padStart(2, '0'),
      views: Math.floor(Math.random() * 500),
      recommend: Math.floor(Math.random() * 50)
    }));
  } finally {
    loading.value = false;
  }
}


// 컴포넌트 마운트 시 loadPost 함수 실행
onMounted(() => {
  loadPost();
});

// Watch 및 Mounted (모드 전환 로직)
// **핵심**: 라우트 파라미터 변경을 감시하여 상세 보기/목록 보기 전환 처리
watch(username, (newUsername) => {
  if (newUsername) {
    loadPost(newUsername); // 상세 보기 로드
  } else {
    post.value = null; // 상세 내용 초기화
    loadReviews(); // 목록 로드 (URL 변경을 통한 목록으로 돌아가기 처리)
  }
}, { immediate: true }); // 컴포넌트 마운트 시 초기 파라미터 검사

watch(() => route.query, (newQuery, oldQuery) => {
    // 1. 현재 라우트가 목록 페이지('reviews')이고
    // 2. 상세 게시물(username)이 로드되지 않은 상태일 때만
    if (!username.value && route.name === 'reviews' && JSON.stringify(newQuery) !== JSON.stringify(oldQuery)) {
        // 검색 쿼리가 변경되었으므로 목록을 다시 로드합니다.
        loadReviews();
    }
}, { deep: true }); // route.query 내부의 변경 사항을 감지하기 위해 deep: true 사용

</script>

<style lang="scss" scoped>
@use 'sass:map';
@use '@/styles/_variables' as *;


.list-status {
  box-shadow: 1px 1px 2px black;
  min-height: 200px; /* 메시지가 보일 영역의 최소 높이 */
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.2rem;
  color: map.get($colors, 'muted');
  border: 1px solid map.get($colors, 'border');
  border-radius: map.get($radius, 'sm');
  background: map.get($colors, 'white');
  padding: 2rem;
  text-align: center;

  &.error {
    color: map.get($colors, 'error');
    font-weight: bold;
  }
}

.gallery-page {
display: flex;
min-height: 100vh;
min-width: 100vw;
gap: 2vw;
padding: 2vh 3vw;
font-family: 'TheJamsilOTF6ExtraBold', sans-serif;
background-color: map.get($colors, 'table');

.gallery-main {
  flex: 1 1 auto;

  .gallery-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: map.get($spacing, 'md') map.get($spacing, 'lg');
    background: transparent;
    border-radius: map.get($radius, 'md');
    box-shadow: map.get($shadows, 'sm');

    .gallery-title { font-size: 2rem; margin: 0; }
    .gallery-sub { color: map.get($colors, 'muted'); margin: 0; }
  }

  .controls {
    position: relative;
    margin-bottom: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: map.get($spacing, 'md');

    .back {
      position: relative;
      background: transparent; border: none;
      color: map.get($colors,'muted');
      cursor: pointer;
    }
  }

  .post-list {
    margin-top: map.get($spacing, 'md');
    .posts-table {
      width: 100%;
      border-collapse: collapse;
      background: map.get($colors,'white');
      border-radius: map.get($radius,'sm');
      overflow: hidden;
      box-shadow: map.get($shadows,'xs');

      th, td {
        font-size: 0.9rem;
        padding: 0.9rem 1rem;
        text-align: center;
        border-bottom: 1px solid map.get($colors,'border');
      }
      thead {
        background: map.get($colors,'light');
        color: map.get($colors,'dark');
      }
      tbody tr { cursor: pointer; }
      .col-no { width:7%; }
      .col-tag { width:8%; }
      .col-title { width:50%; text-align: left;}
      .col-author { width:10%; }
      .col-date { width:10%; }
      .col-views, .col-rec { width:8%; text-align:center; }
    }
  }
}

// 상세 보기 전용 스타일
.review-write-wrapper {
  position: relative;

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
  .container {
    max-width: 100%;
    margin: 0 auto;

    .back {
      position: relative;
      top: 20px;
      left: 20px;
      background: transparent; border: none;
      color: map.get($colors,'muted');
      cursor: pointer;
      margin-bottom: 1rem;
    }

    .post-card {
      min-height: 60vh;
      background: map.get($colors,'white');
      padding: map.get($spacing,'lg');
      border-radius: map.get($radius,'md');
      box-shadow: map.get($shadows,'sm');

      .post-header {
        position: relative;
        display:flex; justify-content:space-between; align-items:flex-start;
        border-bottom: 2px solid map.get($colors,'border');
        .post-title { margin:0; font-size:2rem; }
        .post-meta { position: relative; top:20px; color: map.get($colors,'muted'); font-size:0.9rem; display:flex; gap:0.8rem; }
      }

      .post-body {
        margin-top:1rem;
        min-height: 40vh;
        .tag {
          display:inline-block;
          background: map.get($colors,'light');
          padding:0.2rem 0.5rem;
          border-radius:4px;
          margin-bottom:0.8rem;
        }
      }

      .post-actions {
        margin-top:1.2rem;
        display:flex;
        gap:0.8rem;
        justify-content: flex-end; // 버튼 오른쪽 정렬

        .btn {
          background-color: map.get($colors, 'black');
          color: map.get($colors, 'white');
          border: 1px solid map.get($colors,'black');
          font-size: 1rem;
          padding: 0.3rem 0.8rem;
          cursor: pointer;
          transition: background-color 0.1s ease;
          border-radius:4px;
          box-shadow: 1px 1px 3px black;

          &:hover {
            background-color: map.get($colors, 'dark');
            color: map.get($colors, 'white');
          }
        }

        .recommend-btn {
          margin-right: auto; // 오른쪽 버튼들과 분리하여 왼쪽으로 이동

          background-color: map.get($colors, 'white');
          color: map.get($colors, 'black');
          border: 1px solid map.get($colors,'border');

          &.active {
            background-color: map.get($colors, 'dark');
            color: map.get($colors, 'white');
            border-color: transparent;

            &:hover {
              background-color: darken(map.get($colors,'error'), 10%);
            }
          }

          &:hover:not(.active) {
            background-color: map.get($colors, 'gray-hover');
            color: map.get($colors, 'black');
          }
        }

        .danger {
          background: map.get($colors,'error');
          border-color: map.get($colors,'error');
          &:hover {
            background-color: darken(map.get($colors,'error'), 10%);
          }
        }
      }
    }
  }
}


  .gallery-side { width: 18vw; display:flex; flex-direction:column;

    .category-padding { height: 2rem; }
    .trending-box, .recent-box {
      background: map.get($colors,'white');
      padding: map.get($spacing,'md');
      border-radius: map.get($radius,'md');
      box-shadow: map.get($shadows,'sm');

      h3, h4 { margin: 10px 0 0.8rem 0; font-size: 1rem; }
    }


    .trending-list, .recent-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 0.5rem 0;
        border-bottom: 1px solid map.get($colors,'border');

        &:last-child { border-bottom: none; }
      }
    }

    .trending-list li {
      display:flex;
      justify-content:space-between;
      align-items:center;
      gap:0.5rem;
      cursor: pointer;

      &:hover { background:map.get($colors,'light'); padding-left:0.3rem; }

      .trending-title { flex:1; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
      .trending-count {
        background: map.get($colors,'black');
        color: white;
        padding:0.2rem 0.5rem;
        border-radius:12px;
        font-size:0.8rem;
        font-weight:bold;
      }
    }

    .recent-list li {
      display:flex;
      justify-content:space-between;
      align-items:center;
      gap:0.5rem;
      cursor: pointer;

      &:hover { background:map.get($colors,'light'); padding-left:0.3rem; }

      .recent-title { flex:1; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
      .recent-date {
        color: map.get($colors,'muted');
        font-size:0.8rem;
        white-space:nowrap;
      }
    }
  }
}
</style>
