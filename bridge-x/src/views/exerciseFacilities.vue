<template>
  <div class="map-wrapper">
    <div class="container">
      <!-- 왼쪽 섹션: 지도 -->
      <div class="map-section">
        <h2>지도</h2>
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
        <h2>선택된 지역</h2>
        <div class="info-content">
          <div v-if="selectedCity" class="selected-info">
            <div class="info-item">
              <span class="label">지역:</span>
              <span class="value">{{ selectedRegion }}</span>
            </div>
            <div class="info-item">
              <span class="label">시군구:</span>
              <span class="value">{{ selectedCity }}</span>
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
                  <ul class="facility-list">
                      <li v-for="facility in facilityListState.data" :key="facility.id" class="facility-item">
                          <span class="facility-name">📌 {{ facility.name }} ({{ facility.type }})</span>
                          <span class="facility-address">{{ facility.address }}</span>
                      </li>
                  </ul>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import svgContent from '@/img/maps/korea_map.svg?raw'
import { facilityListState, fetchExerciseFacilities, setSelectedRegionAndCity } from '@/stores/exerciseFacilitiesStore'

// 상태 관리
const showDropdown = ref(false)
const selectedRegion = ref('')
const selectedCity = ref('')
const selectedRegionElement = ref(null)
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
  const target = event.target
  const regionGroup = target.closest('[data-name]')

  if (regionGroup) {
    // 이전 선택 영역 스타일 제거
    if (selectedRegionElement.value) {
      const prevPaths = selectedRegionElement.value.querySelectorAll('path, polygon')
      prevPaths.forEach(el => {
        el.style.stroke = ''
        el.style.strokeWidth = ''
        el.style.fill = ''
      })
    }

    // 새로운 영역 선택
    const region = regionGroup.getAttribute('data-name')
    selectedRegionElement.value = regionGroup

    // 선택된 영역 내 모든 path/polygon에 스타일 적용
    const paths = regionGroup.querySelectorAll('path, polygon')
    paths.forEach(el => {
      el.style.fill = '#4caf50'
      el.style.stroke = 'none'
    })

    // SVG 컨테이너의 위치 기준으로 드롭다운 위치 계산
    const mapContainer = document.querySelector('.map-container')
    const containerRect = mapContainer.getBoundingClientRect()

    // 마우스 위치 (클릭 위치 기준)
    let mouseX = event.clientX - containerRect.left
    let mouseY = event.clientY - containerRect.top

    // 드롭다운 크기 (대략적인 크기로 초기 계산)
    const dropdownWidth = 200
    const dropdownHeight = 300

    // 오른쪽 여백 체크: 드롭다운이 컨테이너를 벗어나면 왼쪽에 배치
    if (mouseX + dropdownWidth + 10 > containerRect.width) {
      mouseX = Math.max(10, mouseX - dropdownWidth - 10)
    } else {
      mouseX = mouseX + 10
    }

    // 아래쪽 여백 체크: 드롭다운이 컨테이너를 벗어나면 위쪽에 배치
    if (mouseY + dropdownHeight > containerRect.height) {
      mouseY = Math.max(10, mouseY - dropdownHeight - 10)
    }

    // 드롭다운 위치 설정
    dropdownPosition.top = Math.max(0, mouseY) + 'px'
    dropdownPosition.left = Math.max(0, mouseX) + 'px'

    selectedRegion.value = region
    selectedCity.value = ''
    showDropdown.value = true
    setSelectedRegionAndCity(region, '')
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

// 지역별 도시/구 데이터
const regionCities = reactive({
'서울': [
    '강남구', '강동구', '강북구', '강서구', '관악구', '광진구', '구로구', '금천구', '노원구',
    '도봉구', '동대문구', '동작구', '마포구', '서대문구', '서초구', '성동구', '성북구',
    '송파구', '양천구', '영등포구', '용산구', '은평구', '종로구', '중구', '중랑구'
  ],
  '경기': [
    '수원시 장안구', '수원시 권선구', '수원시 팔달구', '수원시 영통구',
    '성남시 수정구', '성남시 중원구', '성남시 분당구',
    '안양시 만안구', '안양시 동안구',
    '안산시 상록구', '안산시 단원구',
    '용인시 처인구', '용인시 기흥구', '용인시 수지구',
    '고양시 덕양구', '고양시 일산동구', '고양시 일산서구',
    '부천시',
    '평택시', '화성시', '남양주시', '광명시', '동두천시', '과천시', '구리시', '오산시',
    '시흥시', '군포시', '의왕시', '하남시', '이천시', '안성시', '김포시', '광주시',
    '파주시', '포천시', '양주시', '여주시', '연천군', '가평군', '양평군'
  ],
  '인천': [
    '중구', '동구', '미추홀구', '연수구', '남동구', '부평구', '계양구', '서구',
    '강화군', '옹진군'
  ],
  '강원': [
    '춘천시', '원주시', '강릉시', '동해시', '태백시', '속초시', '삼척시', '홍천군',
    '횡성군', '영월군', '평창군', '정선군', '철원군', '화천군', '양구군', '인제군',
    '고성군', '양양군'
  ],
  '충북': [
    '청주시 상당구', '청주시 서원구', '청주시 흥덕구', '청주시 청원구',
    '충주시', '제천시', '보은군', '옥천군', '영동군', '증평군', '진천군',
    '괴산군', '음성군', '단양군'
  ],
  '충남': [
    '천안시 동남구', '천안시 서북구',
    '공주시', '보령시', '아산시', '서산시', '논산시', '계룡시', '당진시', '금산군',
    '부여군', '서천군', '청양군', '홍성군', '예산군', '태안군'
  ],
  '전북': [
    '전주시 완산구', '전주시 덕진구',
    '군산시', '익산시', '정읍시', '남원시', '김제시', '완주군', '진안군', '무주군',
    '장수군', '임실군', '순창군', '고창군', '부안군'
  ],
  '전남': [
    '목포시', '여수시', '순천시', '나주시', '광양시', '담양군', '곡성군', '구례군',
    '고흥군', '보성군', '화순군', '장흥군', '강진군', '해남군', '영암군', '무안군',
    '함평군', '영광군', '장성군', '완도군', '진도군', '신안군'
  ],
  '경북': [
    '포항시 남구', '포항시 북구',
    '경주시', '김천시', '안동시', '구미시', '영주시', '영천시', '상주시', '문경시',
    '경산시', '군위군', '의성군', '청송군', '영양군', '영덕군', '청도군', '고령군',
    '성주군', '예천군', '봉화군', '울진군', '울릉군', '칠곡군'
  ],
  '경남': [
    '창원시 의창구', '창원시 성산구', '창원시 마산합포구', '창원시 마산회원구', '창원시 진해구',
    '진주시', '통영시', '사천시', '김해시', '밀양시', '거제시', '양산시', '의령군',
    '함안군', '창녕군', '고성군', '남해군', '하동군', '산청군', '함양군', '거창군', '합천군'
  ],
  '부산': [
    '중구', '서구', '동구', '영도구', '부산진구', '동래구', '남구', '북구',
    '해운대구', '사하구', '금정구', '강서구', '연제구', '수영구', '사상구', '기장군'
  ],
  '대구': [
    '중구', '동구', '서구', '남구', '북구', '수성구', '달서구', '달성군', '군위군'
  ],
  '광주': [
    '동구', '서구', '남구', '북구', '광산구'
  ],
  '대전': [
    '동구', '중구', '서구', '유성구', '대덕구'
  ],
  '울산': [
    '중구', '남구', '동구', '북구', '울주군'
  ],
  '세종': [
    '세종시'
  ],
  '제주': [
    '제주시', '서귀포시'
  ]
})
</script>

<style lang="scss" scoped>
.map-wrapper {
  width: 100%;
  padding: 20px;
}

.container {
  display: flex;
  gap: 30px;
  max-width: 1400px;
  margin: 0 auto;
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
    width: 100%;
    height: 100%;
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
  background: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  min-height: 400px;
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
