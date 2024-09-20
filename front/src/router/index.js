import { createRouter, createWebHistory } from 'vue-router'
import Register from '@/views/Store/Register.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/store/register',
      name: 'store-register',
      component: Register
    },
    {
      path: '/user/register',
      name: 'user-register',
      component: () => import('../views/user/Register.vue')
    }
  ]
})

export default router
