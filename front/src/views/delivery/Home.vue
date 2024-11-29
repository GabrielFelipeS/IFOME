<template>
<!--    <Maps />-->
</template>

<script setup>
import Maps from '@/components/delivery/panel/Maps.vue';
import { onMounted, onUnmounted, ref } from 'vue';
import pusher from '@/services/pusherOrders';
import { usePedidoStore } from '@/stores/usePedidoStore';
import api from '@/services/api';

const pedidoStore = usePedidoStore();
const pusherService = pusher;

const user = ref(null);

onMounted(async () => {
    const channel = pusherService.subscribe('pedidos');

    try {
        const { data } = await api.get('delivery/order/', {}, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (data.status === "success" && data.data) {
            const orderData = data.data;

            console.log('Vindo da API:', orderData);

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
                totalPrice: orderData.totalPrice
            });

            pedidoStore.setOrderItems(orderData.orderItems);
        }

    } catch (error) {
        console.log(error);
        // localStorage.removeItem('token');
        // router.push({ name: 'delivery-login' });
    }

    try {
        const { data } = await api.get('delivery/info/', {}, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`
            }
        });
        user.value = data.data;

        channel.bind(`entregador_${user.value.id}`, (data) => {
            console.log('Vindo do Pusher:', data);
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
                totalPrice: data.totalPrice
            });

            pedidoStore.setOrderItems(data.orderItems);
        });
    } catch (error) {
        console.log(error);
        // localStorage.removeItem('token');
        // router.push({ name: 'delivery-login' });
    }
});

const updateLocation = async () => {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(async (position) => {
            const currentLocation = {
                latitude: position.coords.latitude,
                longitude: position.coords.longitude
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
