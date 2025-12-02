const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080';

async function request(path, options = {}) {
  const defaultOptions = {
    credentials: 'include', // 쿠키 포함
    headers: { 'Content-Type': 'application/json' }
  };
  const finalOptions = Object.assign(defaultOptions, options);

  // GET/DELETE 요청 시 body 제거
  if ((finalOptions.method === 'GET' || finalOptions.method === 'DELETE') && finalOptions.body) {
    delete finalOptions.body;
    delete finalOptions.headers['Content-Type'];
  }

  const url = `${API_BASE}${path}`;
  const res = await fetch(url, finalOptions);
  const text = await res.text();

  let data = null;
  try { data = text ? JSON.parse(text) : null;
  } catch(error) { data = text; console.error('데이터 로드 실패:', error);}

  if (!res.ok) {
    const err = new Error(data?.message || res.statusText || 'API error');
    err.status = res.status;
    err.raw = data;
    throw err;
  }
  return data;
}

export async function post(path, body = {}, opts = {}) {
  return request(path, Object.assign({
    method: 'POST',
    body: JSON.stringify(body),
  }, opts));
}

export async function get(path, opts = {}) {
  return request(path, Object.assign({
    method: 'GET',
  }, opts));
}

// ========================
// User Exercise Board API Endpoints
// ========================

export async function getUserExercise() {
  return get('/api/userExercise');
}

// ========================
// User Profile API Endpoints
// ========================

/**
 * 현재 로그인된 사용자의 프로필 정보를 조회합니다.
 * @returns {Promise<Object>} 사용자 프로필 데이터
 */

export async function getProfile() {
  // 일반적으로 토큰이나 세션을 통해 현재 사용자를 식별하므로, 경로는 단순합니다.
  return get('/api/users/profile');
}

/**
 * 현재 로그인된 사용자의 프로필 정보를 수정합니다.
 * PUT 메서드를 사용하여 리소스 전체를 업데이트합니다.
 * @param {Object} data - 수정할 프로필 데이터
 * @returns {Promise<Object>} 업데이트된 프로필 데이터
 */

export async function updateProfile(data) {
  return request('/api/users/profile', {
    method: 'PUT',
    body: JSON.stringify(data),
    // headers와 credentials는 request 함수에서 기본값으로 처리됩니다.
  });
}

// ========================


// ========================
// Review Board API Endpoints
// ========================

export async function getReviews(query = {}) {
  const params = new URLSearchParams(query);
  return get(`/api/reviews?${params.toString()}`);
}

export async function getReviewDetail(reviewId) {
  return get(`/api/reviews/${reviewId}`);
}

export async function createReview(data) {
  return post('/api/reviews', data);
}

// PUT 메서드를 사용하여 RESTful 표준을 따릅니다.
// PUT은 리소스 전체를 업데이트할 때 사용합니다.
export async function updateReview(reviewId, data) {
  return request(`/api/reviews/${reviewId}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  });
}

// DELETE 메서드를 직접 request 함수로 호출합니다.
export async function deleteReview(reviewId) {
  return request(`/api/reviews/${reviewId}`, {
    method: 'DELETE',
  });
}





export default {
    API_BASE, request, post, get,
    getReviews, getReviewDetail, createReview, updateReview, deleteReview,
    getProfile, updateProfile
};
