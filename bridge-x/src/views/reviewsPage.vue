
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
          <div :class="['tab', selectedCategory === 'review' ? 'active' : '']" @click="selectCategory('review')">리뷰</div>
          <div :class="['tab', selectedCategory === 'chat' ? 'active' : '']" @click="selectCategory('chat')">잡담</div>
        </div>


        <div class="search-area">
          <select v-model="search.type">
            <option value="all">전체</option>
            <option value="title">제목</option>
            <option value="author">작성자</option>
          </select>
          <input v-model="search.query" placeholder="검색어 입력" />
          <button class="search-btn" @click=handleSearchAndBlur($event)>검색</button>
        </div>
      </section>

      <section class="post-list">
        <table class="posts-table">
          <thead :style="{ borderBottom: '3px solid #ccc' }">
            <tr>
              <th class="col-no">번호</th>
              <th class="col-tag">말머리</th>
              <th class="col-title" :style="{ textAlign: 'center' }" >제목</th>
              <th class="col-author">글쓴이</th>
              <th class="col-date">작성일</th>
              <th class="col-views">조회</th>
              <th class="col-rec">추천</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(post) in pagedPosts" :key="post.username" @click="openPost(post)">
              <td class="col-no">{{ post.no }}</td>
              <td class="col-tag">{{ post.tag }}</td>
              <td class="col-title">{{ post.title }}</td>
              <td class="col-author">{{ post.author }}</td>
              <td class="col-date">{{ post.date }}</td>
              <td class="col-views">{{ post.views }}</td>
              <td class="col-rec">{{ post.recommend }}</td>
            </tr>
          </tbody>
        </table>

        <div class="pagination">
          <button @click="prevPage" :disabled="page === 1">이전</button>
          <span> {{ page }} / {{ totalPages }} </span>
          <button @click="nextPage" :disabled="page === totalPages">다음</button>


        </div>
        <div class="gallery-actions">
          <button class="write-btn" @click="goToWrite">글쓰기</button>
        </div>

      </section>

    </div>

    <aside class="gallery-side">
      <div class="category-padding"></div>
      <div class="category-padding"></div>
      <div class="category-padding"></div>
      <div class="category-padding"></div>
      <div class="category-padding"></div>

      <div class="trending-box">
        <h3 :style="{ paddingBottom: '18px', borderBottom: '2px solid #ccc' }" >인기글</h3>
        <ul class="trending-list">
          <li v-for="post in trendingPosts" :key="post.username" @click="openPost(post)">
            <span class="trending-title">{{ post.title }}</span>
            <span class="trending-count">{{ post.recommend }}</span>
          </li>
        </ul>
      </div>

      <div class="category-padding"></div>

      <div class="recent-box">
        <h3 :style="{ paddingBottom: '18px', borderBottom: '2px solid #ccc' }" >최근글</h3>
        <ul class="recent-list">
          <li v-for="post in recentPosts" :key="post.username" @click="openPost(post)">
            <span class="recent-title">{{ post.title }}</span>
            <span class="recent-date">{{ post.date }}</span>
          </li>
        </ul>
      </div>
    </aside>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getReviews } from '@/api.js';

const router = useRouter();

const goToWrite = () => router.push({ name: 'reviewsWrite' });

const search = ref({ type: 'all', query: '' });
const selectedCategory = ref('all');
const posts = ref([]);
const loading = ref(false);
const error = ref(null);

const page = ref(1);
const pageSize = 10;

async function loadReviews() {
  loading.value = true;
  error.value = null;
  try {
    const data = await getReviews({ page: page.value, limit: pageSize });
    posts.value = data.data || data || [];
  } catch (err) {
    console.error('리뷰 목록 로드 실패:', err);
    error.value = err.message;
    // 폴백: 샘플 데이터
    posts.value = Array.from({ length: 37 }).map((_, i) => ({
      username: `author${i + 1}`,
      no: 37 - i,
      tag: i % 5 === 0 ? '질문' : i % 3 === 0 ? '정보' : '',
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
}

function handleSearchAndBlur(event) {
  // 1. 실제 검색 작업 수행
  doSearch();

  // 2. 포커스 해제 (blur)
  // event.currentTarget은 이벤트가 바인딩된 요소를 가리킵니다.
  if (event && event.currentTarget) {
    event.currentTarget.blur();
    console.log('버튼 포커스 해제됨');
  }
}
function selectCategory(cat) {
  selectedCategory.value = cat;
  page.value = 1;
}

function openPost(post) {
  if (router) router.push({ name: 'reviewDetail', params: { username: post.username } }).catch(()=>{});
}

function prevPage() { if (page.value > 1) { page.value -= 1; loadReviews(); } }
function nextPage() { if (page.value < totalPages.value) { page.value += 1; loadReviews(); } }

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
      if (search.value.type === 'title') return p.title.toLowerCase().includes(q);
      if (search.value.type === 'author') return p.author.toLowerCase().includes(q);
      return p.title.toLowerCase().includes(q) || p.author.toLowerCase().includes(q);
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

onMounted(() => {
  loadReviews();
});
</script>

<style lang="scss" scoped>
@use 'sass:map';
@use '@/styles/_variables' as *;


.gallery-page {
  display: flex;
  gap: 2vw;
  padding: 2vh 3vw;
  font-family: 'CC', sans-serif;
  background-color: map.get($colors, 'table');
  .gallery-main {
    flex: 1 1 auto;

    .gallery-actions{
      position: relative;
      bottom: 25px;
      right: 5px;
      text-align:right;
      .write-btn {
        background-color: map.get($colors, 'black');
        color: map.get($colors, 'white');
        border: 1px solid map.get($colors,'black');
        font-size: 0.8rem;
        padding: 0.3rem 0.8rem;
        cursor: pointer;
        transition: background-color 0.1s ease;
        border-radius:4px;

        &:hover {
          background-color: map.get($colors, 'dark');
          color: map.get($colors, 'white');
        }
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

      .gallery-title { font-size: 1.6rem; margin: 0; }
      .gallery-sub { color: map.get($colors, 'muted'); margin: 0; }

    }

    .controls {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: map.get($spacing, 'md');

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

      .search-area { display:flex; gap:0.5rem; align-items:center;
        select, input {
          background-color: map.get($colors,'white');
          padding: 0.6rem; border-radius:4px;
          border:1px solid map.get($colors,'border');
        }
        .search-btn {
          background-color: map.get($colors,'white');
          padding: 0.6rem; border-radius:4px;
          border:1px solid map.get($colors,'border');

          cursor: pointer;
          font-weight: bold;
          transition: background-color 0.1s ease;

          &:focus {
            outline: none;
            background-color: map.get($colors, 'gray-hover');
            color: map.get($colors, 'white');
          }
        }
      }
    }

    .post-list { margin-top: map.get($spacing, 'md');
      .posts-table { width: 100%; border-collapse: collapse;
        background: map.get($colors,'white');
        border-radius: map.get($radius,'sm');
        overflow: hidden;
        box-shadow: map.get($shadows,'xs');

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
        .col-author { width:10%; }
        .col-date { width:10%; }
        .col-views, .col-rec { width:8%; text-align:center; }
      }

      .pagination { display:flex; justify-content:center; align-items:center; gap:1rem; margin-top:1rem; }
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
