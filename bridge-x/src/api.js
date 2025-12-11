// 환경 변수에서 API 기본 URL을 가져오거나 기본값(로컬호스트)을 설정합니다.
const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080';

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
    // 브라우저에게 해당 API 호출 시 현재 도메인에 저장된 모든 쿠키를 요청에 자동으로 포함하여 서버로 보내도록 지시
    credentials: 'include',
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
    const err = new Error(data?.message || res.statusText || 'API 문제');
    err.status = res.status; // HTTP 상태 코드 저장
    err.raw = data; // 응답 데이터 원본 저장
    throw err; // 호출 함수에서 catch 할 수 있도록 에러 발생
  }
  return data;
}

// ===============================================================
//  POST 함수
// ===============================================================

export async function post(path, body = {}, opts = {}) {
  return request(path, Object.assign({
    method: 'POST',
    body: JSON.stringify(body), // 객체를 JSON 문자열로 변환하여 전송
  }, opts));
}

// ===============================================================
//  GET 함수
// ===============================================================

export async function get(path, opts = {}) {
  return request(path, Object.assign({
    method: 'GET',
  }, opts));
}

// ===============================================================
// 사용자 운동 기록 조회 API 함수
// ===============================================================

export async function getUserExercise() {
  return get('/api/userExercise');
}

// ===============================================================
//  운동 시설 조회 API 함수
// ===============================================================

export async function loadExerciseFacilities(query = {}) {
  // 쿼리 객체를 'region=도&city=시군구' 형태의 URLSearchParams로 변환
  // 예: { region: '서울', city: '강남구' } -> ?region=서울&city=강남구
  const params = new URLSearchParams(query);
  return get(`/api/exerciseFacilities?${params.toString()}`);
}


//================================================================
// 사용자 프로필 관련 API 함수
// ===============================================================

export async function getUserProfile() {
  return get('/api/users/profile');
}

export async function updateProfile(data) {
  return request('/api/users/profile', {
    method: 'PUT',
    body: JSON.stringify(data),
  });
}

// ===============================================================
// 사용자 신체 정보 조회 API 함수 (새로 추가)
// ===============================================================

export async function getUserBodyInfo() {
  // 백엔드 URL을 /users/body에 맞춰서 호출
  return get('/api/users/body');
}

// ===============================================================
// 리뷰(게시물) 관련 API
// ===============================================================

export async function getReviews(params) {
  const query = new URLSearchParams(params).toString();
  return get(`/api/reviews?${query}`);
}

export async function getReviewDetail(reviewId) {
  return get(`/api/reviews/${reviewId}`);
}

export async function createReview(data) {
  return post('/api/reviews', data);
}

export async function updateReview(reviewId, data) {
  return request(`/api/reviews/${reviewId}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  });
}

export async function deleteReview(reviewId) {
  return request(`/api/reviews/${reviewId}`, {
    method: 'DELETE',
  });
}

// 추천(좋아요) 토글 API
// 백엔드 로직: 이 유저가 이미 눌렀으면 취소(-1), 안 눌렀으면 추가(+1) 처리
export async function toggleReviewRecommend(reviewId) {
  return post(`/api/reviews/${reviewId}/recommend`);
}

// ===============================================================
// 댓글 관련 API
// ===============================================================

export async function createComment(reviewId, data) {
  // 예: /api/reviews/{reviewId}/comments 또는 data 안에 reviewId 포함
  // 여기서는 data에 reviewId를 포함하여 전송하는 방식 예시
  return post(`/api/reviews/${reviewId}/comments`, data);
}

export async function getComment(reviewId) {
  // 예: /api/reviews/{reviewId}/comments 또는 data 안에 reviewId 포함
  // 여기서는 data에 reviewId를 포함하여 전송하는 방식 예시
  return get(`/api/reviews/${reviewId}/comments`);
}
export async function updateComment(commentId, data) {
  return request(`/api/comments/${commentId}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  });
}

export async function deleteComment(commentId) {
  return request(`/api/comments/${commentId}`, {
    method: 'DELETE',
  });
}

// ===============================================================
// 기본 내보내기 (모든 API 함수 일괄 접근용)
// ===============================================================

export default {
    API_BASE, request, post, get,
    getReviews, getReviewDetail, createReview, updateReview, deleteReview,
    getUserProfile, updateProfile, loadExerciseFacilities,
    createComment, updateComment, deleteComment, toggleReviewRecommend,
};
