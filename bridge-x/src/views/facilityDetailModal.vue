<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="emit('close')">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">
          <span v-if="isLoading">상세 정보 불러오는 중...</span>
          <span v-else-if="facilityDetail.FCLTY_NM">{{ facilityDetail.FCLTY_NM }} 상세 정보</span>
          <span v-else>시설 상세 정보</span>
        </h3>
        <button class="close-button" @click="emit('close')">&times;</button>
      </div>

      <div class="modal-body">
        <div v-if="isLoading" class="loading-state">
            <i class="fa fa-spinner fa-spin"></i>
            데이터를 불러오는 중입니다...
        </div>

        <div v-else-if="hasError" class="error-state">
            🚨 정보를 불러오는 데 실패했습니다. ID: {{ facilityId }}
        </div>

        <div v-else-if="facilityDetail.FCLTY_NM" class="detail-content">

          <div class="detail-group">
            <label>사업 분류</label>
            <p>{{ facilityDetail.INDUTY_NM }}</p>
          </div>

          <div class="detail-group">
            <label>시설 주소 (도로명)</label>
            <p>{{ facilityDetail.RDNMADR_ONE_NM }}</p>
          </div>

          <div class="detail-group">
            <label>시설 주소 (지번)</label>
            <p>{{ facilityDetail.FCLTY_ADDR_ONE_NM }}</p>
          </div>

          <div class="detail-group half">
            <label>시설 연락처</label>
            <p>{{ facilityDetail.FCLTY_TEL_NO }}</p>
          </div>

          <div class="detail-group half">
            <label>담당자 연락처</label>
            <p>{{ facilityDetail.RSPNSBLTY_TEL_NO }}</p>
          </div>

          <div class="detail-group">
            <label>홈페이지</label>
            <p v-if="facilityDetail.FCLTY_HMPG_URL">
                <a :href="facilityDetail.FCLTY_HMPG_URL" target="_blank">{{ facilityDetail.FCLTY_HMPG_URL }}</a>
            </p>
            <p v-else>정보 없음</p>
          </div>

        </div>

        <div v-else class="empty-state">
            선택된 시설의 상세 정보가 없습니다.
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="cancel-button" @click="emit('close')">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, watch } from 'vue';
// Store의 데이터는 사용하지 않고, API 요청만 가정합니다.
// 실제 API가 없으므로 더미 데이터를 사용합니다.
import localFacilityData from '@/stores/facilityListState.js'

// props 정의: 모달 열림 상태와 상세 조회할 시설 ID
const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true
  },
  facilityId: {
    type: [String, Number],
    default: null
  }
});

const emit = defineEmits(['close']);

// 로컬 상태
const facilityDetail = reactive({});
const isLoading = ref(false);
const hasError = ref(false);

/**
 * 💡 시설 ID를 사용하여 상세 정보를 조회하는 함수 (더미 데이터 사용)
 * 실제 환경에서는 `getFacilityDetail(id)`와 같은 API 함수를 호출해야 합니다.
 */
const fetchFacilityDetail = async (id) => {
    if (id === null || id === undefined) return;

    isLoading.value = true;
    hasError.value = false;

    // 이전에 로드된 데이터를 초기화
    Object.keys(facilityDetail).forEach(key => delete facilityDetail[key]);

    try {
        const targetId = String(id);

        console.log('fetchFacilityDetail 호출됨. 검색 ID:', targetId);
        console.log('더미 데이터 로드 상태:', localFacilityData ? '성공' : '실패', '총 항목 수:', localFacilityData ? localFacilityData.length : 0);

        // 더 안전한 ID 비교 (더미 데이터의 id는 문자열)
        const detail = localFacilityData.find(f => String(f.id) === targetId); // String()을 한번 더 사용하여 안전성 강화
        if (detail) {
            // 상세 정보를 reactive 객체에 복사
            Object.assign(facilityDetail, detail);
            console.log('시설 상세 정보 로드 성공:', detail);
        } else {
          throw new Error(`시설 ID ${targetId} 정보를 찾을 수 없습니다.`);
        }
    } catch (error) {
        console.error('시설 상세 정보 로드 실패:', error);
        hasError.value = true;
    } finally {
        isLoading.value = false;
    }
}

watch(() => [props.isOpen, props.facilityId], ([newOpen, newId]) => {
  if (newOpen && newId !== null && newId !== undefined) {
    fetchFacilityDetail(newId);
  }
  // 모달이 닫히면 상태 초기화
  if (!newOpen) {
    Object.keys(facilityDetail).forEach(key => delete facilityDetail[key]);
    isLoading.value = false;
    hasError.value = false;
  }
});
</script>

<style lang="scss" scoped>
@import '@/styles/_variables.scss';
// 변수 파일 경로를 실제 경로로 수정하세요.
// 만약 _variables.scss가 없다면, 아래의 스타일 코드를 일반 CSS로 변경해야 합니다.

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 600px; /* 상세 정보 표시를 위해 최대 너비 증가 */
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

.modal-header {
  padding: 20px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-title {
    font-size: 1.4rem;
    color: #4CAF50;
    margin: 0;
}

.close-button {
    background: none;
    border: none;
    font-size: 1.8rem;
    cursor: pointer;
    color: #999;
}

.modal-body {
  padding: 20px;
  display: flex;
  flex-wrap: wrap; /* 상세 항목을 가로로 배치하기 위해 추가 */
}

/* 상세 정보 그룹 스타일 */
.detail-content {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
}

.detail-group {
    width: 100%; /* 기본적으로 한 줄 전체 사용 */
    margin-bottom: 15px;
    padding: 10px;
    border: 1px solid #eee;
    border-radius: 5px;
    background: #fafafa;
}

.detail-group.half {
    width: calc(50% - 5px); /* 두 항목을 가로로 나란히 배치 */
}


.detail-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
  color: #555;
  font-size: 0.9rem;
}

.detail-group p {
    margin: 0;
    font-size: 1rem;
    color: #333;
    word-break: break-word; /* 긴 주소 처리 */
}

.detail-group a {
    color: #2196F3;
    text-decoration: none;
    &:hover { text-decoration: underline; }
}

.modal-footer {
  padding: 20px;
  border-top: 1px solid #e0e0e0;
  display: flex;
  justify-content: flex-end;
}

.cancel-button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  background-color: #f5f5f5;
  color: #333;
  &:hover { background-color: #e0e0e0; }
}

.loading-state, .error-state, .empty-state {
    width: 100%;
    text-align: center;
    padding: 30px 0;
}
.loading-state { color: #2196F3; }
.error-state { color: #F44336; }

</style>
