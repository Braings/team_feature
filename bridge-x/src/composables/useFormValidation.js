import { reactive } from 'vue';

/**
 * 폼 검증 로직을 관리하는 composable
 * @param {Object} validationRules - 검증 규칙 설정
 * @returns {Object} 검증 함수 및 에러 상태
 */
export function useFormValidation(validationRules) {
  const errors = reactive({});

  // 초기화: 모든 필드의 에러 메시지 초기화
  Object.keys(validationRules).forEach(field => {
    errors[field] = '';
  });

  /**
   * 단일 필드 검증
   * @param {string} field - 검증할 필드명
   * @param {*} value - 검증할 값
   * @returns {boolean} 검증 결과
   */
  const validateField = (field, value) => {
    const rules = validationRules[field];

    if (!rules) return true;

    // 필수 입력 확인
    if (!value || !value.toString().trim()) {
      errors[field] = rules.messages.empty;
      return false;
    }

    // 최소 길이 확인
    if (rules.minLength && value.trim().length < rules.minLength) {
      errors[field] = rules.messages.minLength;
      return false;
    }

    // 최대 길이 확인
    if (rules.maxLength && value.trim().length > rules.maxLength) {
      errors[field] = rules.messages.maxLength;
      return false;
    }

    // 패턴 확인
    if (rules.pattern && !rules.pattern.test(value.trim())) {
      errors[field] = rules.messages.pattern;
      return false;
    }

    // 커스텀 검증 함수
    if (rules.validate && !rules.validate(value)) {
      errors[field] = rules.messages.custom;
      return false;
    }

    errors[field] = '';
    return true;
  };

  /**
   * 모든 필드 검증
   * @param {Object} formData - 폼 데이터
   * @returns {boolean} 전체 검증 결과
   */
  const validateForm = (formData) => {
    const results = Object.keys(validationRules).map(field =>
      validateField(field, formData[field])
    );
    return results.every(result => result === true);
  };

  /**
   * 특정 필드 에러 초기화
   * @param {string} field - 초기화할 필드명
   */
  const clearError = (field) => {
    if (field in errors) {
      errors[field] = '';
    }
  };

  return {
    errors,
    validateField,
    validateForm,
    clearError
  };
}
