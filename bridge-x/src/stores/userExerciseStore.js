import { reactive } from 'vue'
import { post } from '@/api'

// Signup 폼 데이터 전역 상태
export const userExerciseData = reactive({
  recommend: '',

  // Reset 함수
  reset() {
    this.recommend = ''
  }
})

/**
 * API 호출
 * @returns {Promise} API 응답
 * @throws {Error} API 오류 시 에러 throw
 */
export async function submitExercise() {
  const userExerciseData = {
    username: userExerciseData.recommend,
  }

  try {
    const response = await post('/api/userExercise', userExerciseData)
    console.log('추천운동 불러오기 성공:', response)
    return response
  } catch (error) {
    console.error('추천운동 불러오기 실패:', error)
    throw error
  }
}
