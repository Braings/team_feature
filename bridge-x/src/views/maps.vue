<template>
  <div class="map-wrapper">
    <div id="map-container" style="width: 100%; position: relative;">
      <strong>지도</strong>
      <!-- SVG 지도 -->
      <div class="svg-wrapper" v-html="svgContent" @click="handleMapClick"></div>

      <!-- 드롭다운 메뉴 -->
      <div v-if="showDropdown" class="dropdown-menu" :style="dropdownStyle">
        <button @click="closeDropdown" class="close-btn">×</button>
        <h3>{{ selectedRegion }}</h3>
        <div class="city-list">
          <button
            v-for="city in regionCities[selectedRegion]"
            :key="city"
            @click="selectCity(city)"
            class="city-item"
          >
            {{ city }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import svgContent from '@/img/maps/korea_map.svg?raw'

// 상태 관리
const showDropdown = ref(false)
const selectedRegion = ref('')
const selectedCity = ref('')
const dropdownPosition = reactive({ top: '0px', left: '0px' })

// 드롭다운 스타일 계산
const dropdownStyle = computed(() => ({
  top: dropdownPosition.top,
  left: dropdownPosition.left
}))

// 지역별 도시/구 데이터
const regionCities = reactive({
  '서울': ['강남구', '강동구', '강서구', '관악구', '광진구', '구로구', '금천구', '노원구', '도봉구', '동대문구', '동작구', '마포구', '서대문구', '서초구', '성동구', '성북구', '송파구', '양천구', '영등포구', '용산구', '은평구', '종로구', '중구', '중랑구'],
  '경기': ['수원시', '성남시', '안양시', '안산시', '용인시', '평택시', '화성시', '남양주시', '오산시', '시흥시', '군포시', '의왕시', '하남시', '이천시', '광명시', '광주시', '여주시', '양평군', '포천시', '연천군', '가평군', '김포시', '파주시', '이평도시'],
  '인천': ['중구', '동구', '남구', '연수구', '남동구', '부평구', '계양구', '서구', '강화군', '옹진군'],
  '강원': ['춘천시', '원주시', '강릉시', '동해시', '태백시', '속초시', '삼척시', '홍천군', '횡성군', '영월군', '평창군', '정선군', '철원군', '화천군', '양구군', '인제군', '고성군', '양양군'],
  '충북': ['청주시', '충주시', '제천시', '보은군', '옥천군', '영동군', '증평군', '진천군', '괴산군'],
  '충남': ['천안시', '공주시', '보령시', '아산시', '서산시', '논산시', '계룡시', '당진시', '금산군', '부여군', '서천군', '청양군', '홍성군', '예산군', '태안군'],
  '전북': ['전주시', '군산시', '익산시', '정읍시', '남원시', '김제시', '완주군', '진안군', '무주군', '장수군', '임실군', '순창군', '고창군', '부안군'],
  '전남': ['목포시', '여수시', '순천시', '나주시', '광양시', '담양군', '곡성군', '구례군', '고흥군', '보성군', '화순군', '장흥군', '강진군', '해남군', '영암군', '무안군', '함평군', '영광군', '장성군', '완도군', '진도군', '신안군'],
  '경북': ['포항시', '경주시', '김천시', '안동시', '구미시', '영주시', '영천시', '상주시', '문경시', '칠곡군', '군위군', '의성군', '청송군', '영양군', '영덕군', '청도군', '고령군', '성주군', '달성군', '남구'],
  '경남': ['창원시', '진주시', '통영시', '사천시', '김해시', '밀양시', '거제시', '양산시', '의령군', '함안군', '창녕군', '고성군', '남해군', '하동군', '산청군', '거창군', '합천군'],
  '부산': ['중구', '서구', '동구', '영도구', '부산진구', '동래구', '남구', '북구', '강서구', '금정구', '연제구', '수영구', '사상구', '기장군'],
  '울산': ['중구', '남구', '동구', '북구', '울주군'],
  '광주': ['동구', '서구', '남구', '북구', '광산구'],
  '대전': ['동구', '중구', '서구', '유성구', '대덕구'],
  '대구': ['중구', '동구', '서구', '남구', '북구', '수성구', '달서구', '달성군'],
  '세종': ['세종시'],
  '제주': ['제주시', '서귀포시']
})

// 드롭다운 닫기
const closeDropdown = () => {
  showDropdown.value = false
}

// 도시 선택
const selectCity = (city) => {
  selectedCity.value = city
  // 주석: 여기서 선택된 지역과 도시 정보를 활용하여 다음 단계로 진행하거나
  // 부모 컴포넌트로 데이터를 전달할 수 있습니다.
  // 예: emit('region-selected', { region: selectedRegion.value, city: selectedCity.value })
  closeDropdown()
}

// SVG 지도 클릭 이벤트 처리
const handleMapClick = (event) => {
  const target = event.target
  const regionGroup = target.closest('[data-name]')

  if (regionGroup) {
    const region = regionGroup.getAttribute('data-name')
    // SVG 컨테이너의 위치 기준으로 드롭다운 위치 계산
    const svgContainer = document.querySelector('.svg-wrapper')
    const containerRect = svgContainer.getBoundingClientRect()

    // 마우스 위치 (클릭 위치 기준)
    const mouseX = event.clientX - containerRect.left
    const mouseY = event.clientY - containerRect.top

    // 드롭다운 위치를 마우스 바로 옆에 고정 (오른쪽 10px, 위쪽과 같은 높이)
    dropdownPosition.top = mouseY + 'px'
    dropdownPosition.left = (mouseX + 10) + 'px'

    selectedRegion.value = region
    selectedCity.value = ''
    showDropdown.value = true
  }
}

// SVG 지도 초기화 (마운트 시 실행)
const initializeMap = () => {
  // 주석: SVG가 렌더링된 후 스타일 적용
  const svgElement = document.querySelector('.svg-wrapper svg')
  if (svgElement) {
    svgElement.style.width = '100%'
    svgElement.style.maxWidth = '800px'
    svgElement.style.cursor = 'pointer'
  }
}

// 컴포넌트 마운트 시 지도 초기화
onMounted(() => {
  initializeMap()
})
</script>

<style lang="scss" scoped>
.map-wrapper {
  width: 100%;
  padding: 20px;
}

.svg-wrapper {
  width: 100%;
  max-width: 900px;
  margin: 0 auto;
  position: relative;

  :deep(svg) {
    width: 100%;
    height: auto;
    cursor: pointer;
    display: block;

    g[data-name] {
      transition: opacity 0.2s ease;

      &:hover {
        opacity: 0.7;
      }
    }

    path, polygon {
      transition: fill 0.2s ease;
    }
  }
}

.dropdown-menu {
  position: absolute;
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  padding: 16px;
  min-width: 200px;
  max-height: 400px;
  overflow-y: auto;
  z-index: 1000;

  h3 {
    margin: 0 0 12px 0;
    font-size: 16px;
    color: #333;
  }

  .close-btn {
    position: absolute;
    top: 8px;
    right: 8px;
    background: none;
    border: none;
    font-size: 20px;
    cursor: pointer;
    color: #999;

    &:hover {
      color: #333;
    }
  }
}

.city-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.city-item {
  padding: 8px 12px;
  background: #f5f5f5;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  color: #333;
  transition: all 0.2s ease;

  &:hover {
    background: #4CAF50;
    color: white;
    border-color: #4CAF50;
  }

  &:active {
    transform: scale(0.98);
  }
}
</style>
