<template>
  <div class="signup-page">
    <!-- moving background image -->
    <div class="bg" :style="{ backgroundImage: `url(${bgImage})` }"></div>
    <div class="signup-card">
      <h1 class="title">BRIDGE-X</h1>

      <form class="input-area" @submit.prevent="handleNext">
        <FormField
          v-model="formData.email"
          type="email"
          placeholder="EMAIL"
          :error="errors.email"
          @blur="validateField('email')"
          @input="validateField('email')"
        />
          <select
            :style="{ color: '#888888' }"
            v-model="formData.sex"
            class="select-field"
            @blur="validateField('sex')"
          >
            <option value="" disabled selected hidden >select sex</option>
            <option :style="{ color: 'black' }" value="MALE">Male</option>
            <option :style="{ color: 'black' }" value="FEMALE">Female</option>
            <!-- <option value="other">Other</option> -->
          </select>
        <div class="birthday-wrapper">
          <div class="birthday-input-wrapper">
            <input
              type="text"
              v-model="formData.birthday"
              @input="onBirthdayInput"
              @blur="validateField('birthday')"
              @click="toggleDatePicker"
              inputmode="numeric"
              placeholder="YYYY-MM-DD"
              class="input-field birthday-field"
            />
            <div v-if="showDatePicker" class="date-picker-popover">
              <input
                type="date"
                v-model="datePickerValue"
                @input="onDatePickerChange"
                class="date-input"
              />
            </div>
          </div>
          <div v-if="errors.birthday" class="error-text">{{ errors.birthday }}</div>
        </div>

        <button type="submit" class="next-button">NEXT</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import router from '@/router';
import FormField from '@/components/FormField.vue';
import { useFormValidation } from '@/composables/useFormValidation';
import { signupFormData } from '@/stores/signupStore';
import bgImage from '@/img/gym.png';

// ========================
// Data
// ========================
const formData = reactive({
  email: '',
  birthday: '',
  sex: ''
});

const MIN_AGE = 13;

// ========================
// Birthday Picker State
// ========================
const showDatePicker = ref(false);
const datePickerValue = ref('');

// ========================
// Validation Rules
// ========================
const VALIDATION_RULES = {
  email: {
    pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
    messages: {
      empty: '이메일을 입력하세요.',
      pattern: '유효한 이메일 형식을 입력하세요.'
    }
  },
  birthday: {
    pattern: /^\d{4}-\d{2}-\d{2}$/,
    validate: (value) => validateBirthdayDate(value),
    messages: {
      empty: '생년월일을 입력하세요.',
      pattern: '날짜 형식은 YYYY-MM-DD입니다.',
      custom: '유효한 생년월일을 입력하세요.'
    }
  }
};

// ========================
// Form Validation
// ========================
const { errors, validateField, validateForm } = useFormValidation(VALIDATION_RULES, formData);

// ========================
// Birthday Validation Functions
// ========================
const validateBirthdayDate = (value) => {
  if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) return false;

  const [yStr, mStr, dStr] = value.split('-');
  const y = Number(yStr), m = Number(mStr), d = Number(dStr);



  // 월 유효성
  if (m < 1 || m > 12) return false;

  // 일 유효성
  const mdays = new Date(y, m, 0).getDate();
  if (d < 1 || d > mdays) return false;

  // 미래 날짜 확인
  const birthDate = new Date(y, m - 1, d);
  const today = new Date();
  if (birthDate > today) {
    errors.birthday = '미래 날짜는 입력할 수 없습니다.';
    return false;
  }

  // 최소 연령 확인
  let age = today.getFullYear() - y;
  const mDiff = today.getMonth() + 1 - m;
  const dDiff = today.getDate() - d;
  if (mDiff < 0 || (mDiff === 0 && dDiff < 0)) age -= 1;

  if (age < MIN_AGE) {
    errors.birthday = `만 ${MIN_AGE}세 이상만 가입 가능합니다.`;
    return false;
  }

  return true;
};

// ========================
// Birthday Input Handlers
// ========================
const onBirthdayInput = (e) => {
  const raw = e.target.value || '';
  const digits = raw.replace(/\D+/g, '').slice(0, 8);

  let formatted = digits;
  if (digits.length >= 5) {
    formatted = digits.slice(0, 4) + '-' + digits.slice(4, 6) + (digits.length > 6 ? '-' + digits.slice(6) : '');
  } else if (digits.length >= 4) {
    formatted = digits.slice(0, 4) + '-' + digits.slice(4);
  }

  formData.birthday = formatted;

  // Sync with date picker
  if (/^\d{4}-\d{2}-\d{2}$/.test(formatted)) {
    datePickerValue.value = formatted;
  } else {
    datePickerValue.value = '';
  }
};

const toggleDatePicker = () => {
  showDatePicker.value = !showDatePicker.value;
  if (showDatePicker.value && /^\d{4}-\d{2}-\d{2}$/.test(formData.birthday)) {
    datePickerValue.value = formData.birthday;
  } else if (showDatePicker.value) {
    datePickerValue.value = '';
  }
};

const onDatePickerChange = (e) => {
  const v = e.target.value || '';
  datePickerValue.value = v;
  formData.birthday = v;
  errors.birthday = '';
  showDatePicker.value = false;
};

// ========================
// Event Handlers
// ========================
const handleNext = async () => {
  if (!validateForm(formData)) {
    console.log('❌ 폼 검증 실패');
    return;
  }

  console.log('✓ 폼 검증 성공');

  // 전역 store에 데이터 저장
  signupFormData.email = formData.email;
  signupFormData.birthday = formData.birthday;
  signupFormData.sex = formData.sex;

  // 다음 단계로 이동
  router.push({ name: 'sign.health' });
};
</script>

<style lang="scss" scoped>
@import '@/styles/_variables.scss';

.signup-page {
  @include flex-center;
  position: relative;
  overflow: hidden;
  min-height: 100vh;
  background-color: map-get($colors, 'dark');

  .bg {
    position: absolute;
    inset: 0;
    z-index: 1;
    background-size: cover;
    background-position: center;
    filter: brightness(0.65) blur(1px);
    transform-origin: center;

    @keyframes bg-pan {
      0% { transform: scale(1) translate3d(0,0,0); }
      50% { transform: scale(1.06) translate3d(-4%, -2%, 0); }
      100% { transform: scale(1) translate3d(0,0,0); }
    }
  }

  .signup-card {
    @include card;
    position: relative;
    z-index: 2;
    padding: map-get($spacing, '2xl') map-get($spacing, '3xl');
    width: map-get($sizes, 'card-width');
    display: flex;
    flex-direction: column;
    align-items: center;

    .title {
      font-size: map-get($typography, 'title');
      font-weight: bold;
      margin-bottom: map-get($spacing, '2xl');
      letter-spacing: 5px;
    }

    .input-area {
      @include flex-column;
      width: 100%;

      .select-field {
        width: 100%;
        padding: map-get($spacing, 'lg') map-get($spacing, 'md');
        margin-bottom: map-get($spacing, 'xl');
        border: 1px solid map-get($colors, 'border');
        border-radius: map-get($radius, 'md');
        font-size: map-get($typography, 'base');
        background-color: map-get($colors, 'white');
        cursor: pointer;
        box-sizing: border-box;

        &:focus {
          outline: none;
          border-color: darken(map-get($colors, 'border'), 10%);
        }

        option[value=""] {
          color: map-get($colors, 'text-placeholder');
        }
      }

      .input-field {
        @include input-base;

        &.birthday-field {
          margin-bottom: 0;
        }
      }

      .error-text {
        @include error-text;
        margin-bottom: map-get($spacing, 'lg');
      }

      .birthday-wrapper {
        width: 100%;
        margin-bottom: map-get($spacing, 'xl');

        .birthday-input-wrapper {
          width: 100%;
          display: flex;
          gap: map-get($spacing, 'sm');
          align-items: center;
          position: relative;

          .date-picker-popover {
            position: absolute;
            top: calc(100% + 8px);
            right: 0;
            background: map-get($colors, 'white');
            padding: map-get($spacing, 'sm');
            border: 1px solid map-get($colors, 'border-light');
            border-radius: map-get($radius, 'lg');
            box-shadow: map-get($shadows, 'popover');
            z-index: 10;

            .date-input {
              width: map-get($sizes, 'date-input-width');
              padding: map-get($spacing, 'sm') map-get($spacing, 'md');
              border: 1px solid map-get($colors, 'border');
              border-radius: map-get($radius, 'sm');
            }
          }
        }
      }

      .next-button {
        @include button-base;
        @include flex-center;
        background-color: map-get($colors, 'black');
        color: map-get($colors, 'white');
        padding: 15px 30px;
        border-radius: map-get($radius, 'md');
        font-size: map-get($typography, 'button');
        margin-top: map-get($spacing, 'xl');
        align-self: flex-end;
        width: map-get($sizes, 'button-width');
        height: map-get($sizes, 'button-height');

        &:hover {
          background-color: map-get($colors, 'gray-hover');
        }
      }
    }
  }
}
</style>



