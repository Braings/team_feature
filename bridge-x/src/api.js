// 간단한 API 헬퍼 (fetch 기반)
const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080';

async function request(path, options = {}) {
  const url = `${API_BASE}${path}`;
  const res = await fetch(url, options);
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
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
    credentials: 'include'
  }, opts));
}

export async function get(path, opts = {}) {
  return request(path, Object.assign({
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include'
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

export async function updateReview(username, data) {
  return post(`/api/reviews/${username}`, Object.assign({ _method: 'PUT' }, data));
}

export async function deleteReview(username) {
  return request(`/api/reviews/${username}`, {
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include'
  });
}

export default { API_BASE, request, post, get, getReviews, getReviewDetail, createReview, updateReview, deleteReview };
