import { reactive } from 'vue'
import { post } from '@/api'

// 폼 데이터 전역 상태
export const loginFormData = reactive({
  // 아이디(사용자 이름), 닉네임, 비밀번호
  username: '',
  nickname: '',
  password: '',
  passwordcheck: '',

  // 이메일, 생일, 성별
  email: '',
  birthday: '',
  sex: '',

  // 신체 정보
  height: '',
  weight: '',

  // 운동 능력 정보
  user_grip: '',
  user_flex: '',
  user_situp: '',
  user_jump: '',

  // Reset 함수
  reset() {

    this.username = ''
    this.password = ''
    this.nickname = ''
    this.passwordcheck = ''

    this.email = ''
    this.birthday = ''
    this.sex = ''

    this.height = ''
    this.weight = ''

    this.user_grip = ''
    this.user_flex = ''
    this.user_situp = ''
    this.user_jump = ''
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
      localStorage.setItem('authToken', response.token);

      localStorage.setItem('nickname', response.nickname || loginFormData.nickname);
      // localStorage.setItem('exercise', response.exercise || '기본 추천');
      // 받을꺼 추가
    } else {
      // 토큰이 없으면 로그인 실패로 간주하고 에러를 던집니다.
      throw new Error(response.message || '로그인 인증 정보가 부족합니다.');
    }

    return response;
  } catch (error) {
    console.error('로그인 실패:', error)
    throw error
  }
}
