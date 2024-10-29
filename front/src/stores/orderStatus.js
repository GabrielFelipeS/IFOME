import { defineStore } from 'pinia';

export const useOrderStatusStore = defineStore('orderStatus', {
  state: () => ({
    orders: {},
  }),
  actions: {
    setOrderStatus(orderId, newStatus) {
      this.orders[orderId] = newStatus;
    },
  },
  getters: {
    getOrderStatus: (state) => (orderId) => state.orders[orderId] || null,
  },
});
