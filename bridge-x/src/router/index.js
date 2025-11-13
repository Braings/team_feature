import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/homePage.vue'
import Sign from '../views/sign.vue'
import Maps from '../views/maps.vue'
import MyPage from '../views/myPage.vue'
import Reviews from '../views/reviews.vue'

const routes = [
  {
    path: '/',
    name: 'homePage',
    component: HomePage
  },
  {
    path: '/sign',
    name: 'sign',
    component: Sign
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
    component: () => import('../views/logIn.vue') // 필요할 때 로드하는 방식 (Lazy Loading)
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
