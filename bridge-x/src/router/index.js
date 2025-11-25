import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/homePage.vue'
import SignUpID from '../views/signUpID.vue'
import SignUpPrivate from '../views/signUpPrivate.vue'
import SignUpHealth from '../views/signUpHealth.vue'
import Maps from '../views/maps.vue'
import MyPage from '../views/myPage.vue'
import Reviews from '../views/reviews.vue'
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
