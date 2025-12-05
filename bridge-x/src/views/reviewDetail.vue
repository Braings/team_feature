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

          <article class="post-card" :style="{ boxShadow: '1px 1px 3px black'}">
            <header class="post-header">
              <h1 class="post-title">{{ post.title }}</h1>
              <div class="post-meta">
                <span class="nickname">{{ post.nickname }}</span>
                <span class="date">{{ post.date }}</span>
                <span class="views">조회 {{ post.views }}</span>
              </div>
            </header>

            <div class="post-body">
              <span class="tag">{{ post.tag }}</span>
              <div v-html="post.content"></div>
            </div>

            <div class="post-actions">
              <button class="btn recommend-btn" :class="{ active: isRecommended }" @click="toggleRecommend">
                👍 추천 {{ post.recommend }}
              </button>
              <button class="btn" @click="openEditModal">수정</button>
              <button class="btn danger" @click="deletePost">삭제</button>
            </div>
          </article>

          <article class="comment-card">
            <h3 class="comment-section-title">댓글 ({{ comments.length }})</h3>

            <section class="comment-write-form">
              <textarea
                v-model="newComment.content"
                placeholder="댓글을 작성해 주세요."
                class="comment-textarea"
                :disabled="isCommentSubmitting"
              ></textarea>
              <div class="comment-submit-area">
                <span v-if="isCommentSubmitting" class="loading-message">댓글 작성 중...</span>
                <button
                  class="btn comment-submit-btn"
                  @click="submitComment"
                  :disabled="!newComment.content.trim() || isCommentSubmitting"
                >
                  댓글 등록
                </button>
              </div>
            </section>

            <section class="comment-list-section">
              <div v-if="isCommentLoading" class="comment-status">댓글을 불러오는 중입니다...</div>
              <div v-else-if="comments.length === 0 && !isCommentLoading" class="comment-status">등록된 댓글이 없습니다.</div>
              <div v-else>
                <div v-for="comment in comments" :key="comment.id" class="comment-item">
                  <div class="comment-header-row">
                    <span class="nickname comment-nickname">{{ comment.nickname }}</span>
                    <span class="date comment-date">{{ comment.date }}</span>
                  </div>
                  <p class="comment-content">{{ comment.content }}</p>

                  <div class="comment-actions">
                    <button class="btn reply-btn" @click="toggleReplyForm(comment.id)">답글 달기</button>
                  </div>

                  <section v-if="replyingToId === comment.id" class="reply-write-form">
                    <textarea
                      v-model="newReplyContent"
                      placeholder="답글을 작성해 주세요."
                      class="reply-textarea"
                    ></textarea>
                    <div class="reply-submit-area">
                      <button
                        class="btn reply-submit-btn"
                        @click="submitReply(comment.id)"
                        :disabled="!newReplyContent.trim()"
                      >
                        답글 등록
                      </button>
                      <button type="button" class="btn cancel-btn" @click="toggleReplyForm(null)">취소</button>
                    </div>
                  </section>

                  <section v-if="comment.replies && comment.replies.length > 0" class="reply-list-section">
                    <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                      <div class="reply-header-row">
                        <span class="nickname reply-nickname">{{ reply.nickname }}</span>
                        <span class="date reply-date">{{ reply.date }}</span>
                      </div>
                      <p class="reply-content">{{ reply.content }}</p>
                    </div>
                  </section>

                </div>
              </div>
            </section>
          </article>
        </div>
      </div>

      <div v-else-if="loading" class="list-status detail-status">게시물을 로드하는 중...</div>
      <div v-else-if="error" class="list-status detail-status error">게시물을 로드하는 데 실패했습니다: {{ error }}</div>
      <div v-else class="list-status detail-status">게시물 정보가 없습니다.</div>

    </div>

    <ReviewWriteModal
      :isOpen="isModalOpen"
      :reviewId="detailData.reviewId"
      :initialData="detailData"
      @close="closeModal"
      @submit-success="handleReviewEdit"
    />

  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getReviewDetail, deleteReview } from '@/api.js';
import ReviewWriteModal from './reviewModal.vue';


const route = useRoute();
const router = useRouter();

// 상세 보기 관련 상태
const reviewID = computed(() => route.params.reviewID || '');
const post = ref(null);
const loading = ref(false);
const error = ref(null);

// 추천 상태
const isRecommended = ref(false);

// 상세 보기 로직
async function loadPost(reviewID) {
  loading.value = true;
  error.value = null;

  if (!reviewID) {
      error.value = "유효하지 않은 리뷰 ID입니다.";
      loading.value = false;
      return;
  }
  try {
    const data = await getReviewDetail(reviewID);

    post.value = data.data || data || {
      reviewID: reviewID,
      username: 'sss12',
      title: `샘플 게시물 제목 ${reviewID}`,
      nickname: '운영자1',
      date: '25/11/24',
      views: 123,
      tag: reviewID.length % 3 === 0 ? '정보' : '질문',
      content: '이것은 샘플 게시물의 본문입니다.',
      recommend: 5
    };
  } catch (e) {
    console.error('게시물 로드 실패:', e);
    error.value = e.message;
    post.value = null;
  } finally {
    loading.value = false;
  }
}

// 추천 토글 함수
async function toggleRecommend() {
  if (!post.value) return;

  const isCurrentlyRecommended = isRecommended.value;
  isRecommended.value = !isCurrentlyRecommended;
  post.value.recommend += isCurrentlyRecommended ? -1 : 1;

  const action = isRecommended.value ? '추천' : '추천 취소';

  try {
    console.log(`[Mock API] ${action} 요청: Post ${post.value.reviewID}`);
    alert(`${action}되었습니다! (현재 추천 수: ${post.value.recommend})`);
  } catch (error) {
    isRecommended.value = isCurrentlyRecommended;
    post.value.recommend += isCurrentlyRecommended ? 1 : -1;
    alert('추천/추천 취소에 실패했습니다: ' + error.message);
    console.error('추천 토글 실패:', error);
  }
}

function goBack() {
  router.push({ name: 'reviews' }).catch(()=>{});
}

async function deletePost() {
  if (!post.value || !confirm('정말 삭제하시겠습니까?')) return;
  try {
    await deleteReview(post.value.reviewID);
    alert('삭제되었습니다.');
    router.push({ name: 'reviews' }).catch(()=>{});
  } catch (error) {
    alert('삭제 실패: ' + error.message);
    console.error('삭제 실패:', error);
  }
}

// Watch (상세 보기 로직 처리)
watch(reviewID, (newId) => {
  if (newId) {
    loadPost(newId);
  } else {
    post.value = null;
  }
}, { immediate: true });


// ======================================
// 🌟 댓글 및 대댓글 기능 로직 (ID 기반) 🌟
// ======================================
const isCommentLoading = ref(false);
const isCommentSubmitting = ref(false);

const newComment = reactive({ content: '' });
const replyingToCommentID = ref(null);
const newReplyContent = ref('');

const comments = ref([
  {
    commentID: 101,
    reviewID: 1,
    nickname: '테스터1',
    content: '좋은 정보 감사합니다!',
    date: '25/12/05 10:00',
    replies: [
      { commentID: 10101, parentCommentID: 101, nickname: '운영자', content: '감사합니다.', date: '25/12/05 10:30' }
    ]
  },
  { commentID: 102, reviewID: 1, nickname: '개발자', content: '댓글 테스트', date: '25/12/05 11:00', replies: [] }
]);

async function submitComment() {
  if (!newComment.content.trim()) return;

  isCommentSubmitting.value = true;
  try {
    // 💡 API 호출 시 reviewId 전달
    // await apiCreateComment(post.value.reviewId, { content: newComment.content });

    // Mock Update
    const newID = Date.now();
    comments.value.unshift({
      commentID: newID,
      reviewID: post.value.reviewId,
      nickname: '현재사용자',
      content: newComment.content.trim(),
      date: new Date().toLocaleDateString(),
      replies: []
    });
    newComment.content = '';
  } catch(e) {
      console.error(e);
      alert('실패');
  } finally {
      isCommentSubmitting.value = false;
  }
}

function toggleReplyForm(commentID) {
    if (replyingToCommentID.value === commentID) {
        replyingToCommentID.value = null;
    } else {
        replyingToCommentID.value = commentID;
        newReplyContent.value = '';
    }
}

async function submitReply(parentCommentID) {
    if (!newReplyContent.value.trim()) return;

    // 부모 댓글 찾기 (commentID로 검색)
    const parent = comments.value.find(c => c.commentID === parentCommentID);
    if (!parent) return;

    try {
        // Mock Update
        const newID = Date.now();
        if(!parent.replies) parent.replies = [];

        parent.replies.push({
            commentID: newID,
            parentCommentID: parentCommentID,
            nickname: '현재사용자',
            content: newReplyContent.value.trim(),
            date: new Date().toLocaleDateString()
        });

        newReplyContent.value = '';
        replyingToCommentID.value = null;
    } catch(e) {
        console.error(e);
    }
}

// ======================================
// 모달 기능 로직
// ======================================
const isModalOpen = ref(false);
const detailData = reactive({
  reviewID: null,
  title: '',
  content: '',
  tag: ''
});

// 수정 버튼 클릭 시 호출
const openEditModal = () => {
  if (!post.value) return;

  // 현재 게시물 데이터를 모달용 데이터에 복사
  Object.assign(detailData, {
    reviewID: post.value.reviewID,
    title: post.value.title,
    content: post.value.content,
    tag: post.value.tag
  });

  isModalOpen.value = true;
};


const closeModal = () => {
  isModalOpen.value = false;
};


const handleReviewEdit = (updatedReview) => {
  console.log('리뷰가 수정되었습니다:', updatedReview);
  loadPost(post.value.reviewID);
  closeModal();
};

onMounted(() => {
  // 컴포넌트 마운트 시 초기 데이터 로딩은 watch(username, ...)에서 처리됩니다.
});

</script>

<style lang="scss" scoped>
@use 'sass:color' as c;
@use 'sass:map';
@use '@/styles/_variables' as *;

.list-status {
    box-shadow: 1px 1px 2px black;
    min-height: 200px;
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
    margin-top: 20px;

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
      justify-content: center;
      align-items: center;
      margin-left: 2rem;
      padding: map.get($spacing, 'md') map.get($spacing, 'lg');
      background: transparent;
      border-radius: map.get($radius, 'md');
      box-shadow: map.get($shadows, 'sm');

      .gallery-meta {
        display: flex;
        flex-direction: column;
      }

        .gallery-title { text-align: center; font-size: 2.5rem; margin: 0; }
        .gallery-sub { color: map.get($colors, 'muted'); margin: 0; }
      }

      .controls {
        position: relative;
        margin-bottom: 10px;
        display: flex;
        justify-content: flex-start;
        align-items: center;
        margin-top: map.get($spacing, 'md');
      }

    }

      .review-detail-wrapper {
        position: relative;

      .container {
        max-width: 100%;
        margin: 0 auto;

        .back {
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
          margin: 10px auto;
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
            justify-content: flex-end;

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
              margin-right: auto;

              background-color: map.get($colors, 'white');
              color: map.get($colors, 'black');
              border: 1px solid map.get($colors,'border');

              &.active {
                background-color: map.get($colors, 'dark');
                color: map.get($colors, 'white');
                border-color: transparent;

                &:hover {
                  background-color: c.adjust(map.get($colors,'error'), $lightness: -10%);
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
                background-color: c.adjust(map.get($colors,'error'), $lightness: -10%);
              }
            }
          }
        }

      // =================== comment =======================

      .comment-card {
        min-height: auto;
        background: map.get($colors,'white');
        padding: map.get($spacing,'lg');
        border-radius: map.get($radius,'md');
        box-shadow: map.get($shadows,'sm');
        margin: 20px auto;

        .comment-section-title {
          font-size: 1.5rem;
          margin-bottom: map.get($spacing, 'md');
          padding-bottom: map.get($spacing, 'xs');
          border-bottom: 2px solid map.get($colors, 'border');
          font-family: 'TheJamsilOTF6ExtraBold', sans-serif;
        }

        .comment-write-form {
          margin-bottom: map.get($spacing, 'lg');

          .comment-textarea {
            width: 100%;
            padding: 15px;
            border-radius: map.get($radius, 'sm');
            border: 1px solid map.get($colors, 'border');
            resize: vertical;
            outline: none;
            font-family: 'SCDream5', sans-serif;
            font-size: 1rem;
            transition: border-color 0.2s;

            &:focus {
              border-color: map.get($colors, 'dark');
            }
            &:disabled {
              background-color: map.get($colors, 'light');
              cursor: not-allowed;
            }
          }

          .comment-submit-area {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            margin-top: map.get($spacing, 'sm');

            .loading-message {
              color: map.get($colors, 'muted');
              font-size: 0.9rem;
              margin-right: map.get($spacing, 'md');
            }

            .comment-submit-btn {
              background-color: map.get($colors, 'black');
              color: map.get($colors, 'white');
              border: 1px solid map.get($colors,'black');
              padding: map.get($spacing, 'xs') map.get($spacing, 'md');
              box-shadow: 1px 1px 3px black;

              &:disabled {
                background-color: map.get($colors, 'muted');
                border-color: map.get($colors, 'muted');
                cursor: not-allowed;
              }
            }
          }
        }

        .comment-list-section {
          .comment-status {
              text-align: center;
              color: map.get($colors, 'muted');
              padding: map.get($spacing, 'md');
              border-top: 1px solid map.get($colors, 'light');
          }

          .comment-item {
            padding: map.get($spacing, 'sm') 0;
            border-bottom: 1px dotted map.get($colors, 'border');

            &:first-child { border-top: 1px solid map.get($colors, 'border'); }
            &:last-child { border-bottom: none; }

            .comment-header-row {
              display: flex;
              justify-content: space-between;
              align-items: center;
              font-size: 0.85rem;
              color: map.get($colors, 'muted');
              margin-bottom: 5px;
            }

            .comment-nickname {
              font-weight: bold;
              color: map.get($colors, 'dark');
              padding-right: map.get($spacing, 'sm');
            }

            .comment-content {
              margin: 0;
              padding-left: map.get($spacing, 'sm');
              font-size: 1rem;
            }

            .comment-actions {
                display: flex;
                justify-content: flex-end;
                padding: 0 0 5px;
            }

            .reply-btn {
                background: map.get($colors, 'light');
                color: map.get($colors, 'dark');
                font-size: 0.8rem;
                padding: 0.1rem 0.6rem;
                border-radius: 3px;
                border: 1px solid map.get($colors, 'border');

                &:hover {
                    background: map.get($colors, 'gray-hover');
                }
            }


            .reply-write-form {
                margin: 10px 0 15px 20px;
                padding: 10px;
                background: #f7f7f7;
                border: 1px solid map.get($colors, 'border-light');
                border-radius: map.get($radius, 'sm');

                .reply-textarea {
                    width: 100%;
                    min-height: 60px;
                    padding: 8px;
                    border-radius: map.get($radius, 'xs');
                    border: 1px solid map.get($colors, 'border');
                    resize: vertical;
                    outline: none;
                    font-family: 'SCDream5', sans-serif;
                    font-size: 0.9rem;
                }

                .reply-submit-area {
                    display: flex;
                    justify-content: flex-end;
                    gap: 5px;
                    margin-top: 5px;

                    .reply-submit-btn, .cancel-btn {
                        font-size: 0.8rem;
                        padding: 0.2rem 0.5rem;
                        background-color: map.get($colors, 'dark');
                        color: map.get($colors, 'white');
                        border: none;
                        border-radius: 3px;
                        box-shadow: 1px 1px 3px map.get($colors, 'muted');

                        &:disabled {
                            background-color: map.get($colors, 'muted');
                            cursor: not-allowed;
                            box-shadow: none;
                        }
                    }

                    .cancel-btn {
                        background-color: map.get($colors, 'muted');
                    }
                }
            }

            .reply-list-section {
                margin-left: 20px;
                padding-left: 10px;
                border-left: 2px solid map.get($colors, 'light');

                .reply-item {
                    padding: 8px 0;
                    border-bottom: 1px dotted map.get($colors, 'border-light');

                    &:last-child { border-bottom: none; }

                    .reply-header-row {
                        padding-left: map.get($spacing, 'sm');
                        font-size: 0.75rem;
                        color: map.get($colors, 'muted');
                        display: flex;
                        justify-content: space-between;
                        margin-bottom: 3px;
                    }

                    .reply-nickname {
                        font-weight: bold;
                        color: map.get($colors, 'dark');
                    }

                    .reply-content {
                        margin: 0;
                        padding-left: map.get($spacing, 'md');
                        font-size: 0.9rem;
                    }
                }
            }
          }
        }
      }
    }
  }
}
</style>
