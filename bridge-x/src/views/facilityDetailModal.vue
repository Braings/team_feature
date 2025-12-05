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
import { watch } from 'vue';

// 💡 변경: 더미 데이터 대신 Store에서 필요한 상태와 함수를 가져옵니다.
import {
    facilityDetailState,      // 상세 정보를 담고 있는 반응형 상태
    fetchFacilityDetailById   // 상세 정보 API를 호출하는 함수
} from '@/stores/exerciseFacilitiesStore'

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

// 💡 변경: 로컬 상태 대신 Store 상태를 직접 사용합니다.
// Store의 상태에서 필요한 정보(데이터, 로딩, 에러)를 직접 가져와 변수로 사용합니다.
const facilityDetail = facilityDetailState.data;
const isLoading = facilityDetailState.isLoading;
const hasError = facilityDetailState.hasError;


/**
 * 💡 시설 ID를 사용하여 상세 정보를 조회하는 함수 (Store 함수 호출로 변경)
 * 실제 환경에서는 fetchFacilityDetailById(id) Store 함수를 호출합니다.
 */
const fetchDetail = async (id) => {
    if (id === null || id === undefined) return;

    try {
        console.log('fetchFacilityDetailById Store 함수 호출됨. 검색 ID:', id);
        // 💡 핵심 변경: Store의 비동기 함수를 호출하여 API 요청을 위임합니다.
        // 이 함수 내에서 로딩 및 에러 상태를 Store가 관리한다고 가정합니다.
        await fetchFacilityDetailById(id);

    } catch (error) {
        // Store에서 에러를 처리하지만, 컴포넌트에서도 추가적인 처리가 필요하다면 여기에 추가합니다.
        console.error('시설 상세 정보 로드 실패 (컴포넌트 레벨):', error);
        // Store가 에러 상태를 관리하므로 별도의 상태 변경은 필요하지 않습니다.
    }
}

watch(() => [props.isOpen, props.facilityId], ([newOpen, newId]) => {
  if (newOpen && newId !== null && newId !== undefined) {
    // 💡 변경: 로컬 함수 이름을 fetchDetail로 변경하고 호출합니다.
    fetchDetail(newId);
  }

  // 모달이 닫히면 상태 초기화
  if (!newOpen) {
    // 💡 변경: Store에서 상세 정보 초기화 로직을 담당한다고 가정하고,
    // Store에 초기화 함수를 호출하거나, Store가 자동으로 초기화하도록 설계할 수 있습니다.
    // 여기서는 props.isOpen이 false가 될 때 Store의 초기화 함수를 호출한다고 가정합니다.
    // 만약 Store에 초기화 함수가 없다면, Store의 data를 직접 초기화해야 합니다.

    // 예시: Store 초기화 함수를 호출한다고 가정 (Store 파일에 구현해야 함)
    // resetFacilityDetailState();

    // 현재 코드에서는 Store의 상태 객체를 직접 조작하는 대신,
    // Store가 닫힐 때 상태를 초기화하도록 설계하는 것이 일반적입니다.
    // 만약 Store를 수정할 수 없다면, 모달이 닫힐 때 데이터만 수동으로 지워줍니다.
    Object.keys(facilityDetail).forEach(key => delete facilityDetail[key]);

  }
}, { immediate: true }); // 즉시 실행 옵션 추가 (Store 상태가 초기값으로 설정되도록)
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
