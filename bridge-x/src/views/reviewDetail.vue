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

      <div class="review-detail-wrapper" v-if="post">
        <div class="container">

          <article class="post-card">
            <header class="post-header">
              <h1 class="post-title">
                <span v-if="isDummy" class="test-badge">[TEST]</span>
                {{ post.title }}
              </h1>
              <div class="post-meta">
                <span class="nickname">{{ post.nickname }}</span>
                <span class="date">{{ post.date || post.createdAt }}</span>
                <span class="views">조회 {{ post.views }}</span>
              </div>
            </header>

            <div class="post-body">
              <span class="tag">[{{ post.tag }}]</span>

              <div class="content-area" v-html="sanitizeContent(post.content)"></div>
            </div>

            <div class="post-actions">
              <button class="btn recommend-btn" :class="{ active: isRecommended }" @click="toggleRecommend">
                추천 {{ post.recommend || 0 }}
              </button>

              <div class="owner-actions" v-if="isAuthor">
                <button class="btn" @click="openEditModal">수정</button>
                <button class="btn danger" @click="deletePost">삭제</button>
              </div>
            </div>
          </article>

          <div class="comment-section">
            <h3>댓글 {{ post.comments ? post.comments.length : 0 }}</h3>

            <form class="comment-form" @submit.prevent="submitComment">
               <textarea
                 v-model="newComment"
                 placeholder="댓글을 남겨보세요..."
                 required
               ></textarea>
               <button type="submit">등록</button>
            </form>

            <div class="comment-list" v-if="post.comments && post.comments.length > 0">
              <div class="comment-item" v-for="comment in post.comments" :key="comment.id">
                 <div class="comment-header-row">
                    <span class="comment-nickname">{{ comment.nickname }}</span>
                    <span class="comment-date">{{ comment.date }}</span>

                    <button class="comment-delete-btn" @click="removeComment(comment.id)">×</button>
                 </div>
                 <p class="comment-content">{{ comment.content }}</p>
              </div>
            </div>
             <div v-else class="no-comments">
               <p>작성된 댓글이 없습니다. 첫 번째 댓글을 남겨보세요!</p>
             </div>
          </div>

        </div>
      </div>

      <div v-else class="loading-state">
        <p>게시글을 불러오는 중입니다...</p>
      </div>

    </div>

    <ReviewModal
      v-if="isEditModalOpen"
      :isOpen="isEditModalOpen"
      :reviewID="currentId"
      :initialData="post"
      @close="closeEditModal"
      @submit-success="onEditSuccess"
    />

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
// DOMPurify 임포트
import DOMPurify from 'dompurify';
// API 함수들 임포트 (toggleReviewRecommend 추가됨)
import { getReviewDetail, deleteReview, createComment, deleteComment, toggleReviewRecommend } from '@/api';
import ReviewModal from './reviewModal.vue';

const route = useRoute();
const router = useRouter();

// === 상태 변수들 ===
const post = ref(null);
const isRecommended = ref(false);
const newComment = ref('');
const isDummy = ref(false);
const isEditModalOpen = ref(false);

// === 1. [권한 체크] 작성자 확인 로직 ===
const isAuthor = computed(() => {
  // 로컬 스토리지에 저장된 내 닉네임 가져오기
  const myNickname = localStorage.getItem('nickname');
  // 게시글 작성자와 내 닉네임 비교
  return post.value && myNickname && (post.value.nickname === myNickname);
});

// === 2. [보안] XSS 방지 함수 ===
const sanitizeContent = (content) => {
  return DOMPurify.sanitize(content);
};

// === 3. 더미 데이터 (서버 에러 시 비상용) ===
const DUMMY_DATA = {
  reviewId: 9999,
  title: "데이터 로드 실패 - 테스트 모드",
  nickname: "Bridge-Admin",
  date: "2024-01-01",
  views: 0,
  tag: "시스템",
  content: `<p>서버와 연결할 수 없어 <strong>더미 데이터</strong>를 표시합니다.</p>
            <p>보안 스크립트 테스트: <script>alert('xss')<//script> (이 경고창이 뜨면 안됨) </p>`,
  recommend: 10,
  comments: []
};

// ID 추출
const currentId = route.params.reviewID || route.params.reviewId || route.params.id || route.params.username;

// === 데이터 로드 ===
const fetchReview = async () => {
  try {
    const data = await getReviewDetail(currentId);

    if (!data || (data.success === false)) throw new Error("Invalid Data");

    console.log("데이터 로드 성공:", data);
    post.value = data;
    isDummy.value = false;

    // 서버에서 '내가 좋아요 눌렀는지' 정보를 준다면 여기서 세팅
    // isRecommended.value = data.isLiked || false;

  } catch (error) {
    console.error("API 로드 실패, 더미 데이터 사용:", error);
    post.value = DUMMY_DATA;
    isDummy.value = true;
  }
};

// === 4. 👍 [기능 수정] 추천(좋아요) 서버 저장 ===
const toggleRecommend = async () => {
  // 더미 데이터일 경우 UI만 변경
  if (isDummy.value) {
    isRecommended.value = !isRecommended.value;
    if(post.value) post.value.recommend = (post.value.recommend || 0) + (isRecommended.value ? 1 : -1);
    return;
  }

  try {
    // 실제 API 호출 (서버에 저장)
    const response = await toggleReviewRecommend(currentId);

    // 서버가 최신 추천 수와 내 상태를 돌려준다고 가정
    if (response) {
       // 만약 서버가 숫자만 준다면: post.value.recommend = response.count;
       // 토글 방식이라면 다시 로드하거나 수동 계산
       isRecommended.value = !isRecommended.value;
       post.value.recommend += (isRecommended.value ? 1 : -1);
    }
  } catch (error) {
    console.error("추천 실패:", error);
    alert("추천을 반영하지 못했습니다.");
  }
};

// === 수정/삭제/댓글 등 나머지 기능 (기존 유지) ===
const openEditModal = () => {
  if (isDummy.value) { alert("더미 데이터는 수정할 수 없습니다."); return; }
  isEditModalOpen.value = true;
};

const closeEditModal = () => { isEditModalOpen.value = false; };

const onEditSuccess = () => {
  alert("수정이 완료되었습니다.");
  closeEditModal();
  fetchReview();
};

const deletePost = async () => {
  if (isDummy.value) { alert("더미 데이터는 삭제할 수 없습니다."); return; }
  if (!confirm("정말로 게시글을 삭제하시겠습니까?")) return;

  try {
    await deleteReview(currentId);
    alert("삭제되었습니다.");
    router.push({ name: 'reviews' });
  } catch (error) {
    console.error(error);
    alert("삭제 중 오류가 발생했습니다.");
  }
};

const submitComment = async () => {
  if (!newComment.value.trim()) return;

  const myNickname = localStorage.getItem('nickname') || "익명";

  if (isDummy.value) {
    post.value.comments.push({
      id: Date.now(),
      nickname: myNickname,
      date: new Date().toISOString().split('T')[0],
      content: newComment.value
    });
    newComment.value = '';
    return;
  }

  try {
    const commentData = {
       reviewId: currentId,
       content: newComment.value,
       nickname: myNickname
    };
    await createComment(currentId, commentData);
    await fetchReview();
    newComment.value = '';
  } catch (error) {
    console.error("댓글 작성 실패:", error);
    alert("댓글을 등록하지 못했습니다.");
  }
};

const removeComment = async (commentId) => {
  if (isDummy.value) {
    post.value.comments = post.value.comments.filter(c => c.id !== commentId);
    return;
  }
  if(!confirm("댓글을 삭제하시겠습니까?")) return;

  try {
    await deleteComment(commentId);
    await fetchReview();
  } catch (error) {
    alert("댓글 삭제 실패");
    console("댓글 삭제 실패:",error);
  }
};

const goBack = () => { router.push({ name: 'reviews' }); };

onMounted(() => { fetchReview(); });
</script>

<style lang="scss" scoped>
@use 'sass:map';

// === 색상 팔레트 (기존 테마 유지) ===
$colors: (
  'bg-gray': #f5f5f5,
  'white': #ffffff,
  'dark': #333333,
  'black': #000000,
  'primary': #007bff,
  'muted': #888888,
  'border': #dddddd,
  'border-light': #eeeeee,
  'light': #f9f9f9,
  'danger': #ff4d4d,
  'success': #28a745
);

$spacing: ('sm': 8px, 'md': 16px, 'lg': 24px, 'xl': 32px);
$radius: ('md': 8px);

// 로딩
.loading-state {
    text-align: center;
    padding: 100px;
    font-size: 1.2rem;
    color: map.get($colors, 'muted');
}

.test-badge {
    color: map.get($colors, 'danger');
    font-size: 0.6em;
    vertical-align: middle;
    margin-right: 5px;
}

// 레이아웃
.gallery-page {
  min-height: 100vh;
  background-color: map.get($colors, 'bg-gray');
  padding-bottom: 50px;
}

.gallery-main {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 map.get($spacing, 'md');
}

.gallery-header {
  padding: map.get($spacing, 'xl') 0;
  text-align: center;
  margin-bottom: map.get($spacing, 'lg');
}

.gallery-title {
  font-size: 2.5rem;
  font-weight: 900;
  margin: 0;
}

.gallery-sub {
  color: map.get($colors, 'muted');
  margin-top: map.get($spacing, 'sm');
}

.controls { margin-bottom: map.get($spacing, 'lg'); }

.back {
  background: none;
  border: none;
  font-size: 1rem;
  cursor: pointer;
  color: map.get($colors, 'dark');
  font-weight: bold;
  &:hover { text-decoration: underline; }
}

.container {
  display: flex;
  flex-direction: column;
  gap: map.get($spacing, 'lg');
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

// === 게시글 카드 스타일 ===
.post-card {
  background: map.get($colors, 'white');
  border-radius: map.get($radius, 'md');
  padding: map.get($spacing, 'xl');
  border: 1px solid map.get($colors, 'border');
  box-shadow: 1px 1px 3px rgba(0,0,0,0.1);
}

.post-header {
  border-bottom: 1px solid map.get($colors, 'border-light');
  padding-bottom: map.get($spacing, 'md');
  margin-bottom: map.get($spacing, 'lg');
}

.post-title {
  font-size: 1.8rem;
  margin: 0 0 map.get($spacing, 'sm') 0;
  line-height: 1.3;
}

.post-meta {
  display: flex;
  gap: map.get($spacing, 'md');
  color: map.get($colors, 'muted');
  font-size: 0.9rem;
}

.post-body {
  min-height: 200px;
  line-height: 1.6;
  font-size: 1.1rem;
  margin-bottom: map.get($spacing, 'xl');

  .tag {
    display: inline-block;
    background-color: map.get($colors, 'light');
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 0.9rem;
    color: map.get($colors, 'primary');
    margin-bottom: 15px;
    font-weight: bold;
  }
}

.post-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid map.get($colors, 'border-light');
  padding-top: map.get($spacing, 'lg');
}

.owner-actions {
  display: flex;
  gap: map.get($spacing, 'sm');
}

.btn {
  padding: 8px 16px;
  border: 1px solid map.get($colors, 'border');
  background: white;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  font-weight: 500;

  &:hover { background: map.get($colors, 'light'); }

  &.danger {
    color: map.get($colors, 'danger');
    border-color: map.get($colors, 'danger');
    &:hover { background: #fff0f0; }
  }
}

.recommend-btn {
  border-color: map.get($colors, 'success');
  color: map.get($colors, 'success');

  &.active {
      background-color: map.get($colors, 'success');
      color: white;
  }
  &:hover {
      background-color: #e8f5e9;
  }
}

// === 댓글 섹션 스타일 ===
.comment-section {
  background: map.get($colors, 'white');
  border-radius: map.get($radius, 'md');
  padding: map.get($spacing, 'lg');
  border: 1px solid map.get($colors, 'border');

  h3 { margin-top: 0; margin-bottom: map.get($spacing, 'md'); }
}

.comment-form {
  display: flex;
  gap: map.get($spacing, 'sm');
  margin-bottom: map.get($spacing, 'lg');

  textarea {
    flex: 1;
    height: 60px;
    padding: 12px;
    border: 1px solid map.get($colors, 'border');
    border-radius: 4px;
    resize: none;
    font-family: inherit;
    &:focus { outline: 1px solid map.get($colors, 'dark'); }
  }

  button {
    padding: 0 24px;
    background: map.get($colors, 'dark');
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-weight: bold;
    &:hover { background: map.get($colors, 'black'); }
  }
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: map.get($spacing, 'md');
}

.comment-item {
  padding: map.get($spacing, 'md');
  background: map.get($colors, 'light');
  border-radius: 6px;
  position: relative;

  .comment-header-row {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 6px;
    font-size: 0.9rem;

    .comment-nickname { font-weight: bold; color: map.get($colors, 'dark'); }
    .comment-date { color: map.get($colors, 'muted'); font-size: 0.8rem; }
  }

  .comment-content {
    margin: 0;
    font-size: 0.95rem;
    line-height: 1.4;
    white-space: pre-wrap;
  }

  .comment-delete-btn {
      position: absolute;
      top: 10px;
      right: 10px;
      background: none;
      border: none;
      color: map.get($colors, 'muted');
      cursor: pointer;
      font-size: 1.2rem;
      &:hover { color: map.get($colors, 'danger'); }
  }
}

.no-comments {
    text-align: center;
    color: map.get($colors, 'muted');
    padding: 30px;
    background: map.get($colors, 'light');
    border-radius: 6px;
}
</style>
