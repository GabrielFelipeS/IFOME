<template>
    <div class="flex flex-row" v-if="!isMobile">
        <SidebarMenu />
        <main class="h-[100vh] w-[calc(100vw_-_150px)] flex flex-row">
            <Orders :orders="orders" @click="showOrderDetails" />
            <div class="h-[100vh] lg:w-[calc(100vw_-_450px)] md:w-[calc(100vw_-_250px)] flex flex-col" v-if="currentOrder">
                <OrderHeader :order="currentOrder" />
                <div class="w-full h-[60vh] grid grid-cols-2">
                    <OrderItems :order="currentOrder" />
                    <OrderChat />
                </div>
                <OrderStatus :order="currentOrder" />
            </div>

            <div class="h-[100vh] lg:w-[calc(100vw_-_450px)] md:w-[calc(100vw_-_250px)] flex flex-col items-center justify-center"
                v-if="!currentOrder">
                <h1 class="text-3xl text-tertiary-subtle">
                    Selecione um pedido para visualizar detalhes
                </h1>
            </div>
        </main>
    </div>

    <div v-else class="h-[100vh] flex items-center justify-center text-center p-4 bg-red-100">
        <h1 class="text-2xl font-semibold text-red-600">Este painel não é adequado para dispositivos móveis ou com resolução menor que 1280px. Por favor, utilize em um dispositivo maior.</h1>
    </div>
</template>

<script setup>
import OrderChat from '@/components/store/panel/OrderChat.vue';
import OrderHeader from '@/components/store/panel/OrderHeader.vue';
import OrderItems from '@/components/store/panel/OrderItems.vue';
import Orders from '@/components/store/panel/Orders.vue';
import OrderStatus from '@/components/store/panel/OrderStatus.vue';
import SidebarMenu from '@/components/store/panel/sidebarMenu.vue';
import api from '@/services/api';
import axios from 'axios';
import { computed, ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';

const orders = ref([]);
const selectedOrderId = ref(null);
const windowWidth = ref(window.innerWidth);
const router = useRouter();

function updateWindowWidth() {
    windowWidth.value = window.innerWidth;
}

const isMobile = computed(() => windowWidth.value < 1280);

onMounted(async () => {
    try {
        if(localStorage.getItem('token') === null) {
            return router.push({ name: 'store-login' });
        }
        const { data } = await api.get('order/restaurantOrders');

        orders.value = data;
    } catch (error) {
        if (error.response && error.response.status === 401) {
            await localStorage.removeItem('token');
            return router.push({ name: 'store-login' });
        } else {
            console.error('Erro ao buscar pedidos:', error);
        }
    }
});

onUnmounted(() => {
    window.removeEventListener('resize', updateWindowWidth);
});

function showOrderDetails(id) {
    selectedOrderId.value = id;
}

const currentOrder = computed(() => {
    console.log(orders.value);
    return orders.value.find(order => order.orderId === selectedOrderId.value) || null;
});

</script>
