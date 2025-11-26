<template>
  <div class="review-detail" v-if="post">
    <div class="container">
      <button class="back" @click="goBack">◀ 목록으로</button>

      <article class="post-card">
        <header class="post-header">
          <h1 class="post-title">{{ post.title }}</h1>
          <div class="post-meta">
            <span class="author">{{ post.author }}</span>
            <span class="date">{{ post.date }}</span>
            <span class="views">조회 {{ post.views }}</span>
          </div>
        </header>

        <section class="post-body">
          <div class="tag">{{ post.tag }}</div>
          <p>{{ post.content }}</p>
        </section>

        <footer class="post-actions">
          <button class="btn" @click="goToEdit">수정</button>
          <button class="btn danger" @click="deletePost">삭제</button>
        </footer>
      </article>
    </div>
  </div>

  <!--  💡 데이터 로딩 중이거나 오류가 있을 때 표시할 메시지 (선택 사항) -->
  <div v-else-if="loading">게시물을 로드하는 중...</div>
  <div v-else-if="error">게시물을 로드하는 데 실패했습니다: {{ error }}</div>
  <div v-else>게시물을 찾을 수 없습니다.</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getReviewDetail, deleteReview } from '@/api.js';

const route = useRoute();
const router = useRouter();

const username = route.params.username || '';
const post = ref(null);
const loading = ref(true);
const error = ref(null);

async function loadPost() {
  loading.value = true;
  error.value = null;
  try {
    const data = await getReviewDetail(username);
    post.value = data.data || data || {
      username,
      title: `샘플 게시물 제목 ${username}`,
      author: '운영자1',
      date: '25/11/24',
      views: 123,
      tag: username.length % 3 === 0 ? '정보' : '질문',
      content: '이것은 샘플 게시물의 본문입니다.'
    };
  } catch (err) {
    console.error('게시물 로드 실패:', err);
    error.value = err.message;
    // 폴백: 샘플 데이터
    post.value = {
      username,
      title: `샘플 게시물 제목 ${username}`,
      author: '운영자1',
      date: '25/11/24',
      views: 123,
      tag: username.length % 3 === 0 ? '정보' : '질문',
      content: '백엔드 연동 중입니다.'
    };
  } finally {
    loading.value = false;
  }
}

function goBack() { router.push({ name: 'reviews' }).catch(()=>{}); }
function goToEdit() { router.push({ name: 'reviewsEdit', params: { username: post.value.username } }).catch(()=>{}); }

async function deletePost() {
  if (!confirm('정말 삭제하시겠습니까?')) return;
  try {
    await deleteReview(username);
    alert('삭제되었습니다.');
    router.push({ name: 'reviews' }).catch(()=>{});
  } catch (err) {
    alert('삭제 실패: ' + err.message);
    console.error('삭제 실패:', err);
  }
}

onMounted(() => {
  loadPost();
});
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;
@use 'sass:map';

.review-detail {
  padding: 3vh 4vw;

  .container { max-width: 900px; margin: 0 auto; }

  .back { background: transparent; border: none; color: map.get($colors,'muted'); cursor: pointer; margin-bottom: 1rem; }

  .post-card { background: map.get($colors,'white'); padding: map.get($spacing,'lg'); border-radius: map.get($radius,'md'); box-shadow: map.get($shadows,'sm');
    .post-header { display:flex; justify-content:space-between; align-items:flex-start;
      .post-title { margin:0; font-size:1.6rem; }
      .post-meta { color: map.get($colors,'muted'); font-size:0.9rem; display:flex; gap:0.8rem; }
    }

    .post-body { margin-top:1rem; .tag { display:inline-block; background: map.get($colors,'light'); padding:0.2rem 0.5rem; border-radius:4px; margin-bottom:0.8rem; } }

    .post-actions { margin-top:1.2rem; display:flex; gap:0.8rem;
      .btn { @include button-base; }
      .danger { background: map.get($colors,'error'); color: white; }
    }
  }
}
</style>
