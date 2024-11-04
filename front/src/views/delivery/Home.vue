<template>
    <Header />
    <Maps :restaurantLocation="pedidoStore.restaurantLocation ? pedidoStore.restaurantLocation : null"
        :clientLocation="pedidoStore.clientLocation ? pedidoStore.clientLocation : null" />
    <Footer />
</template>

<script setup>
import Header from '@/components/delivery/panel/Header.vue';
import Footer from '@/components/delivery/panel/Footer.vue';
import Maps from '@/components/delivery/panel/Maps.vue';
import { computed, onMounted, onUnmounted, ref } from 'vue';
import pusher from '@/services/pusherOrders';
import { usePedidoStore } from '@/stores/usePedidoStore';
import api from '@/services/api';
import { useRouter } from 'vue-router';

const pedidoStore = usePedidoStore();
const pusherService = pusher;
const router = useRouter();

console.log(pedidoStore);

const user = ref(null);

onMounted(async () => {
    const channel = pusherService.subscribe('pedidos');

    try {
        const { data } = await api.get('delivery/order/');

        if (data.status === "success" && data.data) {
            const orderData = data.data;

            console.log(orderData);

            if (orderData.status && orderData.status.length > 0) {
                pedidoStore.setStatus(orderData.status);
            } else {
                pedidoStore.setStatus("buscando");
            }

            if (orderData.addressRestaurant) {
                pedidoStore.setRestaurantData(orderData.addressRestaurant);
            }

            if (orderData.addressClient) {
                pedidoStore.setClientData(orderData.addressClient);
            }

            pedidoStore.setOrderDetails({
                customerOrderId: orderData.customerOrderId,
                deliveryCost: orderData.deliveryCost,
                expectedTime: orderData.expectedTime,
                nameClient: orderData.nameClient,
                phoneClient: orderData.phoneClient,
                orderItems: orderData.orderItens,
                totalPrice: orderData.totalPrice
            });
        }

    } catch (error) {
        console.log(error);
        localStorage.removeItem('token');
        router.push({ name: 'delivery-login' });
    }

    try {
        const { data } = await api.get('delivery/info/');
        user.value = data.data;

        channel.bind(`entregador_${user.value.id}`, (data) => {
            console.log(data);
            pedidoStore.setStatus(data.status);

            if (data.addressRestaurant) {
                pedidoStore.setRestaurantData(data.addressRestaurant);
            }

            if (data.addressClient) {
                pedidoStore.setClientData(data.addressClient);
            }

            pedidoStore.setOrderDetails({
                customerOrderId: data.customerOrderId,
                deliveryCost: data.deliveryCost,
                expectedTime: data.expectedTime,
                nameClient: data.nameClient,
                phoneClient: data.phoneClient,
                orderItems: data.orderItems,
                totalPrice: data.totalPrice
            });
        });
    } catch (error) {
        console.log(error);
        localStorage.removeItem('token');
        router.push({ name: 'delivery-login' });
    }
});

const updateLocation = async () => {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(async (position) => {
            const currentLocation = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

            try {
                await api.put('/delivery/coordinates/', currentLocation);
            } catch (error) {
                console.error("Erro ao atualizar a localização:", error);
            }
        });
    }
};

let locationInterval = setInterval(updateLocation, 10000);

onUnmounted(() => {
    pusherService.unsubscribe('pedidos');

    if (locationInterval) {
        clearInterval(locationInterval);
    }
});
</script>
