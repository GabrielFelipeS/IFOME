<template>
    <div>
        <BuscandoPedidos v-if="currentStatus === 'buscando'"/>

        <NovoPedido :valor="pedidoStore.deliveryCost" :enderecoColeta="pedidoStore.restaurantData.address"
                    :enderecoDestino="pedidoStore.clientData.address" @negar="handleNegar" @aceitar="handleAceitar"
                    v-if="currentStatus === 'NOVO'"/>

        <EmTrajeto :endereco="pedidoStore.restaurantData.address" @cancelar="handleCancelar" @chegada="handleChegada"
                   @chat="showChat"
                   v-if="currentStatus === 'ACEITO'"/>

        <PedidoEmPreparo :pedidoId="pedidoStore.customerOrderId" :cliente="pedidoStore.nameClient"
                         @chat="showChat"
                         @cancelar="handleCancelar"
                         v-if="currentStatus === 'EM_PREPARO' || currentStatus === 'NO_LOCAL'"/>

        <PedidoPronto :pedidoId="pedidoStore.customerOrderId" :cliente="pedidoStore.nameClient"
                      @chat="showChat"
                      :itens="pedidoStore.orderItems ? pedidoStore.orderItems.map(item => item.dishName) : []"
                      :endereco="pedidoStore.clientData.address" @cancelar="handleCancelar"
                      @sairParaEntrega="handleSairParaEntrega" v-if="currentStatus === 'PRONTO'"/>

        <IndoAteCliente :endereco="pedidoStore.clientData.address" @entregar="handleEntregar" @chat="showChat"
                        v-if="currentStatus === 'A_CAMINHO'"/>

        <ConfirmacaoCodigo @finalizar="handleFinalizar" v-if="currentStatus === 'CONFIRMACAO'"/>

        <OrderChatDelivery :delivery-chat="deliveryChat" :customer-order-id="pedidoStore.customerOrderId" :chat="chat"
                           :isActive="isActive" @closeChat="closeChat"></OrderChatDelivery>
    </div>
</template>

<script setup>
import {computed, onUpdated, ref, watch} from 'vue';
import {usePedidoStore} from '@/stores/usePedidoStore';

import BuscandoPedidos from '@/components/delivery/status/BuscandoPedidos.vue';
import NovoPedido from '@/components/delivery/status/NovoPedido.vue';
import EmTrajeto from '@/components/delivery/status/EmTrajeto.vue';
import PedidoEmPreparo from '@/components/delivery/status/PedidoEmPreparo.vue';
import PedidoPronto from '@/components/delivery/status/PedidoPronto.vue';
import IndoAteCliente from '@/components/delivery/status/IndoAteCliente.vue';
import ConfirmacaoCodigo from '@/components/delivery/status/ConfirmacaoCodigo.vue';
import api from '@/services/api';
import OrderChatDelivery from "@/components/delivery/panel/OrderChatDelivery.vue";

const pedidoStore = usePedidoStore();

const isActive = ref(false);

function showChat() {
    isActive.value = true;
}

function closeChat() {
    isActive.value = false;
}

const currentStatus = computed(() => {
    if (!Array.isArray(pedidoStore.status) || pedidoStore.status.length === 0) {
        return 'buscando';
    }

    const status = pedidoStore.status[pedidoStore.status.length - 1].orderDeliveryStatus;
    if (status === 'CANCELADO' || status === 'CONCLUIDO') {
        pedidoStore.resetOrderData();
        return 'buscando';
    }
    return status;
});

watch(() => pedidoStore.customerOrderId, async (orderId) => {
    if (!orderId) {
        chat.value = null;
        return;
    }
    try {

        const {data} = await api.get(`chat/client/delivery/${pedidoStore.customerOrderId}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
        });
        chat.value = data.data.messages;
    } catch (error) {
        console.error('Erro ao buscar chat:', error);
    }

    try {
        const {data} = await api.get(`chat/restaurant/delivery/${pedidoStore.customerOrderId}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
        });
        deliveryChat.value = data.data.messages;
    } catch (error) {
        deliveryChat.value = [];
    }
})

const chat = ref([]);
const deliveryChat = ref([]);

// Funções para atualizar o status do pedido e manipular as ações do usuário
async function handleNegar() {
    await api.put(`delivery/order/status/${pedidoStore.customerOrderId}/refuse`);
    pedidoStore.resetOrderData();
}

async function handleAceitar() {
    await api.put(`delivery/order/status/${pedidoStore.customerOrderId}`);
}

async function handleCancelar() {
    await api.put(`delivery/order/status/${pedidoStore.customerOrderId}/refuse`);
    pedidoStore.resetOrderData();
}

async function handleChegada() {
    await api.put(`delivery/order/status/${pedidoStore.customerOrderId}`);
}

async function handleSairParaEntrega() {
    await api.put(`delivery/order/status/${pedidoStore.customerOrderId}`);
}

async function handleEntregar() {
    pedidoStore.status.push({
        orderDeliveryStatus: 'CONFIRMACAO',
        localDateTime: new Date().toISOString()
    });
}

async function handleFinalizar() {
    await api.put(`delivery/order/status/${pedidoStore.customerOrderId}`);
    pedidoStore.resetOrderData();
}
</script>
