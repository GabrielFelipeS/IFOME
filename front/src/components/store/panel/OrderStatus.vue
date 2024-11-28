<template>
    <div class="w-[98%] h-[18vh] m-auto bg-white p-4 rounded-lg shadow-md border">
        <!-- Cabeçalho do Histórico -->
        <div class="text-gray-700 font-semibold mb-4">Histórico</div>

        <!-- Linha de Status -->
        <div class="flex justify-around items-center">
            <div v-for="(state, index) in states" :key="state" class="flex flex-col items-center text-center">
                <v-icon name="fa-check-circle" class="w-8 h-8" :class="{
                    'text-red-500': index <= currentStatusIndex,
                    'text-tertiary-light': index > currentStatusIndex
                }" />
                <p
                    :class="{ 'text-red-500': index <= currentStatusIndex, 'text-gray-700': index > currentStatusIndex }">
                    {{ state.replaceAll('_',' ') }}
                </p>
            </div>
        </div>
    </div>
</template>

<script setup>
import { useOrderStatusStore } from '@/stores/orderStatus';
import { computed } from 'vue';

const props = defineProps(['order']);

const states = [
    'NOVO',
    'EM_PREPARO',
    'PRONTO_PARA_ENTREGA',
    'SAIU_PARA_ENTREGA',
    'CONCLUIDO',
];

const orderStatusStore = useOrderStatusStore();
const currentStatus = computed(() => orderStatusStore.getOrderStatus(props.order.orderId) || props.order.orderInfo[props.order.orderInfo.length - 1].orderStatus);
const currentStatusIndex = computed(() => states.indexOf(currentStatus.value));

</script>