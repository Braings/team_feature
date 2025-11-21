import { reactive } from 'vue'

// Signup 폼 데이터 전역 상태
export const signupFormData = reactive({
  // Step 1: ID & Password
  id: '',
  password: '',

  // Step 2: Email & Birthday
  email: '',
  birthday: '',

  // Step 3: Health Info
  height: '',
  weight: '',
  experienceLevel: '',

  // Reset 함수
  reset() {
    this.id = ''
    this.password = ''
    this.email = ''
    this.birthday = ''
    this.height = ''
    this.weight = ''
    this.experienceLevel = ''
  }
})
