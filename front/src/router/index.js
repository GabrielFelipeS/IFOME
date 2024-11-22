import { createRouter, createWebHistory } from "vue-router";
import Register from "@/views/store/Register.vue";
import api from "@/services/api";

// Definindo as rotas com prefixos aninhados
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      beforeEnter: async (to, from, next) => {
        try {
          const data = await api.post(
            `${import.meta.env.VITE_API_URL}auth/token/client/`
          );
          if (data.status === 200) {
            next();
          } else {
            next({ name: "user-login" });
          }
        } catch (e) {
          localStorage.removeItem("token");
          console.error(e);
          next({ name: "user-login" });
        }
      },
      children: [
        {
          path: "/",
          name: "home-site",
          component: () => import("@/views/site/Home.vue"),
        },
        {
          path: "/restaurants",
          name: "restaurants",
          component: () => import("@/views/site/Restaurants.vue"),
        },
        {
          path: '/dishs',
            name: 'dishs',
            component: () => import('@/views/site/Dishs.vue')
        },
        {
          path: "/restaurant/:id",
          name: "restaurant internal",
          component: () => import("@/views/site/Restaurant.vue"),
        },
        {
          path: "/orders",
          name: "orders",
          component: () => import("@/views/site/Orders.vue"),
        },
        {
          path: "/order/:id",
          name: "order internal",
          component: () => import("@/views/site/Order.vue"),
        },
        {
          path: 'search',
          name: 'search',
          component: () => import('@/views/site/Search.vue')
        }
      ],
    },
    {
      path: "/store",
      children: [
        {
          path: "register",
          name: "store-register",
          component: Register,
        },
        {
          path: "login",
          name: "store-login",
          component: () => import("@/views/store/Login.vue"),
        },
        {
          path: "new-dish",
          name: "store-new-dish",
          component: () => import("@/views/store/NewDish.vue"),
        },
        {
          path: "panel",
          name: "store-panel",
          component: () => import("@/views/store/Panel.vue"),
        },
      ],
    },
    {
      path: "/user",
      children: [
        {
          path: "register",
          name: "user-register",
          component: () => import("../views/user/Register.vue"),
        },
        {
          path: "login",
          name: "user-login",
          component: () => import("../views/user/Login.vue"),
        },
      ],
    },
    {
      path: "/delivery",
      children: [
        {
          path: "register",
          name: "delivery-register",
          component: () => import("../views/delivery/Register.vue"),
        },
        {
          path: "login",
          name: "delivery-login",
          component: () => import("../views/delivery/Login.vue"),
        },
        {
          path: "panel",
          name: "delivery-panel",
          component: () => import("../views/delivery/Home.vue"),
        },
      ],
    },
    {
      path: "/NotFound",
      name: "NotFound",
      component: () => import("@/views/site/NotFound.vue"),
    },
    {
      path: "/:pathMatch(.*)*",
      redirect: { name: "NotFound" },
    },
  ],
});

export default router;
