import { reactive } from 'vue'
import { post } from '@/api'

// Signup 폼 데이터 전역 상태
export const signupFormData = reactive({
  // Step 1: ID & Password
  username: '',
  password: '',
  passwordcheck: '',

  // Step 2: Email & Birthday & Sex
  email: '',
  birthday: '',
  sex: '',

  // Step 3: Health Info
  height: '',
  weight: '',
  experienceLevel: '',

  // Reset 함수
  reset() {
    this.username = ''
    this.password = ''
    this.email = ''
    this.birthday = ''
    this.sex = ''
    this.height = ''
    this.weight = ''
    this.experienceLevel = ''
  }
})

/**
 * 회원가입 API 호출
 * @returns {Promise} API 응답
 * @throws {Error} API 오류 시 에러 throw
 */
export async function submitSignup() {
  // 회원가입 데이터 구성
  const signupData = {
    username: signupFormData.username,
    password: signupFormData.password,
    email: signupFormData.email,
    birthday: signupFormData.birthday,
    sex: signupFormData.sex,
    height: parseInt(signupFormData.height),
    weight: parseInt(signupFormData.weight),
    experienceLevel: signupFormData.experienceLevel
  }

  try {
    // API 호출
    const response = await post('/api/sign', signupData)
    console.log('회원가입 성공:', response)
    return response
  } catch (error) {
    console.error('회원가입 실패:', error)
    throw error
  }
}
