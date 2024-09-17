import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/store/register',
      name: 'store-register',
      component: () => import('../views/Store/Register.vue')
    },
    {
      path: '/user/register',
      name: 'user-register',
      component: () => import('../views/user/Register.vue')
    }
  ]
})

export default router
