import { createRouter, createWebHistory } from 'vue-router';
import Register from '@/views/store/Register.vue';

// Definindo as rotas com prefixos aninhados
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      children:[
        {
          path: '/restaurants',
          name: 'restaurants',
          component: () => import('@/views/site/Restaurants.vue'),
        },
        {
          path: '/restaurant/:id',
          name: 'restaurant internal',
          component: () => import('@/views/site/Restaurant.vue'),
        }
      ]
    },
    {
      path: '/store',
      children: [
        {
          path: 'register',
          name: 'store-register',
          component: Register,
        },
        {
          path: 'login',
          name: 'store-login',
          component: () => import('@/views/store/Login.vue'),
        },
        {
          path: 'new-dish',
          name: 'store-new-dish',
          component: () => import('@/views/store/NewDish.vue'),
        },
      ],
    },
    {
      path: '/user',
      children: [
        {
          path: 'register',
          name: 'user-register',
          component: () => import('../views/user/Register.vue'),
        },
        {
          path: 'login',
          name: 'user-login',
          component: () => import('../views/user/Login.vue'),
        },
      ],
    },
    {
      path: '/delivery',
      children: [
        {
          path: 'register',
          name: 'delivery-register',
          component: () => import('../views/delivery/Register.vue'),
        },
        {
          path: 'login',
          name: 'delivery-login',
          component: () => import('../views/delivery/Login.vue'),
        },
      ],
    },
    {
      path: '/NotFound',
      name: 'NotFound',
      component: () => import('@/views/site/NotFound.vue'),
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: { name: 'NotFound' },
    }
  ],
});

export default router;
