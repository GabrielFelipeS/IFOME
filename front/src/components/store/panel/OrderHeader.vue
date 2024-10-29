<template>
    <div class="w-full grid grid-cols-3 h-[20vh] border-b ">
        <div class="w-full h-full flex flex-col justify-center items-start pl-5 text-tertiary-light gap-3">
            <div>
                <p class="font-bold">PEDIDO</p>
                <p>#{{ props.order.orderId }}</p>
            </div>
            <div>
                <p class="font-bold">CEP</p>
                <p>{{ props.order.address.cep }}</p>
            </div>
            <div>
                <p class="font-bold">COMPLEMENTO</p>
                <p>{{ props.order.address.complement }}</p>
            </div>
        </div>
        <div class="w-full h-full flex flex-col justify-center items-start pl-5 text-tertiary-light gap-3">
            <div>
                <p class="font-bold">CLIENTE</p>
                <p>{{ props.order.name }}</p>
            </div>
            <div>
                <p class="font-bold">ENDEREÇO</p>
                <p>{{ address }}</p>
            </div>
            <div>
                <p class="font-bold">REFERÊNCIA</p>
                <p>{{ props.order.address.details }}</p>
            </div>
        </div>
        <div class="w-full h-full flex flex-col justify-center items-center pl-5 text-tertiary-light gap-3">
            <button :class="['px-10 rounded py-2 cursor-pointer', buttonClasses]" @click="setState"
                :disabled="isLastStatus">
                {{ buttonText }}
            </button>
            <span>Previsão: --:--</span>
        </div>
    </div>
</template>

<script setup>
import api from '@/services/api';
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

const address = computed(() => {
    return props.order.address.address + ', ' + props.order.address.number;
});


const buttonClasses = computed(() =>
    isLastStatus.value ? 'bg-gray-400 text-white' : 'bg-green-500 text-white'
);

const buttonText = computed(() => {
    switch (currentStatus.value) {
        case 'NOVO':
            return 'Iniciar Preparo';
        case 'EM_PREPARO':
            return 'Concluir Preparo';
        case 'PRONTO':
            return 'Pedido Pronto';
        case 'PRONTO_PARA_ENTREGA':
            return 'Aguardando Entregador';
        default:
            return 'Concluido';
    }
});

const isLastStatus = computed(() => currentStatusIndex.value >= 2);

async function setState() {
    if (isLastStatus.value) {
        console.log('Pedido já concluído');
        return;
    }else{
        await api.put(`order/updateStatus/${props.order.orderId}`);
    }
}
</script>
