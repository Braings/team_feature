import { reactive, ref } from 'vue'
import { loadExerciseFacilities } from '@/api' // api.js에서 가져온 실제 API 헬퍼 함수
import localFacilityData from './facilityListState.js'

// -------------------------------------------------------------
// 1. 전역 상태 (STATE) 정의
// -------------------------------------------------------------

// 폼 데이터 전역 상태 (등록/수정 폼에 사용될 필드)
export const exerciseFacilitiesFormData = reactive({
  id:'',
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

export const facilityDetailState = reactive({
    data: {},
    isLoading: ref(false),
    hasError: ref(false),
});
// -------------------------------------------------------------
// 2. 함수 (ACTIONS) 정의
// -------------------------------------------------------------

// 폼 데이터 필드를 초기화하는 함수

export function resetFormData() {
  exerciseFacilitiesFormData.id = ''
  exerciseFacilitiesFormData.FCLTY_MANAGE_CTPRVN_NM = ''
  exerciseFacilitiesFormData.FCLTY_MANAGE_SIGNGU_NM = ''
  exerciseFacilitiesFormData.FCLTY_MANAGE_CTPRVN_NMElement = ''
}


// 지도에서 선택된 지역(도/시) 및 시군구 정보를 Store에 저장합니다.
  // @param {string} region - '도' 또는 '시' (예: '서울')
  // @param {string} city - '시군구' (예: '강남구')


export function setSelectedRegionAndCity(region, city) {
  exerciseFacilitiesFormData.FCLTY_MANAGE_CTPRVN_NM = region
  exerciseFacilitiesFormData.FCLTY_MANAGE_SIGNGU_NM = city
  console.log(`Store 상태 업데이트: 지역=${region}, 시군구=${city}`)
}


// API 호출: 특정 지역의 운동 시설 목록을 조회하고 Store 상태를 업데이트 합니다.
  // @param {string} region - '도' 또는 '시' (예: '서울')
  // @param {string} city - '시군구' (예: '강남구')
  // @returns {void} Store 상태 업데이트
  // @throws {Error} API 오류 시 에러 throw


export async function fetchExerciseFacilities(region, city) {

  // 1. 로딩 상태 설정
  facilityListState.isLoading = true
  facilityListState.hasError = false
  facilityListState.data = []

  // 2. API 호출을 위한 쿼리 데이터 구성
  const queryData = {
    region: encodeURIComponent(region),
    city: encodeURIComponent(city),
  }

  try {
    // 3. 실제 API 호출
    const response = await loadExerciseFacilities(queryData)
    console.log('운동시설 불러오기 성공:', response)

    // 4. 성공 시 데이터 상태 업데이트
    facilityListState.data = response // response는 시설 목록 배열이라고 가정

  } catch (error) {
    // 5. 에러 시 에러 상태 설정
    console.error('운동시설 불러오기 실패:', error)
    // facilityListState.hasError = true
    // throw error // 실제 가동시엔 주석 해제해야함

    // 더미 데이터 불러오기
    facilityListState.data = localFacilityData;


  } finally {
    // 6. 로딩 상태 해제
    facilityListState.isLoading = false
  }
}

// 2. 상세 정보 조회를 위한 비동기 함수
export const fetchFacilityDetailById = async (id) => {
    facilityDetailState.isLoading.value = true;
    facilityDetailState.hasError.value = false;
    // 이전 데이터 초기화
    Object.keys(facilityDetailState.data).forEach(key => delete facilityDetailState.data[key]);

    try {
        // 🚨 실제 API 호출 로직을 여기에 구현합니다.
        // 예: const response = await axios.get(`/api/facilities/${id}`);

        // 🚨 임시: 2초 지연 후 더미 데이터 로드 시뮬레이션
        await new Promise(resolve => setTimeout(resolve, 2000));

        // API에서 받은 상세 정보라고 가정합니다.
        const mockDetail = { id: id, FCLTY_NM: `테스트 시설 ${id}` };

        if (mockDetail) {
             Object.assign(facilityDetailState.data, mockDetail);
        } else {
            throw new Error(`ID ${id} 시설 정보를 찾을 수 없습니다.`);
        }
    } catch (error) {
        facilityDetailState.hasError.value = true;
        console.error("Store: 시설 상세 정보 조회 실패", error);
        throw error; // 컴포넌트에서 에러를 잡을 수 있도록 다시 throw
    } finally {
        facilityDetailState.isLoading.value = false;
    }
};
