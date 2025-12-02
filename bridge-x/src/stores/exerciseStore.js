import { reactive } from 'vue'
import { post } from '@/api'

// Signup 폼 데이터 전역 상태
export const userExerciseData = reactive({
  // 추천 운동
  recommend: '',

  // 평균 운동 능력
  avg_grip: '',
  avg_flex: '',
  avg_situp: '',
  avg_jump: '',

  // Reset 함수
  reset() {
    this.recommend = ''
    this.avg_flex = ''
    this.avg_grip = ''
    this.avg_jump = ''
    this.avg_situp = ''
  }
})

/**
 * API 호출
 * @returns {Promise} API 응답
 * @throws {Error} API 오류 시 에러 throw
 */
export async function submitExercise() {
  const payload = {
    recommend: userExerciseData.recommend,
    avg_grip: userExerciseData.avg_grip,
    avg_flex: userExerciseData.avg_flex,
    avg_jump: userExerciseData.avg_jump,
    avg_situp: userExerciseData.avg_situp,
  }

try {
    const response = await post('/api/userExercise', payload)

    if (response && response.recommend) {
      userExerciseData.recommend = response.recommend
    }

    console.log('운동 정보 요청 성공:', response)
    return response
  } catch (error) {
    console.error('운동 정보 요청 실패:', error)
    throw error
  }
}
