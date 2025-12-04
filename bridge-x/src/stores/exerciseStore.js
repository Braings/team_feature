import { reactive } from 'vue'
import { post } from '@/api'

// Signup 폼 데이터 전역 상태
export const userExerciseData = reactive({
  // 운동 기록(일지) 추가할수도

  // Reset 함수
  reset() {

  }
})

/**
 * API 호출
 * @returns {Promise} API 응답
 * @throws {Error} API 오류 시 에러 throw
 */
export async function submitExercise() {
  const dataToSend = {
    // 운동 기록(일지) 추가할수도
  }

try {
    const response = await post('/api/userExercise', dataToSend)

    console.log('운동 정보 요청 성공:', response)
    return response
  } catch (error) {
    console.error('운동 정보 요청 실패:', error)
    throw error
  }
}
