import { reactive } from 'vue'
import { post } from '@/api'

// 폼 데이터 전역 상태
export const loginFormData = reactive({
  // Step 1: ID & Password
  username: '',
  nickname: '',
  password: '',
  passwordcheck: '',

  // Step 2: Email & Birthday & Sex
  email: '',
  birthday: '',
  sex: '',

  // Step 3: Health Info
  height: '',
  weight: '',

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
