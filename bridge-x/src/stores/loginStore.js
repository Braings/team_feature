import { reactive } from 'vue'
import { post } from '@/api'

// 폼 데이터 전역 상태
export const loginFormData = reactive({
  // 아이디(사용자 이름), 비밀번호
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
export async function submitLogin() {
  // 로그인 데이터 구성
  const dataToSend = {

    username: loginFormData.username,
    password: loginFormData.password,

  }

  try {
    // API 호출
    const response = await post('/api/login', dataToSend)
    console.log('로그인 성공:', response)

    if (response && response.token) {
      // 토큰 저장 (쿠키)
      localStorage.setItem('authToken', response.token);

    } else {
      // 토큰 저장 실패
      throw new Error(response.message || '로그인 인증 정보가 부족합니다.');
    }

    return response;
  } catch (error) {
    console.error('로그인 실패:', error)
    throw error
  }
}
