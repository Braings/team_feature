// 환경 변수에서 API 기본 URL을 가져오거나 기본값(로컬호스트)을 설정합니다.
const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080';

// --------------------------------------------------------------- //

// 공통 API 헬퍼 함수

// ===============================================================
//  REQUEST 함수
// ===============================================================

// [핵심 함수] 모든 API 요청을 처리하는 제네릭 헬퍼 함수입니다.
  // HTTP 요청의 공통 설정, 응답 처리 및 에러 처리를 담당합니다.
  // @param {string} path - 요청할 엔드포인트 경로 (예: '/api/reviews')
  // @param {Object} options - fetch API에 전달될 추가 옵션 (method, body 등)
  // @returns {Promise<Object>} 성공적인 JSON 응답 데이터
  // @throws {Error} HTTP 상태 코드가 200번대가 아닐 경우 에러 발생

async function request(path, options = {}) {
  // 모든 요청에 기본으로 적용될 옵션 설정
  const defaultOptions = {
    credentials: 'include', // 세션/인증 쿠키를 요청에 포함 (인증 유지)
    headers: { 'Content-Type': 'application/json' } // 기본 콘텐츠 타입 (JSON)
  };
  const finalOptions = Object.assign(defaultOptions, options);

  // GET/DELETE 요청은 Body를 가질 수 없으므로, 혹시 Body가 있다면 제거
  if ((finalOptions.method === 'GET' || finalOptions.method === 'DELETE') && finalOptions.body) {
    delete finalOptions.body;
    // Body가 없으므로 Content-Type 헤더도 제거하여 불필요한 정보 전송 방지
    delete finalOptions.headers['Content-Type'];
  }

  // 최종 URL 구성: 기본 URL + 요청 경로
  const url = `${API_BASE}${path}`;

  // fetch API를 사용한 실제 요청 및 응답 대기
  const res = await fetch(url, finalOptions);

  // 응답 본문(Body)을 텍스트로 읽기
  const text = await res.text();

  let data = null;
  // 응답 텍스트를 JSON으로 파싱 시도 (비어있는 응답도 처리)
  try {
    data = text ? JSON.parse(text) : null;
  } catch(error) {
    // JSON 파싱 실패 시 (예: HTML 응답) 텍스트 그대로 저장
    data = text;
    console.error('데이터 로드 실패:', error);
  }

  // 응답 상태가 성공(res.ok = 2xx)이 아닐 경우 에러 처리
  if (!res.ok) {
    const err = new Error(data?.message || res.statusText || 'API error');
    err.status = res.status; // HTTP 상태 코드 저장
    err.raw = data; // 응답 데이터 원본 저장
    throw err; // 호출 함수에서 catch 할 수 있도록 에러 발생
  }

  // 성공적으로 파싱된 데이터 반환
  return data;
}

// ===============================================================
//  POST 함수
// ===============================================================

// POST 요청을 간편하게 수행하는 헬퍼 함수 (데이터 생성)
  // @param {string} path - 요청 경로
  // @param {Object} body - 요청 본문에 포함할 데이터 객체

export async function post(path, body = {}, opts = {}) {
  return request(path, Object.assign({
    method: 'POST',
    body: JSON.stringify(body), // 객체를 JSON 문자열로 변환하여 전송
  }, opts));
}

// ===============================================================
//  GET 함수
// ===============================================================

// GET 요청을 간편하게 수행하는 헬퍼 함수 (데이터 조회)
  // @param {string} path - 요청 경로

export async function get(path, opts = {}) {
  return request(path, Object.assign({
    method: 'GET', // GET 메서드 지정
  }, opts));
}

// --------------------------------------------------------------- //

// User Exercise Board API Endpoints (사용자 운동 게시판)


// ===============================================================
// 사용자 운동 기록 조회 API 함수
// ===============================================================

// 사용자 운동 기록 목록을 조회합니다.
  // @returns {Promise<Object>} 운동 기록 데이터 목록

export async function getUserExercise() {
  return get('/api/userExercise');
}

// --------------------------------------------------------------- //

// User Profile API Endpoints (사용자 프로필)

//================================================================
// 사용자 프로필 조회 API 함수
// ===============================================================

// 현재 로그인된 사용자의 프로필 정보를 조회합니다.
  // @returns {Promise<Object>} 사용자 프로필 데이터

  export async function getProfile() {
  // 인증 정보를 통해 서버가 현재 사용자를 식별하여 데이터를 반환합니다.
  return get('/api/users/profile');
}

// ===============================================================
// 사용자 프로필 수정 API 함수
// ===============================================================

// 현재 로그인된 사용자의 프로필 정보를 수정합니다.
  // @param {Object} data - 수정할 프로필 데이터
  // @returns {Promise<Object>} 업데이트된 프로필 데이터


export async function updateProfile(data) {
  return request('/api/users/profile', {
    method: 'PUT', // PUT 메서드 사용: 리소스 전체를 업데이트
    body: JSON.stringify(data),
  });
}

// --------------------------------------------------------------- //

// Review Board API Endpoints (리뷰 게시판)

// ===============================================================
// 리뷰 목록 조회 API 함수
// ===============================================================

// 리뷰 목록을 검색 및 필터링하여 조회합니다.
  // @param {Object} query - 검색 및 페이지네이션을 위한 쿼리 파라미터 객체
  // @returns {Promise<Object>} 리뷰 목록 및 관련 메타데이터

export async function getReviews(query = {}) {
  // 쿼리 객체를 'page=1&limit=10' 형태의 URLSearchParams로 변환
  const params = new URLSearchParams(query);
  return get(`/api/reviews?${params.toString()}`);
}

// ===============================================================
// 리뷰 상세 조회 API 함수
// ===============================================================

// 특정 ID를 가진 리뷰 상세 정보를 조회합니다.
 // @param {string|number} reviewId - 조회할 리뷰의 고유 ID
 // @returns {Promise<Object>} 리뷰 상세 데이터

export async function getReviewDetail(reviewId) {
  return get(`/api/reviews/${reviewId}`);
}

// ===============================================================
// 리뷰 생성 API 함수
// ===============================================================

// 새로운 리뷰를 생성합니다.
  // @param {Object} data - 생성할 리뷰 데이터 (제목, 내용, 태그 등)
  // @returns {Promise<Object>} 생성된 리뷰 데이터 (ID, 생성 시간 등 포함)

export async function createReview(data) {
  return post('/api/reviews', data);
}

// ===============================================================
// 리뷰 수정 API 함수
// ===============================================================

// 특정 ID를 가진 리뷰를 수정합니다.
 // @param {string|number} reviewId - 수정할 리뷰의 고유 ID
 // @param {Object} data - 수정 내용이 담긴 데이터 객체
 // @returns {Promise<Object>} 업데이트된 리뷰 데이터

export async function updateReview(reviewId, data) {
  return request(`/api/reviews/${reviewId}`, {
    method: 'PUT', // PUT 메서드 사용: 리소스 전체를 업데이트
    body: JSON.stringify(data),
  });
}


// ===============================================================
// 리뷰 삭제 API 함수
// ===============================================================

// 특정 ID를 가진 리뷰를 삭제합니다.
  // @param {string|number} reviewId - 삭제할 리뷰의 고유 ID
  // @returns {Promise<Object>} 삭제 성공 응답

export async function deleteReview(reviewId) {
  return request(`/api/reviews/${reviewId}`, {
    method: 'DELETE', // DELETE 메서드 사용
  });
}

// --------------------------------------------------------------- //

// ===============================================================
// 기본 내보내기 (모든 API 함수 일괄 접근용)
// ===============================================================

export default {
    API_BASE, request, post, get,
    getReviews, getReviewDetail, createReview, updateReview, deleteReview,
    getProfile, updateProfile
};
