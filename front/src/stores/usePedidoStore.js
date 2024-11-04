import { defineStore } from 'pinia';

export const usePedidoStore = defineStore('pedido', {
    state: () => ({
        status: [],
        restaurantData: {},
        clientData: {},
        restaurantLocation: null,
        clientLocation: null,
        customerOrderId: null,
        deliveryCost: null,
        expectedTime: null,
        nameClient: "",
        phoneClient: "",
        orderItems: [],
        totalPrice: null,
    }),

    actions: {
        setStatus(status) {
            this.status = status;
        },
        setRestaurantData(data) {
            this.restaurantData = data;
            this.restaurantLocation = { lat: data.latitude, lng: data.longitude };
        },
        setClientData(data) {
            this.clientData = data;
            this.clientLocation = { lat: data.latitude, lng: data.longitude };
        },
        setOrderDetails(details) {
            this.customerOrderId = details.customerOrderId;
            this.deliveryCost = details.deliveryCost;
            this.expectedTime = details.expectedTime;
            this.nameClient = details.nameClient;
            this.phoneClient = details.phoneClient;
            this.orderItems = details.orderItems;
            this.totalPrice = details.totalPrice;
        },
        resetOrderData() {
            this.status = 'buscando';
            this.restaurantData = {};
            this.clientData = {};
            this.restaurantLocation = null;
            this.clientLocation = null;
            this.customerOrderId = null;
            this.deliveryCost = null;
            this.expectedTime = null;
            this.nameClient = "";
            this.phoneClient = "";
            this.orderItems = [];
            this.totalPrice = null;
        }
    },
});
