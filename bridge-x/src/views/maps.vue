<template>
  <div class="map-page">
    <div class="map-container">
      <h1 class="map-title">지역 선택</h1>

      <div class="map-wrapper">
        <img src="@/img/maps/korea.jpg" alt="Korea Map" class="korea-map" />

        <!-- SVG 오버레이: 각 지역을 클릭 가능하게 함 -->
        <svg class="map-overlay" viewBox="0 0 1000 1000">
          <!-- 서울 (대략 좌표) -->
          <rect x="480" y="200" width="80" height="80" class="region" data-region="seoul" @click="openDropdown('seoul')" />

          <!-- 경기도 -->
          <polygon points="400,150 560,150 600,250 400,280" class="region" data-region="gyeonggi" @click="openDropdown('gyeonggi')" />

          <!-- 인천 -->
          <circle cx="350" cy="220" r="50" class="region" data-region="incheon" @click="openDropdown('incheon')" />

          <!-- 강원도 -->
          <polygon points="560,150 700,200 720,400 550,380" class="region" data-region="gangwon" @click="openDropdown('gangwon')" />

          <!-- 충청북도 -->
          <polygon points="480,320 600,300 620,420 480,440" class="region" data-region="chungbuk" @click="openDropdown('chungbuk')" />

          <!-- 충청남도 -->
          <polygon points="380,350 480,340 500,500 350,520" class="region" data-region="chungnam" @click="openDropdown('chungnam')" />

          <!-- 대전 -->
          <circle cx="420" cy="380" r="40" class="region" data-region="daejeon" @click="openDropdown('daejeon')" />

          <!-- 전라북도 -->
          <polygon points="280,480 420,440 450,620 280,640" class="region" data-region="jeonbuk" @click="openDropdown('jeonbuk')" />

          <!-- 전라남도 -->
          <polygon points="200,600 420,580 450,800 150,820" class="region" data-region="jeonnam" @click="openDropdown('jeonnam')" />

          <!-- 광주 -->
          <circle cx="300" cy="680" r="40" class="region" data-region="gwangju" @click="openDropdown('gwangju')" />

          <!-- 경상북도 -->
          <polygon points="620,380 750,350 800,600 620,640" class="region" data-region="gyeongbuk" @click="openDropdown('gyeongbuk')" />

          <!-- 대구 -->
          <circle cx="700" cy="520" r="45" class="region" data-region="daegu" @click="openDropdown('daegu')" />

          <!-- 경상남도 -->
          <polygon points="620,620 800,580 850,800 600,820" class="region" data-region="gyeongnam" @click="openDropdown('gyeongnam')" />

          <!-- 부산 -->
          <circle cx="800" cy="750" r="50" class="region" data-region="busan" @click="openDropdown('busan')" />

          <!-- 울산 -->
          <circle cx="780" cy="650" r="35" class="region" data-region="ulsan" @click="openDropdown('ulsan')" />

          <!-- 제주도 -->
          <ellipse cx="450" cy="900" rx="70" ry="60" class="region" data-region="jeju" @click="openDropdown('jeju')" />
        </svg>
      </div>

      <!-- 드롭다운 메뉴 -->
      <div v-if="selectedRegion" class="dropdown-menu" :style="dropdownPosition">
        <div class="dropdown-header">
          <h3>{{ regionNames[selectedRegion] }}</h3>
          <button class="close-btn" @click="closeDropdown">✕</button>
        </div>
        <ul class="dropdown-list">
          <li v-for="city in regionCities[selectedRegion]" :key="city" @click="selectCity(city)">
            {{ city }}
          </li>
        </ul>
      </div>

      <!-- 선택된 지역 표시 -->
      <div v-if="selectedCity" class="selected-info">
        <p>선택된 지역: <strong>{{ selectedCity }}</strong></p>
        <button @click="resetSelection">초기화</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// ========================
// State
// ========================
const selectedRegion = ref(null);
const selectedCity = ref(null);
const dropdownPosition = ref({ top: '0px', left: '0px' });

// ========================
// Region Data
// ========================
const regionNames = {
  seoul: '서울',
  gyeonggi: '경기도',
  incheon: '인천',
  gangwon: '강원도',
  chungbuk: '충청북도',
  chungnam: '충청남도',
  daejeon: '대전',
  jeonbuk: '전라북도',
  jeonnam: '전라남도',
  gwangju: '광주',
  gyeongbuk: '경상북도',
  daegu: '대구',
  gyeongnam: '경상남도',
  busan: '부산',
  ulsan: '울산',
  jeju: '제주도'
};

const regionCities = {
  seoul: ['강남구', '강동구', '강북구', '강서구', '관악구', '광진구', '구로구', '금천구', '노원구', '도봉구', '동대문구', '동작구', '서대문구', '서초구', '성동구', '성북구', '송파구', '양천구', '영등포구', '용산구', '중구', '중랑구'],
  gyeonggi: ['수원', '성남', '안양', '안산', '용인', '버스', '의정부', '하남', '오산', '파주', '이천', '안성', '김포', '화성', '광주', '포천', '여주', '연천', '가평', '양평'],
  incheon: ['중구', '동구', '미추홀구', '연수구', '남동구', '부평구', '계양구', '서구'],
  gangwon: ['춘천', '원주', '강릉', '동해', '태백', '속초', '삼척', '홍천', '횡성', '영월', '평창', '정선', '인제', '고성', '양양'],
  chungbuk: ['청주', '충주', '제천', '보은', '옥천', '영동', '증평', '진천', '괴산', '음성', '단양'],
  chungnam: ['천안', '공주', '보령', '아산', '서산', '논산', '계룡', '당진', '금산', '예산', '홍성', '부여', '청양'],
  daejeon: ['동구', '중구', '서구', '유성구', '대덕구'],
  jeonbuk: ['전주', '군산', '익산', '정읍', '남원', '김제', '완주', '진안', '무주', '장수', '임실', '순창', '고창', '부안'],
  jeonnam: ['목포', '여수', '순천', '나주', '광양', '담양', '곡성', '구례', '고흥', '보성', '화순', '장흥', '강진', '해남', '영암', '무안', '함평', '영광', '장성', '완도', '진도', '신안'],
  gwangju: ['동구', '서구', '남구', '북구', '광산구'],
  gyeongbuk: ['포항', '경주', '김천', '안동', '구미', '영천', '상주', '문경', '경산', '군위', '의성', '청송', '영양', '영덕', '청도', '고령', '성주', '칠곡'],
  daegu: ['중구', '동구', '서구', '남구', '북구', '수성구', '달서구'],
  gyeongnam: ['창원', '진주', '통영', '사천', '김해', '밀양', '거제', '남해', '하동', '산청', '함양', '거창', '합천'],
  busan: ['중구', '서구', '동구', '영도구', '부산진구', '동래구', '남구', '북구', '강서구', '연제구', '수영구', '사하구', '금정구', '장안구'],
  ulsan: ['중구', '남구', '북구', '동구'],
  jeju: ['제주시', '서귀포시']
};

// ========================
// Methods
// ========================
const openDropdown = (region) => {
  selectedRegion.value = region;
  // 클릭 위치 근처에 드롭다운 표시
  dropdownPosition.value = {
    top: `${Math.random() * 300 + 200}px`,
    left: `${Math.random() * 400 + 100}px`
  };
};

const closeDropdown = () => {
  selectedRegion.value = null;
};

const selectCity = (city) => {
  selectedCity.value = city;
  closeDropdown();
};

const resetSelection = () => {
  selectedCity.value = null;
  selectedRegion.value = null;
};
</script>

<style lang="scss" scoped>
// ========================
// Variables
// ========================
$primary-color: #4CAF50;
$bg-light: #f5f5f5;
$border-color: #ddd;
$text-color: #333;
$hover-color: #45a049;

// ========================
// Styles
// ========================
.map-page {
  width: 100vw;
  min-height: 100vh;
  background-color: $bg-light;
  padding: 20px;
  box-sizing: border-box;
}

.map-container {
  max-width: 1200px;
  margin: 0 auto;
  background-color: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.map-title {
  text-align: center;
  font-size: 2rem;
  margin-bottom: 30px;
  color: $text-color;
}

.map-wrapper {
  position: relative;
  width: 100%;
  max-width: 800px;
  margin: 0 auto 30px;
  background-color: #f9f9f9;
  border: 2px solid $border-color;
  border-radius: 8px;
  overflow: hidden;
}

.korea-map {
  width: 100%;
  height: auto;
  display: block;
}

.map-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;

  .region {
    fill: transparent;
    stroke: transparent;
    transition: all 0.2s ease;

    &:hover {
      fill: rgba(76, 175, 80, 0.2);
      stroke: $primary-color;
      stroke-width: 2;
    }
  }
}

.dropdown-menu {
  position: fixed;
  background-color: white;
  border: 2px solid $primary-color;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 100;
  min-width: 250px;
  max-height: 400px;
  overflow-y: auto;
}

.dropdown-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: $primary-color;
  color: white;
  border-bottom: 1px solid $border-color;

  h3 {
    margin: 0;
    font-size: 1.2rem;
  }
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0;
  line-height: 1;

  &:hover {
    opacity: 0.8;
  }
}

.dropdown-list {
  list-style: none;
  margin: 0;
  padding: 0;

  li {
    padding: 12px 15px;
    border-bottom: 1px solid $border-color;
    cursor: pointer;
    transition: background-color 0.2s ease;

    &:hover {
      background-color: rgba(76, 175, 80, 0.1);
    }

    &:last-child {
      border-bottom: none;
    }
  }
}

.selected-info {
  text-align: center;
  padding: 20px;
  background-color: rgba(76, 175, 80, 0.1);
  border: 2px solid $primary-color;
  border-radius: 8px;
  margin-top: 20px;

  p {
    margin: 0 0 15px 0;
    font-size: 1.1rem;
    color: $text-color;

    strong {
      color: $primary-color;
      font-weight: bold;
    }
  }

  button {
    padding: 10px 20px;
    background-color: $primary-color;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 1rem;
    transition: background-color 0.2s ease;

    &:hover {
      background-color: $hover-color;
    }
  }
}
</style>
