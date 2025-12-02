import { reactive } from 'vue'
import { createReview } from '@/api'

// 폼 데이터 전역 상태
export const reviewsData = reactive({
  rating: 0,
  content: '',
  title: '',
  tag: '',
  creationTime: '',
  suggestion: 0,
  views: 0,
  nickname: '',

  // Reset 함수
  reset() {
    this.rating = 0
    this.content = ''
    this.title = ''
    this.tag = ''
    this.creationTime = ''
    this.suggestion = 0
    this.views = 0
    this.nickname = ''
  }
})

/**
 * API 호출
 * @returns {Promise} API 응답
 * @throws {Error} API 오류 시 에러 throw
 */
export async function submitReviews() {
  const reviewsData = {
    rating: reviewsData.rating,
    content: reviewsData.content,
    title: reviewsData.title,
    tag: reviewsData.tag,

    // creationTime: reviewsData.creationTime,
    // suggestion: reviewsData.suggestion,
    // views: reviewsData.views,
    // nickname: reviewsData.nickname,
  }

  try {
    const response = await createReview('/api/reviews', reviewsData)
    console.log('추천운동 불러오기 성공:', response)
    return response
  } catch (error) {
    console.error('추천운동 불러오기 실패:', error)
    throw error
  }
}
