import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/homePage.vue'
import SignUpID from '../views/signUpID.vue'
import SignUpPrivate from '../views/signUpPrivate.vue'
import SignUpHealth from '../views/signUpHealth.vue'
import ExerciseFacilities from '../views/exerciseFacilities.vue'
import MyPage from '../views/myPage.vue'
import Reviews from '../views/reviewsPage.vue'
import ReviewDetail from '../views/reviewDetail.vue'
import ReviewsWrite from '../views/reviewsWritePage.vue'
import LogIn from '../views/logIn.vue'
import Recommend from '@/views/recommend.vue'

const routes = [
  {
    path: '/',
    name: 'homePage',
    component: HomePage
  },
  {
    path: '/recommend',
    name: 'recommend',
    component: Recommend
  },
  {
    path: '/signup/ID',
    name: 'sign.id',
    component: SignUpID
  },
    {
    path: '/signup/private',
    name: 'sign.private',
    component: SignUpPrivate
  },
    {
    path: '/signup/health',
    name: 'sign.health',
    component: SignUpHealth
  },
  {
    path: '/exerciseFacilities',
    name: 'exerciseFacilities',
    component: ExerciseFacilities
  },
  {
    path: '/myPage',
    name: 'myPage',
    component: MyPage
  },

  {
    path: '/reviews',
    name: 'reviews',
    component: Reviews
  },
  {
    path: '/reviews/:username',
    name: 'reviewDetail',
    component: ReviewDetail
  },
  {
    path: '/reviews/write',
    name: 'reviewsWrite',
    component: ReviewsWrite
  },
  {
    path: '/login',
    name: 'logIn',
    component: LogIn
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
