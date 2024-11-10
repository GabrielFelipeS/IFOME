<template>
    <div class="w-full h-[100px] border border-soft-border flex flex-row justify-around items-center text-tertiary-light cursor-pointer"
        @click="showData" :class="{ 'animate-pulse': status === 'NOVO' }">
        <div>
            <p class="font-bold">#{{ props.order.orderId }}</p>
            <p>{{ orderHour }}</p>
        </div>
        <div>
            <p class="font-bold">{{ props.order.name }}</p>
            <p>Previsão: --:--</p>
        </div>
        <div>
            <p class="text-white px-5 py-2 rounded-full" :class="currentCardColors">
                {{ status.replace(/_/g, ' ') }}
            </p>
        </div>
    </div>
</template>

<script setup>
import pusher from '@/services/pusherOrders';
import { useOrderStatusStore } from '@/stores/orderStatus';
import { computed, onMounted, onUnmounted } from 'vue';

const props = defineProps(['order']);
const emit = defineEmits(['click', 'status_updated']);
const pusherService = pusher;

const orderStatusStore = useOrderStatusStore();

const status = computed(() =>
    orderStatusStore.getOrderStatus(props.order.orderId) ||
    props.order.orderInfo[props.order.orderInfo.length - 1].orderStatus
);

const orderDate = new Date(props.order.orderDate);
const orderHour = `${orderDate.getHours()}:${orderDate.getMinutes().toString().padStart(2, '0')}`;

const stepColors = {
    'NOVO': 'bg-green-500',
    'EM_PREPARO': 'bg-yellow-500',
    'PRONTO_PARA_ENTREGA': 'bg-blue-500',
    'SAIU_PARA_ENTREGA': 'bg-purple-500',
    'CONCLUIDO': 'bg-red-500',
};

const currentCardColors = computed(() => stepColors[status.value] || 'bg-gray-300');

onMounted(() => {
    const channel = pusherService.subscribe('order-channel');
    channel.bind(`order-status-updated_${props.order.orderId}`, (data) => {
        if (data.orderId === props.order.orderId) {
            orderStatusStore.setOrderStatus(data.orderId, data.status);
            emit('status_updated', data.status);
        }
    });
});

onUnmounted(() => {
    pusherService.unsubscribe('order-channel');
});

function showData() {
    emit('click', props.order.orderId);
}
</script>

<style scoped>
@keyframes pulse {

    0%,
    100% {
        background-color: #fff;
    }

    50% {
        background-color: rgba(144, 238, 144, 0.6);
    }
}

.animate-pulse {
    animation: pulse 1.5s ease-in-out infinite;
}
</style>
