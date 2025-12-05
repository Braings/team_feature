import { reactive } from 'vue'
import { post } from '@/api'

// 폼 데이터 전역 상태
export const userInfoData = reactive({
  // 닉네임
  nickname: '',

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
export async function submitUserProfileData() {
  // 데이터 구성
  const dataToSend = {
    // 닉네임
    nickname: userInfoData.nickname,

    // 이메일, 생일, 성별
    email: userInfoData.email,
    birthday: userInfoData.birthday,
    sex: userInfoData.sex,

    // 신체 정보
    height: userInfoData.height,
    weight: userInfoData.weight,

    // 운동 능력 정보
    user_grip: userInfoData.user_grip,
    user_flex: userInfoData.user_flex,
    user_situp: userInfoData.user_situp,
    user_jump: userInfoData.user_jump,
  }

  try {
    // API 호출
    const response = await post('/api/users/profile', dataToSend)
    console.log('로그인 성공:', response)

    if (response) {
      localStorage.setItem('username', response.username || '' );
      localStorage.setItem('email', response.email || '' );
      localStorage.setItem('birthday', response.birthday || '' );
      localStorage.setItem('sex', response.sex || '' );
      localStorage.setItem('height', response.height || '' );
      localStorage.setItem('weight', response.weight || '' );
      localStorage.setItem('user_grip', response.user_grip || '' );
      localStorage.setItem('user_flex', response.user_flex || '' );
      localStorage.setItem('user_jump', response.user_jump || '' );
      localStorage.setItem('user_situp', response.user_situp || '' );

    } else {
      throw new Error(response.message || '회원 정보 불러오기에 실패했습니다.');
    }

    return response;
  } catch (error) {
    console.error('회원 정보 불러오기 실패:', error)
    throw error
  }
}
