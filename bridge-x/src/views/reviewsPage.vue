
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

        <div class="tabs">
          <div class="padding"></div>
          <div :class="['tab', selectedCategory === 'all' ? 'active' : '']" @click="selectCategory('all')">전체글</div>
          <div :class="['tab', selectedCategory === 'question' ? 'active' : '']" @click="selectCategory('question')">질문</div>
          <div :class="['tab', selectedCategory === 'info' ? 'active' : '']" @click="selectCategory('info')">정보</div>
          <!-- <div :class="['tab', selectedCategory === 'review' ? 'active' : '']" @click="selectCategory('review')">리뷰</div> -->
          <div :class="['tab', selectedCategory === 'chat' ? 'active' : '']" @click="selectCategory('chat')">잡담</div>
        </div>


        <div class="search-area">
          <select v-model="search.type" class="select">
            <option value="all">전체</option>
            <option value="title">제목</option>
            <option value="nickname">작성자</option>
          </select>
          <input v-model="search.query" placeholder="검색어 입력" class="search-input"/>
          <button class="search-btn" @click=handleSearchAndBlur($event)>검색</button>
        </div>
      </section>

      <section class="post-list">
        <div v-if="loading" class="list-status">게시물을 로드하는 중...</div>
        <!-- <div v-else-if="error" class="list-status error">게시물을 로드하는 데 실패했습니다: **{{ error }}**</div> -->
        <div v-else-if="pagedPosts.length === 0" class="list-status no-posts">게시물이 없습니다.</div>

          <table v-else class="posts-table" :style="{  boxShadow: '1px 1px 3px black'}">
            <thead :style="{ borderBottom: '3px solid #ccc' }">
              <tr>
                <th class="col-no">번호</th>
                <th class="col-tag">태그</th>
                <th class="col-title" :style="{ textAlign: 'center' }" >제목</th>
                <th class="col-nickname">글쓴이</th>
                <th class="col-date">작성일</th>
                <th class="col-views">조회</th>
                <th class="col-rec">추천</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(post) in pagedPosts" :key="post.reviewID" :style="{ fontFamily: 'SCDream5'}"  @click="openPost(post)" >
                <td class="col-no">{{ post.id }}</td>
                <td class="col-tag">{{ getKoreanTag(post.tag) }}</td>
                <td class="col-title">{{ post.title }}</td>
                <td class="col-nickname">{{ post.nickname }}</td>
                <td class="col-date">{{ formatDate(post.creationTime) }}</td>
                <td class="col-views">{{ post.views }}</td>
                <td class="col-rec">{{ post.recommend }}</td>
              </tr>
            </tbody>
          </table>

          <div class="pagination" v-if="pagedPosts.length > 0" >
          <button @click="prevPage" :disabled="page === 1">이전</button>
          <span :style="{cursor: 'default'}"> {{ page }} / {{ totalPages }} </span>
          <button @click="nextPage" :disabled="page === totalPages">다음</button>
          <button class="write-btn" @click="openWriteModal">글쓰기</button>
          <ReviewWriteModal
            :isOpen="isModalOpen"
            @close="closeWriteModal"
            @submit-success="handleReviewSubmit"
            :initialData="defaultReviewData"
          />
        </div>

      </section>

    </div>

    <aside class="gallery-side">
      <div class="category-padding" :style="{ height: '9.8rem'}"></div>

      <div class="trending-box" :style="{  boxShadow: '1px 1px 3px black'}">
        <h3 :style="{ paddingBottom: '18px', borderBottom: '2px solid #ccc', fontSize: '20px' }" > &nbsp;&nbsp; 인기글</h3>
        <ul class="trending-list">
          <li v-for="post in trendingPosts" :key="post.reviewID" @click="openPost(post)">
            <span class="trending-title" :style="{ fontFamily: 'SCDream5'}">{{ post.title }}</span>
            <span class="trending-count">{{ post.recommend }}</span>
          </li>
        </ul>
      </div>

      <div class="category-padding"></div>

      <div class="recent-box" :style="{  boxShadow: '1px 1px 3px black'}">
        <h3 :style="{ paddingBottom: '18px', borderBottom: '2px solid #ccc', fontSize: '20px'}" > &nbsp;&nbsp; 최근글</h3>
        <ul class="recent-list">
          <li v-for="post in recentPosts" :key="post.reviewID" @click="openPost(post)">
            <span class="recent-title" :style="{ fontFamily: 'SCDream5'}">{{ post.title }}</span>
            <span class="recent-date">{{ post.date }}</span>
          </li>
        </ul>
      </div>
    </aside>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { getReviews } from '@/api.js';
import ReviewWriteModal from './reviewModal.vue';

const router = useRouter();

const search = ref({ type: 'all', query: '' });
const selectedCategory = ref('all');
const posts = ref([]);
const loading = ref(false);
const error = ref(null);

const page = ref(1);
const pageSize = 10;

// AddPoint : 매핑 추가
const tagKoreanMap = {
  'QUESTION': '질문',
  'TALK': '잡담',
  'INFORMATION': '정보',
};

// AddPoint : 태그 값을 받아 한글로 변환하는 함수
function getKoreanTag(tag) {
  return tagKoreanMap[tag] || tag;
}

// AddPoint : formatDate 추가
function formatDate(dateTimeStr) {
  if (!dateTimeStr) return '';
  return dateTimeStr.split('T')[0];
}
  
// ========================
// Data Loading
// ========================

async function loadReviews() {
  loading.value = true;
  error.value = null;
  try {
    const data = await getReviews({
      page: page.value,
      limit: pageSize,
      category: selectedCategory.value,
      searchType: search.value.type,
      query: search.value.query
    });

    posts.value = data.data || data || [];

  } catch (error) {
    console.error('리뷰 목록 로드 실패:', error);
    error.value = error.message;


    // 이 샘플 데이터는 어떤 필터링 조건에서도 항상 동일하게 전체를 생성해야 합니다.
    posts.value = Array.from({ length: 100  }).map((_, i) => ({
      reviewId: 100 - i,
      username: `nickname${i + 1}`,
      no: 100 - i,
      tag: i % 5 === 0 ? '질문' : i % 3 === 0 ? '정보' : '리뷰',
      title: `샘플 게시물 제목 ${i + 1}`,
      nickname: `운영자${(i % 6) + 1}`,
      date: '25/11/' + ((i % 30) + 1).toString().padStart(2, '0'),
      views: Math.floor(Math.random() * 500),
      recommend: Math.floor(Math.random() * 50)
    }));
  } finally {
    loading.value = false;
  }
}
// ========================
// Event Handlers
// ========================

function doSearch() {
  page.value = 1;
  loadReviews();
}

function handleSearchAndBlur(event) {
  doSearch();

  if (event && event.currentTarget) {
    event.currentTarget.blur();
    console.log('버튼 포커스 해제됨');
  }
}
function selectCategory(cat) {
  selectedCategory.value = cat;
  page.value = 1;
  loadReviews();
}

// reviewsPage.vue 의 openPost 함수 교체

function openPost(post) {
  // 1. 실제 데이터에 들어있는 ID 찾기 (안전 장치)
  // DB나 API에 따라 id, review_id, reviewId 중 하나로 들어옵니다.
  const realId = post.reviewID || post.reviewId || post.review_id || post.id;

  console.log('클릭한 게시글 데이터:', post); // F12 콘솔에서 확인 가능
  console.log('추출된 ID:', realId);

  // 2. ID가 없으면 경고 띄우고 중단
  if (!realId) {
    alert('게시글 ID를 찾을 수 없습니다. (데이터 오류)');
    return;
  }

  // 3. 페이지 이동 (reviewID로 통일)
  if (router) {
    router.push({
      name: 'reviewDetail',
      params: { reviewID: realId } // 라우터 설정(:reviewID)과 철자 100% 일치
    }).catch(err => {
      console.error("페이지 이동 에러:", err);
    });
  }
}
function prevPage() {
  if (page.value > 1) {
    page.value -= 1;
  }
}
function nextPage() {
  if (page.value < totalPages.value) {
    page.value += 1;
  }
}

// ========================
// Computed Properties
// ========================

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
      const nicknameMatch = (p.nickname || '').toLowerCase().includes(q);

      if (search.value.type === 'title') return titleMatch;
      if (search.value.type === 'nickname') return nicknameMatch;
      return titleMatch || nicknameMatch;
    });
  }

  return result;
});

const trendingPosts = computed(() => {
  return [...posts.value].sort((a, b) => (b.recommend || 0) - (a.recommend || 0)).slice(0, 5);
});

const recentPosts = computed(() => {
  return [...posts.value].slice(0, 5);
});

const totalPages = computed(() => Math.ceil(filteredPosts.value.length / pageSize));

const pagedPosts = computed(() => {
  const start = (page.value - 1) * pageSize;
  return filteredPosts.value.slice(start, start + pageSize);
});

watch(search, () => {
  doSearch();
}, { deep: true });

onMounted(() => {
  loadReviews();
});


// ========================
// Modal State and Handlers
// ========================

const isModalOpen = ref(false);

const defaultReviewData = reactive({ rating: 5, content: '' });

const openWriteModal = () => {
  isModalOpen.value = true;
};

const closeWriteModal = () => {
  isModalOpen.value = false;
};

const handleReviewSubmit = (newReview) => {
  console.log('새 리뷰가 작성되었습니다:', newReview);
  closeWriteModal();
  loadReviews();
};

</script>

<style lang="scss" scoped>
@use 'sass:color';
@use 'sass:map';
@use '@/styles/_variables' as *;


.gallery-page {
  display: flex;
  gap: 2vw;
  padding: 2vh 3vw;
  font-family: 'TheJamsilOTF6ExtraBold', sans-serif;
  background-color: map.get($colors, 'table');
  .gallery-main {
    flex: 1 1 auto;


  .write-btn {
      position: absolute;
      right: 0;
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
      justify-content: center;
      align-items: center;
      margin-left: 3rem;
      padding: map.get($spacing, 'md') map.get($spacing, 'lg');
      background: transparent;
      border-radius: map.get($radius, 'md');
      box-shadow: map.get($shadows, 'sm');

      .gallery-meta {
        display: flex;
        flex-direction: column;
      }

      .gallery-title { font-size: 2.5rem; margin: 0; text-align: center;}
      .gallery-sub { color: map.get($colors, 'muted'); margin: 0; }

    }

    .controls {
      position: relative;
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: map.get($spacing, 'md');
      bottom: 10px;

      .tabs { display: flex; gap: 0.5rem;
        .tab { @include button-base;
          background-color: map.get($colors,'white');
          border: 1px solid map.get($colors,'border');
          font-size: 0.8rem; padding: 0.3rem 0.8rem; cursor: pointer;
          transition: background-color 0.1s ease;
          border-radius:4px;

          &:hover {
            background-color: map.get($colors, 'gray-hover');
            color: map.get($colors, 'white');
          }
        }
        .tab.active {
          background: map.get($colors,'black');
          color: map.get($colors,'white');
        }
        .tab.padding {background: transparent; border: none; cursor: default;}
      }

      .search-area { display:flex; gap:0.5rem; align-items:center; cursor: pointer; position: relative;

        background-color: map.get($colors,'white');
        padding: 0.6rem; border-radius:4px;
        border:1px solid map.get($colors,'border');

        .search-input {
          border: none;
          outline: none;
          font-size: 0.9rem;
          width: 150px;
        }
        .select {
          text-align: center;
          border: none;
          outline: none;
          font-size: 0.9rem;
          padding: 0.1rem;
        }
        .search-btn {
          font-weight: bold;
          transition: background-color 0.1s ease;

          &:hover {
          box-shadow: map.get($shadows,'sm');
        }

        &:focus {
          outline: none;
          color: map.get($colors, 'gray-hover');
        }
        }
      }
    }

    .post-list { margin-top: map.get($spacing, 'md');
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

      .posts-table { width: 100%; border-collapse: collapse;
        background: map.get($colors,'white');
        border-radius: map.get($radius,'sm');
        overflow: hidden;
        box-shadow: map.get($shadows,'xs');
        position: relative;
        bottom: 12px;

        th, td { font-size: 0.9rem; padding: 0.9rem 1rem; text-align: center;
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
        .col-nickname { width:10%; }
        .col-date { width:10%; }
        .col-views, .col-rec { width:8%; text-align:center; }
      }

      .pagination { align-items: center; justify-content: center; position: relative; bottom: 5px; cursor: outo; display:flex; gap:1rem; margin-top:1rem; }
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
