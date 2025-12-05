<template>
  <div class="map-wrapper">
    <div class="header-gap"/>
    <div class="container">
      <!-- 왼쪽 섹션: 지도 -->

      <div class="map-section">
        <div class="map-container" style="position: relative;">
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

      <!-- 오른쪽 섹션: 선택된 정보 -->
      <div class="info-section">
        <div class="info-content">
          <div v-if="selectedCity" class="selected-info">
            <div class="info-item">
              <span class="value">{{ selectedRegion }} {{ selectedCity }}</span>
            </div>
            <div class="info-display">
              <p v-if="facilityListState.isLoading" class="loading-state">
                  <i class="fa fa-spinner fa-spin"></i>
                  {{ selectedRegion }} {{ selectedCity }} 정보를 불러오는 중...
              </p>
              <p v-else-if="facilityListState.hasError" class="error-state">
                  🚨 데이터를 불러오는 데 실패했습니다. 다시 시도해 주세요.
              </p>
              <div v-else-if="facilityListState.data.length > 0">
                  <h3>운동 시설 목록 (총 {{ facilityListState.data.length }}개)</h3>
                  <div class="facility-list-wrapper">
                    <ul class="facility-list">
                      <li @click="openFacilityModal(facility.id)" v-for="(facility) in facilityListState.data" :key="facility.id" class="info-item">
                        <span class="value"> {{ truncateText(facility.FCLTY_NM) }} </span>
                        <span class="value"> {{ truncateText(facility.INDUTY_NM) }} </span>
                        <span class="value"> {{ facility.RSPNSBLTY_TEL_NO }} </span>
                      </li>
                    </ul>
                  </div>
                </div>
              <p v-else>
                  {{ selectedRegion }} {{ selectedCity }}에 등록된 시설 정보가 없습니다.
              </p>
            </div>
             </div>
          <div v-else class="empty-state">
            <p>지도에서 지역을 선택하세요</p>
          </div>
        </div>
      </div>
    </div>
    <FacilityDetailModal
      :isOpen="isModalOpen"
      :facilityId="selectedFacilityId"
      @close="closeFacilityModal"
    />
  </div>


</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import svgContent from '@/img/maps/korea_map.svg?raw'
import { facilityListState, fetchExerciseFacilities, setSelectedRegionAndCity } from '@/stores/exerciseFacilitiesStore'
import FacilityDetailModal from './facilityDetailModal.vue'

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

// 드롭다운 닫기
const closeDropdown = () => {
  showDropdown.value = false
}

// 도시 선택
const selectCity = async (city) => {
  selectedCity.value = city
  setSelectedRegionAndCity(selectedRegion.value, city)

  // 드롭다운 닫기
  closeDropdown()

  try {
    await fetchExerciseFacilities(selectedRegion.value, city)
  } catch (error) {
    // Store에서 에러를 처리하지만, 컴포넌트에서도 추가적인 UI 처리가 필요하다면 catch 블록에 추가합니다.
    console.error("시설 조회 실패 (컴포넌트 레벨):", error);
  }
}

// SVG 지도 클릭 이벤트 처리
const handleMapClick = (event) => {
  const target = event.target;
  const regionGroup = target.closest('[data-name]');

  if (regionGroup) {
    // 1. SVG의 data-name 값 (약칭, 예: '충북')을 가져옵니다.
    const svgRegionName = regionGroup.getAttribute('data-name');

    // 2. 매핑 객체를 사용하여 정식 명칭으로 변환합니다. (예: '충청북도')
    const fullRegionName = regionNameMap[svgRegionName];

    // 3. 지역 데이터가 유효하지 않거나 이미 선택된 지역을 다시 클릭한 경우 처리
    if (!fullRegionName || !regionCities[fullRegionName]) {
      // 유효하지 않은 지역 경고 (기존 로직 유지)
      if (!regionCities[fullRegionName]) {
        console.warn(`[지도 클릭 경고] 매핑된 '${fullRegionName}'에 대한 시/군/구 데이터가 regionCities에 없습니다.`);
      }
      return;
    }

    // 4. 이전 선택 영역 스타일 제거
    // 이전에 선택되었던 지역의 DOM 요소를 찾아서 스타일을 초기화합니다.
    if (selectedRegion.value) {
      // 기존 선택된 지역의 data-name 약칭을 다시 찾아야 합니다.
      const prevSvgRegionName = Object.keys(regionNameMap).find(key => regionNameMap[key] === selectedRegion.value);

      if (prevSvgRegionName) {
        // SVG 내에서 이전 data-name을 가진 그룹 요소를 찾습니다.
        const prevRegionGroup = document.querySelector(`.svg-wrapper g[data-name="${prevSvgRegionName}"]`);

        if (prevRegionGroup && prevRegionGroup !== regionGroup) {
          const prevPaths = prevRegionGroup.querySelectorAll('path, polygon');
          prevPaths.forEach(el => {
            el.style.fill = ''; // 초기화
            el.style.stroke = '';
            el.style.strokeWidth = '';
          });
        }
      }
    }

    // 5. 새로운 영역 선택 및 변수 업데이트
    // 선택된 지역을 정식 명칭으로 업데이트하고 도시를 초기화합니다.
    if (selectedRegion.value !== fullRegionName) {
      selectedRegion.value = fullRegionName;
      selectedCity.value = '';
      setSelectedRegionAndCity(fullRegionName, ''); // 외부 함수 호출
    }

    // 6. 선택된 영역에 스타일 적용
    const paths = regionGroup.querySelectorAll('path, polygon');
    paths.forEach(el => {
      el.style.fill = '#4caf50';
      el.style.stroke = 'none';
    });

    // 7. 드롭다운 위치 계산 및 표시 로직 최적화
    const mapContainer = document.querySelector('.map-container');
    if (!mapContainer) return;

    const containerRect = mapContainer.getBoundingClientRect();

    let mouseX = event.clientX - containerRect.left;
    let mouseY = event.clientY - containerRect.top;

    // 드롭다운 크기 변수화
    const DROPDOWN_WIDTH = 250;
    const DROPDOWN_HEIGHT = 350;
    const PADDING = 10;

    // 오른쪽 여백 체크 및 위치 조정
    let newLeft = mouseX + PADDING;
    if (newLeft + DROPDOWN_WIDTH > containerRect.width) {
      newLeft = mouseX - DROPDOWN_WIDTH - PADDING;
      // 왼쪽으로 조정해도 컨테이너 밖으로 나가는 경우, 컨테이너 왼쪽 끝에 붙입니다.
      if (newLeft < PADDING) {
        newLeft = PADDING;
      }
    }

    // 아래쪽 여백 체크 및 위치 조정
    let newTop = mouseY;
    if (newTop + DROPDOWN_HEIGHT > containerRect.height) {
      newTop = mouseY - DROPDOWN_HEIGHT - PADDING;
      // 위로 조정해도 컨테이너 밖으로 나가는 경우, 컨테이너 상단 끝에 붙입니다.
      if (newTop < PADDING) {
        newTop = PADDING;
      }
    }


    // 드롭다운 위치 설정
    dropdownPosition.top = Math.max(0, newTop) + 'px';
    dropdownPosition.left = Math.max(0, newLeft) + 'px';

    showDropdown.value = true;
  }
};

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

const truncateText = (text, maxLength = 8) => {
  if (!text) return '';
  if (text.length > maxLength) {
    return text.substring(0, maxLength) + '...';
  }
  return text;
};

// ========================
// Modal State and Handlers
// ========================

// 1. 모달 상태 변수 추가
const isModalOpen = ref(false);
const selectedFacilityId = ref(null);


// 2. 모달 열기/닫기 함수
const openFacilityModal = (id) => {
  selectedFacilityId.value = id; // ID 저장
  isModalOpen.value = true;
};
const closeFacilityModal = () => {
  isModalOpen.value = false;
  selectedFacilityId.value = null; // 닫힐 때 ID 초기화
};


// 컴포넌트 마운트 시 지도 초기화
onMounted(() => {
  initializeMap()
})

// svg.data -> regionCities 맵핑
const regionNameMap = {
  '서울': '서울특별시',
  '부산': '부산광역시',
  '대구': '대구광역시',
  '인천': '인천광역시',
  '광주': '광주광역시',
  '대전': '대전광역시',
  '울산': '울산광역시',
  '세종': '세종특별자치시',
  '경기': '경기도',
  '강원': '강원특별자치도', // CSV 데이터에서 '강원특별자치도'로 추출되었습니다.
  '충북': '충청북도',
  '충남': '충청남도',
  '전북': '전라북도',
  '전남': '전라남도',
  '경북': '경상북도',
  '경남': '경상남도',
  '제주': '제주특별자치도',
};

// 지역별 도시/구 데이터
const regionCities = reactive({

  '강원특별자치도':
  [
    '강릉시',   '고성군',   '동해시',   '삼척시',   '속초시',   '양구군',   '양양군',
    '영월군',   '원주시',   '인제군',   '정선군',   '철원군',   '춘천시',   '태백시',
    '평창군',   '홍천군',   '화천군',   '횡성군',
  ],

  '경기도':
  [
    '고양시',   '과천시',   '광명시',   '광주시',   '구리시',   '군포시',   '김포시',
    '남양주시',   '동두천시',   '부천시',   '성남시',   '수원시',   '시흥시',   '안산시',
    '안성시',   '안양시',   '양주시',   '양평군',   '여주시',   '연천군',   '오산시',
    '용인시',   '의왕시',   '의정부시',   '이천시',   '파주시',   '평택시',   '포천시',
    '하남시',   '화성시',
  ],

  '경상남도':
  [
    '거제시',   '거창군',   '고성군',   '김해시',   '남해군',   '밀양시',   '사천시',
    '산청군',   '양산시',   '의령군',   '진주시',   '창녕군',   '창원시',   '창원시 마산합포구',
    '창원시 마산회원구',   '창원시 성산구',   '창원시 의창구',   '창원시 진해구',   '통영시',
    '하동군',   '함안군',   '함양군',   '합천군',
  ],

  '경상북도':
  [
    '경산시',   '경주시',   '고령군',   '구미시',   '김천시',   '문경시',   '봉화군',   '상주시',
    '성주군',   '안동시',   '영덕군',   '영양군',   '영주시',   '영천시',   '예천군',   '울릉군',
    '울진군',   '의성군',   '청도군',   '청송군',   '칠곡군',   '포항시',
  ],

  '광주광역시':
  [
    '광산구',   '남구',   '동구',   '북구',   '서구',
  ],

  '대구광역시':
  [
    '남구',   '달서구',   '달성군',   '동구',   '북구',   '서구',   '수성구',   '중구',
  ],

  '대전광역시':
  [
    '대덕구',   '동구',   '서구',   '유성구',   '중구',
  ],

  '부산광역시':
  [
    '강서구',   '금정구',   '기장군',   '남구',   '동구',   '동래구',   '부산진구',   '북구',
    '사상구',   '사하구',   '서구',   '수영구',   '연제구',   '영도구',   '중구',   '해운대구',
  ],

  '서울특별시':
  [
    '강남구',   '강동구',   '강북구',   '강서구',   '관악구',   '광진구',   '구로구',   '금천구',
    '노원구',   '도봉구',   '동대문구',   '동작구',   '마포구',   '서대문구',   '서초구',   '성동구',
    '성북구',   '송파구',   '양천구',   '영등포구',   '용산구',   '은평구',   '종로구',   '중구',   '중랑구',
  ],

  '세종특별자치시':
  [
    '세종특별자치시',
  ],

  '울산광역시':
  [
    '남구',   '동구',   '북구',   '울주군',   '중구',
  ],

  '인천광역시':
  [
    '강화군',   '계양구',   '남동구',   '동구',   '미추홀구',   '부평구',   '서구',   '연수구',   '옹진군',
    '중구',
  ],

  '전라남도':
  [
    '강진군',   '고흥군',   '곡성군',   '광양시',   '구례군',   '나주시',   '담양군',   '목포시',   '무안군',
    '보성군',   '순천시',   '신안군',   '여수시',   '영광군',   '영암군',   '완도군',   '장성군',   '장흥군',
    '진도군',   '함평군',   '해남군',   '화순군',
  ],

  '전라북도':
  [
    '고창군',   '군산시',   '김제시',   '남원시',   '무주군',   '부안군',   '순창군',   '완주군',   '익산시',
    '임실군',   '장수군',   '전주시',   '정읍시',   '진안군',
  ],

  '제주특별자치도':
  [
    '서귀포시',   '제주시',
  ],

  '충청남도':
  [
    '계룡시',   '공주시',   '금산군',   '논산시',   '당진시',   '보령시',   '부여군',   '서산시',   '서천군',
    '아산시',   '예산군',   '천안시',   '청양군',   '태안군',   '홍성군',
  ],

  '충청북도':
  [
    '괴산군',   '단양군',   '보은군',   '영동군',   '옥천군',   '음성군',   '제천시',   '증평군',   '진천군',
    '청주시',   '충주시',
  ],

})
</script>

<style lang="scss" scoped>
@use 'sass:color';
@use 'sass:map';

.header-gap {
  height: 10vh;
}
.map-wrapper {
  width: 100%;
  padding: 20px;
}

/* 시설 목록 스크롤 래퍼 스타일 */
.facility-list-wrapper {
  max-height: 400px; /* 원하는 최대 높이를 설정하세요. 이 높이를 넘어가면 스크롤이 생깁니다. */
  overflow-y: auto;  /* 세로 스크롤을 활성화합니다. */
  padding-right: 15px; /* 스크롤바 때문에 내용이 잘리는 것을 방지하기 위한 패딩 */
}

/* 목록 스타일 (선택사항: 가독성 향상) */
.facility-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.facility-list li {
  padding: 10px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.container {
  display: flex;
  gap: 30px;
  max-width: 1400px;
  margin: 0 auto;

  // 자식 항목의 높이를 동일하게 맞춥니다.
  align-items: stretch;
}
.map-section {
  flex: 1;
  min-width: 0;

  h2 {
    margin-top: 0;
    margin-bottom: 20px;
    font-size: 20px;
    color: #333;
  }
}

.map-container {
  width: 100%;
  aspect-ratio: 1;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

.svg-wrapper {
  width: 100%;
  height: 100%;
  position: relative;

  :deep(svg) {
    width: 90%;
    height: 90%;
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
  min-width: 180px;
  max-width: 280px;
  max-height: 350px;
  overflow-y: auto;
  z-index: 1000;
  pointer-events: auto;

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

.info-section {
  flex: 1;
  min-width: 0;

  h2 {
    margin-top: 0;
    margin-bottom: 20px;
    font-size: 20px;
    color: #333;
  }
}

.info-content {
  height: 100%;

  background: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;

  display: flex;
  align-items: center;
  justify-content: center;
}

.selected-info {
  width: 100%;
}

.info-item {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  padding: 12px;
  background: white;
  border-radius: 6px;
  border-left: 4px solid #4CAF50;

  .label {
    font-weight: 600;
    color: #555;
    min-width: 70px;
  }

  .value {
    color: #333;
    flex: 1;
  }
}

.info-display {
  margin-top: 20px;
  padding: 16px;
  background: white;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  text-align: center;

  p {
    margin: 0;
    color: #666;
    font-size: 14px;
    line-height: 1.5;
  }
}

.empty-state {
  text-align: center;
  color: #999;

  p {
    margin: 0;
    font-size: 16px;
  }
}

.info-display {
  margin-top: 20px;
  padding: 16px;
  background: white;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  // text-align: center; // 목록이 중앙 정렬되는 것을 방지하기 위해 주석 처리

  h3 {
    margin-top: 0;
    margin-bottom: 12px;
    font-size: 16px;
    color: #4CAF50;
    border-bottom: 2px solid #e0e0e0;
    padding-bottom: 8px;
  }

  p {
    margin: 0;
    color: #666;
    font-size: 14px;
    line-height: 1.5;
  }
}

// 💡 로딩 및 에러 상태 스타일
.loading-state {
  color: #2196F3;
  font-weight: bold;
  text-align: center;
}

.error-state {
  color: #F44336;
  font-weight: bold;
  text-align: center;
}

// 💡 시설 목록 스타일
.facility-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.facility-item {
  padding: 10px;
  border: 1px solid #f0f0f0;
  border-left: 4px solid #4CAF50;
  border-radius: 4px;
  background: #fafafa;
  display: flex;
  flex-direction: column;

  .facility-name {
    font-weight: bold;
    color: #333;
    margin-bottom: 4px;
    font-size: 15px;
  }

  .facility-address {
    font-size: 13px;
    color: #777;
  }
}

</style>
