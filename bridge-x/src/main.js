// main.js

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

// 1. Vuetify 관련 import
import 'vuetify/styles' // Vuetify 기본 스타일 시트
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
// 🌟 필수 추가 🌟
import '@mdi/font/css/materialdesignicons.css' // Material Design Icons 스타일 시트

// 2. Vue 앱 인스턴스를 한 번만 생성합니다.
const app = createApp(App)

// 3. Vuetify 인스턴스 생성 (아이콘 설정 포함)
const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi', // MDI를 기본 아이콘 셋으로 설정
  },
})

// 4. 모든 라이브러리를 하나의 앱 인스턴스에 등록합니다.
app.use(vuetify)
app.use(createPinia())
app.use(router)

// 5. 앱을 한 번만 마운트합니다.
app.mount('#app')