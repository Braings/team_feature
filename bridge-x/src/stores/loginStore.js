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
 * 회원가입 API 호출
 * @returns {Promise} API 응답
 * @throws {Error} API 오류 시 에러 throw
 */
export async function submitLogin() {
  // 회원가입 데이터 구성
  const loginFormData = {

    username: loginFormData.username,
    nickname: loginFormData.nickname,
    password: loginFormData.password,
    passwordcheck: loginFormData.passwordcheck,

    email: loginFormData.email,
    birthday: loginFormData.birthday,
    sex: loginFormData.sex,

    height: parseInt(loginFormData.height),
    weight: parseInt(loginFormData.weight),

    user_grip: parseInt(loginFormData.user_grip),
    user_flex: parseInt(loginFormData.user_flex),
    user_situp: parseInt(loginFormData.user_situp),
    user_jump: parseInt(loginFormData.user_jump),
  }

  try {
    // API 호출
    const response = await post('/api/login', loginFormData)
    console.log('로그인 성공:', response)
    return response
  } catch (error) {
    console.error('로그인 실패:', error)
    throw error
  }
}
