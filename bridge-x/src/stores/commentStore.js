import { reactive } from 'vue'
import { createComment } from '@/api'

// 새 댓글/대댓글 폼 데이터 전역 상태
export const commentData = reactive({
  // 댓글이 달릴 리뷰의 ID (필수)
  reviewID: '',

  // 댓글의 내용 (필수)
  content: '',

  // 대댓글인 경우 부모 댓글의 ID (선택적)
  parentCommentID: null,

  // 작성자 정보 (필수)
  writer: '',

  // 작성 시간 (서버에서 설정될 수 있으나, 폼에는 포함하지 않을 수 있음)
  creationTime: '',

  // Reset 함수
  reset() {
    this.reviewID = ''
    this.content = ''
    this.parentCommentID = null // 대댓글 작성 후 초기화 시 null로 설정
    this.writer = ''
    this.creationTime = ''
  }
})
/**
 * API 호출
 * @returns {Promise} API 응답
 * @throws {Error} API 오류 시 에러 throw
 */
export async function submitComment() {
  const dataToSend = {
  reviewID: commentData.reviewID,
  content: commentData.content,
  parentCommentID: commentData.parentCommentID,
  writer: commentData.writer,
  }

  try {
    const response = await createComment('/api/comments', dataToSend)
    console.log('댓글 제출 성공:', response)
    return response
  } catch (error) {
    console.error('댓글 제출 실패:', error)
    throw error
  }
}
