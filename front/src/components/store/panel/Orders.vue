<template>
    <div class="w-[450px] h-[100vh] border-r border-soft-border">
        <!-- FEATURES FUTURAS -->
         <h2 class="text-2xl my-5 ml-2"># Pedidos</h2>
        <!-- <FilterOrders @filter="ordemProducts" /> -->
        <div class="w-full h-[calc(100vh_-_80px)] overflow-y-auto overflow-x-hidden">
            <CardOrder 
                v-for="order in orderProducts"
                :order="order"
                :key="order.orderId"
                @click="showData"
                @status_updated="handleStatusUpdate"
            />
        </div>
    </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue';
import CardOrder from './CardOrder.vue';
import FilterOrders from './FilterOrders.vue';

const props = defineProps({
    orders: {
        type: Array,
        default: () => []
    }
});
const emits = defineEmits(['click']);

const filterStatus = ref('all');

const orderProducts = computed(() => {

    let filteredOrders = filterStatus.value !== 'all' 
        ? props.orders.filter(order => order.orderInfo[order.orderInfo.length - 1].orderStatus === filterStatus.value)
        : props.orders;


    return [...filteredOrders].sort((a, b) => {
        const aStatus = a.orderInfo[a.orderInfo.length - 1].orderStatus;
        const bStatus = b.orderInfo[b.orderInfo.length - 1].orderStatus;

    
        if (aStatus === 'NOVO' && bStatus !== 'NOVO') return -1;
        if (aStatus !== 'NOVO' && bStatus === 'NOVO') return 1;

    
        const statusOrder = ['NOVO', 'EM_PREPARO', 'PRONTO_PARA_ENTREGA', 'SAIU_PARA_ENTREGA', 'CONCLUIDO'];
        return statusOrder.indexOf(aStatus) - statusOrder.indexOf(bStatus);
    });
});

function ordemProducts(status) {
    filterStatus.value = status;
}

function showData(orderId) {
    emits('click', orderId);
}

function handleStatusUpdate() {

    filterStatus.value = filterStatus.value;
}

watch(() => props.orders, () => {

}, { deep: true });

</script>
