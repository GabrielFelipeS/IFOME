<template>
    <div class="flex flex-row">
        <SidebarMenu />
        <main class="h-[100vh] w-[calc(100vw_-_150px)] flex flex-row">
            <Orders />
            <div class="h-[100vh] w-[calc(100vw_-_450px)] flex flex-col" v-if="order">
                <OrderHeader />
                <div class="w-full h-[60vh] grid grid-cols-2">
                    <OrderItems />
                    <OrderChat />
                </div>
                <OrderStatus />
            </div>

            <div class="h-[100vh] w-[calc(100vw_-_450px)] flex flex-col items-center justify-center">
                <h1 class="text-3xl text-tertiary-subtle">
                    Selecione um pedido para visualizar detalhes
                </h1>
            </div>
        </main>
    </div>
</template>


<script setup>
import OrderChat from '@/components/store/panel/OrderChat.vue';
import OrderHeader from '@/components/store/panel/OrderHeader.vue';
import OrderItems from '@/components/store/panel/OrderItems.vue';
import Orders from '@/components/store/panel/Orders.vue';
import OrderStatus from '@/components/store/panel/OrderStatus.vue';
import SidebarMenu from '@/components/store/panel/sidebarMenu.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const order = ref(null);
</script>