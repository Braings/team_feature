import { reactive } from 'vue'
import { createReview } from '@/api'

// 폼 데이터 전역 상태
export const reviewsData = reactive({
  content: '',
  title: '',
  tag: '',
  creationTime: '',
  suggestion: 0,
  views: 0,
  nickname: '',

  // Reset 함수
  reset() {
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
    content: reviewsData.content,
    title: reviewsData.title,
    tag: reviewsData.tag,
  }

  try {
    const response = await createReview('/api/reviews', reviewsData)
    console.log('리뷰 불러오기 성공:', response)
    return response
  } catch (error) {
    console.error('리뷰 불러오기 실패:', error)
    throw error
  }
}
