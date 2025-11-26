import { reactive } from 'vue'
import { post } from '@/api'

// Signup 폼 데이터 전역 상태
export const loginFormData = reactive({
  username: '',
  password: '',
  // Reset 함수
  reset() {
    this.username = ''
    this.password = ''
  }
})

/**
 * 로그인 API 호출
 * @returns {Promise} API 응답
 * @throws {Error} API 오류 시 에러 throw
 */
export async function submitSignup() {
  // 로그인 데이터 구성
  const loginData = {
    username: loginFormData.username,
    password: loginFormData.password,
  }

  try {
    // API 호출
    const response = await post('/api/login', loginData)
    console.log('로그인 성공:', response)
    return response
  } catch (error) {
    console.error('로그인 실패:', error)
    throw error
  }
}
