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
        <div class="search-area" >
          <select v-model="search.type" :style="{ textAlign: 'center'}">
            <option value="all">전체</option>
            <option value="title">제목</option>
            <option value="author">작성자</option>
          </select>
          <input v-model="search.query" placeholder="검색어 입력" />
          <button class="search-btn" @click="handleSearchAndBlur($event)">검색</button>
        </div>
      </section>

      <div class="review-detail-wrapper" v-if="post">
        <div class="container">

          <article class="post-card" :style="{ boxShadow: '1px 1px 3px black'}">
            <header class="post-header">
              <h1 class="post-title">{{ post.title }}</h1>
              <div class="post-meta">
                <span class="author">{{ post.author }}</span>
                <span class="date">{{ post.date }}</span>
                <span class="views">조회 {{ post.views }}</span>
              </div>
            </header>

            <section class="post-body">
              <div class="tag">#{{ post.tag }}</div>
              <p>{{ post.content }}</p>
            </section>

            <footer class="post-actions">
              <button
                :class="['btn', 'recommend-btn', { 'active': isRecommended }]"
                @click="toggleRecommend">
                <span v-if="isRecommended"> 추천 취소 ({{ post.recommend }})</span>
                <span v-else> 추천 ({{ post.recommend }})</span>
              </button>
              <button class="btn" @click="goToEdit">수정</button>
              <button class="btn danger" @click="deletePost">삭제</button>
            </footer>
          </article>
        </div>
      </div>

      <template v-else>
        <div v-if="loading" class="list-status">게시물을 로드하는 중...</div>
        <div v-else-if="error" class="list-status">게시물을 로드하는 데 실패했습니다: {{ error }}</div>
        <div v-else class="post-list">
          <table class="posts-table" :style="{ boxShadow: '1px 1px 3px black'}">
            <thead>
              <tr>
                <th class="col-no">No</th>
                <th class="col-tag">분류</th>
                <th class="col-title">제목</th>
                <th class="col-author">작성자</th>
                <th class="col-date">날짜</th>
                <th class="col-views">조회</th>
                <th class="col-rec">추천</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(p) in pagedPosts" :key="p.no" :style="{ fontFamily: 'SCDream5'}" @click="openPost(p)">
                <td class="col-no">{{ p.no }}</td>
                <td class="col-tag">{{ p.tag }}</td>
                <td class="col-title">{{ p.title }}</td>
                <td class="col-author">{{ p.author }}</td>
                <td class="col-date">{{ p.date }}</td>
                <td class="col-views">{{ p.views }}</td>
                <td class="col-rec">{{ p.recommend }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </template>

    </div>

    <aside class="gallery-side">
      <div class="category-padding" :style="{ height: '9.8rem'}"></div>
      <div class="trending-box" :style="{ boxShadow: '1px 1px 3px black'}">
        <h3 :style="{ paddingBottom: '18px', borderBottom: '2px solid #ccc', fontSize: '20px' }" > &nbsp;&nbsp; 인기글</h3>
        <ul class="trending-list">
          <li v-for="post in trendingPosts" :key="post.username" @click="openPost(post)">
            <span class="trending-title" :style="{ fontFamily: 'SCDream5'}">{{ post.title }}</span>
            <span class="trending-count">{{ post.recommend }}</span>
          </li>
        </ul>
      </div>
      <div class="category-padding"></div>
      <div class="recent-box" :style="{ boxShadow: '1px 1px 3px black'}">
        <h3 :style="{ paddingBottom: '18px', borderBottom: '2px solid #ccc', fontSize: '20px'}" > &nbsp;&nbsp; 최근글</h3>
        <ul class="recent-list">
          <li v-for="post in recentPosts" :key="post.username" @click="openPost(post)">
            <span class="recent-title" :style="{ fontFamily: 'SCDream5'}">{{ post.title }}</span>
            <span class="recent-date">{{ post.date }}</span>
          </li>
        </ul>
      </div>
    </aside>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getReviews, getReviewDetail, deleteReview } from '@/api.js';

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

// 추천 상태
const isRecommended = ref(false);

// 목록 로직
async function loadReviews() {
  // 상세 보기 모드일 때는 목록 로딩을 건너뜁니다.
  if (username.value) return;

  loading.value = true;
  error.value = null;

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


function doSearch() {
  page.value = 1;
  loadReviews();
}


function handleSearchAndBlur(event) {
  doSearch();
  if (event && event.currentTarget) {
    event.currentTarget.blur();
  }
}


// 목록 항목 클릭 시 상세 보기로 전환
function openPost(p) {
  // 'reviewDetail' 라우트로 이동하여 URL 파라미터를 변경
  router.push({ name: 'reviewDetail', params: { username: p.username } }).catch(()=>{});
}


// 상세 보기 로직
async function loadPost(id) {
  loading.value = true;
  error.value = null;
  try {
    const data = await getReviewDetail(id);
    post.value = data.data || data || {
      username: id,
      title: `샘플 게시물 제목 ${id}`,
      author: '운영자1',
      date: '25/11/24',
      views: 123,
      tag: id.length % 3 === 0 ? '정보' : '질문',
      content: '이것은 샘플 게시물의 본문입니다.'
    };
  } catch (err) {
    console.error('게시물 로드 실패:', err);
    error.value = err.message;
    // 폴백
    post.value = {
      username: id,
      title: `샘플 게시물 제목 ${id}`,
      author: '운영자1',
      date: '25/11/24',
      views: 123,
      tag: id.length % 3 === 0 ? '정보' : '질문',
      content: '백엔드 연동 중입니다.'
    };
  } finally {
    loading.value = false;
  }
}

// 💡 추천 토글 함수 추가
async function toggleRecommend() {
  if (!post.value) return;

  // 1. UI 즉시 업데이트 (낙관적 업데이트)
  const isCurrentlyRecommended = isRecommended.value;
  isRecommended.value = !isCurrentlyRecommended;
  post.value.recommend += isCurrentlyRecommended ? -1 : 1;

  const action = isRecommended.value ? '추천' : '추천 취소';

  try {
    // 2. 서버에 추천 상태 변경 요청
    // 실제 API: await toggleRecommendApi(post.value.username, !isCurrentlyRecommended);
    console.log(`[Mock API] ${action} 요청: Post ${post.value.username}`);
    // 성공 시: 그대로 유지
    alert(`${action}되었습니다! (현재 추천 수: ${post.value.recommend})`);

  } catch (err) {
    // 3. 실패 시 상태 롤백 (롤백 로직)
    isRecommended.value = isCurrentlyRecommended;
    post.value.recommend += isCurrentlyRecommended ? 1 : -1;
    alert('추천/추천 취소에 실패했습니다: ' + err.message);
    console.error('추천 토글 실패:', err);
  }
}

// 상세 보기에서 목록으로 돌아가기 (라우트 파라미터 제거)
function goBack() {
  router.push({ name: 'reviews' }).catch(()=>{});
}

function goToEdit() {
  if (post.value) router.push({ name: 'reviewsWrite', params: { username: post.value.username } }).catch(()=>{});
}

async function deletePost() {
  if (!post.value || !confirm('정말 삭제하시겠습니까?')) return;
  try {
    await deleteReview(post.value.username);
    alert('삭제되었습니다.');
    router.push({ name: 'reviews' }).catch(()=>{});
  } catch (err) {
    alert('삭제 실패: ' + err.message);
    console.error('삭제 실패:', err);
  }
}

// 💡 Computed 속성 (목록용)
const filteredPosts = computed(() => {
  let result = posts.value;

  // 카테고리 필터
  if (selectedCategory.value !== 'all') {
    const categoryMap = { question: '질문', info: '정보', review: '리뷰', chat: '잡담' };
    const cat = categoryMap[selectedCategory.value];
    if (cat) result = result.filter(p => p.tag === cat);
  }

  // 검색 필터
  if (search.value.query) {
    const q = search.value.query.toLowerCase();
    result = result.filter(p => {
      const titleMatch = (p.title || '').toLowerCase().includes(q);
      const authorMatch = (p.author || '').toLowerCase().includes(q);
      if (search.value.type === 'title') return titleMatch;
      if (search.value.type === 'author') return authorMatch;
      return titleMatch || authorMatch;
    });
  }

  return result;
});

const trendingPosts = computed(() => {
  // 추천수(recommend) 기반으로 정렬
  return [...posts.value].sort((a, b) => (b.recommend || 0) - (a.recommend || 0)).slice(0, 5);
});

const recentPosts = computed(() => {
  // 최신 순서(no 또는 date)로 정렬
  return [...posts.value].slice(0, 5);
});


const pagedPosts = computed(() => {
  const start = (page.value - 1) * pageSize;
  return filteredPosts.value.slice(start, start + pageSize);
});

// Watch 및 Mounted (모드 전환 로직)

// 1. 검색 조건이 바뀌면 목록을 다시 로드 (상세 모드가 아닐 때만)
watch(search, () => {
  if (!username.value) doSearch();
}, { deep: true });

// 2. **핵심**: 라우트 파라미터 변경을 감시하여 상세 보기/목록 보기 전환 처리
watch(username, (newUsername) => {
  if (newUsername) {
    loadPost(newUsername); // 상세 보기 로드
  } else {
    post.value = null; // 상세 내용 초기화
    loadReviews(); // 목록 로드 (URL 변경을 통한 목록으로 돌아가기 처리)
  }
}, { immediate: true }); // 컴포넌트 마운트 시 초기 파라미터 검사
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

    // 글쓰기 버튼 스타일
    .write-btn {
      display: block;
      margin-top: 1rem;
      margin-left: auto;

      background-color: map.get($colors, 'black');
      color: map.get($colors, 'white');
      border: 1px solid map.get($colors,'black');
      font-size: 1rem;
      padding: 0.3rem 0.8rem;
      cursor: pointer;
      transition: background-color 0.1s ease;
      border-radius:4px;

      &:hover {
        background-color: map.get($colors, 'dark');
        color: map.get($colors, 'white');
      }
    }


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

      .search-area { display:flex; gap:0.5rem; align-items:center; cursor: pointer; position: relative;

          background-color: map.get($colors,'white');
          padding: 0.6rem; border-radius:4px;
          border:1px solid map.get($colors,'border');
        .search-btn {

          font-weight: bold;
          transition: background-color 0.1s ease;

          &:focus {
            outline: none;
            color: map.get($colors, 'gray-hover');
          }
        }
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

      .pagination {
        cursor: default;
        display:flex;
        justify-content:center;
        align-items:center;
        gap:1rem;
        margin-top:1rem;
      }
    }
  }

    // 상세 보기 전용 스타일
    .review-detail-wrapper {
      position: relative;

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
    .category-box, .trending-box, .recent-box, .login-panel, .ad-box {
      background: map.get($colors,'white');
      padding: map.get($spacing,'md');
      border-radius: map.get($radius,'md');
      box-shadow: map.get($shadows,'sm');

      h3, h4 { margin: 10px 0 0.8rem 0; font-size: 1rem; }
    }

    .category-list, .trending-list, .recent-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 0.5rem 0;
        border-bottom: 1px solid map.get($colors,'border');

        &:last-child { border-bottom: none; }

        a {
          color: map.get($colors,'dark');
          text-decoration: none;
          cursor: pointer;

          &:hover { color: map.get($colors,'black'); font-weight: bold; }
        }
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

    .ad-placeholder { height: 150px;
      background: map.get($colors,'light');
      display:flex; align-items:center; justify-content:center;
      color: map.get($colors,'muted');
    }
    .side-btn { @include button-base; width:100%; }
  }
}
</style>
