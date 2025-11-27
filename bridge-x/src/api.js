// 간단한 API 헬퍼 (fetch 기반)
const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080';

async function request(path, options = {}) {
  // credentials: 'include' 옵션을 여기에 추가하여 모든 요청에 적용합니다.
  const defaultOptions = {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' }
  };
  const finalOptions = Object.assign(defaultOptions, options);

  // GET, DELETE 요청에 body가 포함되면 안되므로 제거
  if ((finalOptions.method === 'GET' || finalOptions.method === 'DELETE') && finalOptions.body) {
    delete finalOptions.body;
    delete finalOptions.headers['Content-Type'];
  }

  const url = `${API_BASE}${path}`;
  const res = await fetch(url, finalOptions);
  const text = await res.text();
  let data = null;
  try { data = text ? JSON.parse(text) : null; } catch(e) { data = text; }

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
  // GET 요청은 body가 없으므로 간결하게 유지
  return request(path, Object.assign({
    method: 'GET',
  }, opts));
}

// ========================
// Review Board API Endpoints
// ========================

export async function getReviews(query = {}) {
  const params = new URLSearchParams(query);
  return get(`/api/reviews?${params.toString()}`);
}

export async function getReviewDetail(username) {
  return get(`/api/reviews/${username}`);
}

export async function createReview(data) {
  return post('/api/reviews', data);
}

// PUT 메서드를 사용하여 RESTful 표준을 따릅니다.
// PUT은 리소스 전체를 업데이트할 때 사용합니다.
export async function updateReview(username, data) {
  return request(`/api/reviews/${username}`, {
    method: 'PUT',
    body: JSON.stringify(data),
    // headers와 credentials는 request 함수에서 기본값으로 처리됩니다.
  });
}

// DELETE 메서드를 직접 request 함수로 호출합니다.
export async function deleteReview(username) {
  return request(`/api/reviews/${username}`, {
    method: 'DELETE',
    // body가 없으므로 headers, credentials는 request 함수에서 기본값으로 처리됩니다.
  });
}

export default { API_BASE, request, post, get, getReviews, getReviewDetail, createReview, updateReview, deleteReview };
