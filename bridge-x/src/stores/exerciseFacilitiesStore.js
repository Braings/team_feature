import { reactive } from 'vue'
import { loadExerciseFacilities } from '@/api'

// 폼 데이터 전역 상태
export const exerciseFacilitiesData = reactive({
  selectedRegion :'',
  selectedCity :'',
  selectedRegionElement :'',


  // Reset 함수
  reset() {
    this.selectedRegion = ''
    this.selectedCity = ''
    this.selectedRegionElement = ''
  }
})

/**
 * API 호출
 * @returns {Promise} API 응답
 * @throws {Error} API 오류 시 에러 throw
 */
export async function ExerciseFacilities() {
  const exerciseFacilitiesData = {
    selectedRegion: exerciseFacilitiesData.selectedRegion,
    selectedCity: exerciseFacilitiesData.selectedCity,
    selectedRegionElement: exerciseFacilitiesData.selectedRegionElement,
  }

  try {
    const response = await loadExerciseFacilities('/api/reviews', exerciseFacilitiesData)
    console.log('운동시설 불러오기 성공:', response)
    return response
  } catch (error) {
    console.error('운동시설 불러오기 실패:', error)
    throw error
  }
}
