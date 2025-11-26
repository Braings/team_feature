import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/homePage.vue'
import SignUpID from '../views/signUpID.vue'
import SignUpPrivate from '../views/signUpPrivate.vue'
import SignUpHealth from '../views/signUpHealth.vue'
import Maps from '../views/mapsPage.vue'
import MyPage from '../views/myPage.vue'
import Reviews from '../views/reviewsPage.vue'
import ReviewDetail from '../views/reviewDetail.vue'
import ReviewsWrite from '../views/reviewsWritePage.vue'
import LogIn from '../views/logIn.vue'

const routes = [
  {
    path: '/',
    name: 'homePage',
    component: HomePage
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
    path: '/maps',
    name: 'maps',
    component: Maps
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
    path: '/reviews/write/:username',
    name: 'reviewsEdit',
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
