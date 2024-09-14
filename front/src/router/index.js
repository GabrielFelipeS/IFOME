import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/store/register',
      name: 'store-register',
      component: () => import('../views/store/Register.vue')
    }
  ]
})

export default router
