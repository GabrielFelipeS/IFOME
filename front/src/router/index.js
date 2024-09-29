import { createRouter, createWebHistory } from 'vue-router'
import Register from '@/views/store/Register.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/store/register',
      name: 'store-register',
      component: Register
    },
    {
      path: '/store/login',
      name: 'store-login',
      component: () => import('@/views/store/Login.vue'),
    },
    {
      path: '/user/register',
      name: 'user-register',
      component: () => import('../views/user/Register.vue')
    },
    {
      path: '/user/login',
      name: 'user-login',
      component: () => import('../views/user/Login.vue')
    },
	{
	  path: '/delivery/register',
	  name: 'delivery-register',
	  component: () => import('../views/delivery/Register.vue')
	},
    {
      path: '/delivery/login',
      name: 'delivery-login',
      component: () => import('../views/delivery/Login.vue')
    },
  ]
})

export default router
