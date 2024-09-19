import { createRouter, createWebHistory } from 'vue-router'
import Register from '@/views/Store/Register.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/store/register',
      name: 'store-register',
      component: Register
    }
  ]
})

export default router
