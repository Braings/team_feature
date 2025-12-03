import { reactive, ref } from 'vue'
// import { loadExerciseFacilities } from '@/api' // api.js에서 가져온 실제 API 헬퍼 함수


const mockFacilitiesData = [
  { id: 1, name: '강남 스포츠 센터', type: '헬스', address: '서울 강남구 테헤란로 123' },
  { id: 2, name: '역삼 요가 스튜디오', type: '요가', address: '서울 강남구 논현로 456' },
  { id: 3, name: '삼성 실내 수영장', type: '수영', address: '서울 강남구 영동대로 789' },
  { id: 4, name: '대치 필라테스', type: '필라테스', address: '서울 강남구 선릉로 101' },
];
// ------------------------------

/**
 * API 호출: 특정 지역의 운동 시설 목록을 조회하고 Store 상태를 업데이트 합니다.
 * @param {string} region - '도' 또는 '시' (예: '서울')
 * @param {string} city - '시군구' (예: '강남구')
 * @returns {void} Store 상태 업데이트
 * @throws {Error} API 오류 시 에러 throw
 */
export async function fetchExerciseFacilities(region, city) {
  // 1. 로딩 상태 설정
  facilityListState.isLoading = true
  facilityListState.hasError = false
  facilityListState.data = []

  // 2. API 호출을 위한 쿼리 데이터 구성
  // const queryData = {
  //   region: region, // api.js의 loadExerciseFacilities가 요구하는 파라미터명에 맞춰 조정
  //   city: city
  // }

  try {
    // 3. 실제 API 호출 대신 2초 지연 후 더미 데이터 사용 (시뮬레이션)
    await new Promise(resolve => setTimeout(resolve, 2000)); // 2초 로딩 시뮬레이션

    let response = [];

    // 선택된 지역이 '서울'이고 도시가 '강남구'인 경우에만 더미 데이터 반환
    if (region === '서울' && city === '강남구') {
        response = mockFacilitiesData;
    } else if (region === '서울' && city === '강동구') {
        // 다른 지역/도시에 대한 가상 데이터 추가
        response = [
          { id: 10, name: '강동 체육관', type: '농구/배구', address: '서울 강동구 올림픽로 500' },
          { id: 11, name: '암사동 헬스클럽', type: '헬스', address: '서울 강동구 암사길 10' }
        ];
    } else {
        // 그 외 지역은 빈 배열 (시설 없음) 반환
        response = [];
    }


    // const response = await loadExerciseFacilities(queryData) // 💡 실제 API 호출 주석 처리
    console.log('운동시설 불러오기 성공 (더미 데이터):', response)

    // 4. 성공 시 데이터 상태 업데이트
    facilityListState.data = response // response는 시설 목록 배열이라고 가정

  } catch (error) {
    // 5. 에러 시 에러 상태 설정
    console.error('운동시설 불러오기 실패:', error)
    facilityListState.hasError = true
    throw error // 에러를 다시 던져서 호출부(Vue 컴포넌트)에서도 처리할 수 있게 함
  } finally {
    // 6. 로딩 상태 해제
    facilityListState.isLoading = false
  }
}
// -------------------------------------------------------------
// 1. 전역 상태 (STATE) 정의
// -------------------------------------------------------------

// 폼 데이터 전역 상태 (등록/수정 폼에 사용될 필드)
export const exerciseFacilitiesFormData = reactive({
  FCLTY_NM :'', //(건물명)
  INDUTY_NM:'', //(사업 분류)

  // 도로명 주소
  ROAD_NM_ZIP_NO:'', //(우편번호)
  RDNMADR_ONE_NM:'', //(주소)
  RDNMADR_TWO_NM:'', //(상세주소)

  // 구주소
  ZIP_NO_VALUE:'', //(우편번호)
  FCLTY_ADDR_ONE_NM:'', //(주소)
  FCLTY_ADDR_TWO_NM:'', //(상세주소)

  FCLTY_TEL_NO:'', //(시설 번호)
  RSPNSBLTY_TEL_NO:'', //(사장 번호)
  FCLTY_HMPG_URL:'', //(홈페이지)

  // 지도 선택 정보 (필요한 경우 여기에 유지)
  FCLTY_MANAGE_CTPRVN_NM:'', //(도)
  FCLTY_MANAGE_SIGNGU_NM:'', //(시군구)
  FCLTY_MANAGE_CTPRVN_NMElement :'',
})

// 지도에서 선택된 지역 시설 목록 및 상태 (조회 결과 저장)
export const facilityListState = reactive({
    data: ref([]), // 조회된 시설 목록
    isLoading: ref(false), // 로딩 상태
    hasError: ref(false),  // 에러 상태
})


// -------------------------------------------------------------
// 2. 함수 (ACTIONS) 정의
// -------------------------------------------------------------

/**
 * 폼 데이터 필드를 초기화하는 함수
 */
export function resetFormData() {
  exerciseFacilitiesFormData.FCLTY_MANAGE_CTPRVN_NM = ''
  exerciseFacilitiesFormData.FCLTY_MANAGE_SIGNGU_NM = ''
  exerciseFacilitiesFormData.FCLTY_MANAGE_CTPRVN_NMElement = ''

  // 필요하다면 다른 필드들도 초기화 로직을 추가할 수 있습니다.
}

/**
 * 지도에서 선택된 지역(도/시) 및 시군구 정보를 Store에 저장합니다.
 * @param {string} region - '도' 또는 '시' (예: '서울')
 * @param {string} city - '시군구' (예: '강남구')
 */
export function setSelectedRegionAndCity(region, city) { //
  exerciseFacilitiesFormData.FCLTY_MANAGE_CTPRVN_NM = region //
  exerciseFacilitiesFormData.FCLTY_MANAGE_SIGNGU_NM = city //
  console.log(`Store 상태 업데이트: 지역=${region}, 시군구=${city}`)
}

/**
 * API 호출: 특정 지역의 운동 시설 목록을 조회하고 Store 상태를 업데이트 합니다.
 * @param {string} region - '도' 또는 '시' (예: '서울')
 * @param {string} city - '시군구' (예: '강남구')
 * @returns {void} Store 상태 업데이트
 * @throws {Error} API 오류 시 에러 throw
 */

// export async function fetchExerciseFacilities(region, city) {
//   // 1. 로딩 상태 설정
//   facilityListState.isLoading = true
//   facilityListState.hasError = false
//   facilityListState.data = []

//   // 2. API 호출을 위한 쿼리 데이터 구성
//   const queryData = {
//     region: region, // api.js의 loadExerciseFacilities가 요구하는 파라미터명에 맞춰 조정
//     city: city
//   }

//   try {
//     // 3. 실제 API 호출
//     const response = await loadExerciseFacilities(queryData)
//     console.log('운동시설 불러오기 성공:', response)

//     // 4. 성공 시 데이터 상태 업데이트
//     facilityListState.data = response // response는 시설 목록 배열이라고 가정

//   } catch (error) {
//     // 5. 에러 시 에러 상태 설정
//     console.error('운동시설 불러오기 실패:', error)
//     facilityListState.hasError = true
//     throw error // 에러를 다시 던져서 호출부(Vue 컴포넌트)에서도 처리할 수 있게 함
//   } finally {
//     // 6. 로딩 상태 해제
//     facilityListState.isLoading = false
//   }
// }
