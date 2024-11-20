<template>
    <div>
        <BuscandoPedidos v-if="currentStatus === 'buscando'" />

        <NovoPedido :valor="pedidoStore.deliveryCost" :enderecoColeta="pedidoStore.restaurantData.address"
            :enderecoDestino="pedidoStore.clientData.address" @negar="handleNegar" @aceitar="handleAceitar"
            v-if="currentStatus === 'NOVO'" />

        <EmTrajeto :endereco="pedidoStore.restaurantData.address" @cancelar="handleCancelar" @chegada="handleChegada"
            v-if="currentStatus === 'ACEITO'" />

        <PedidoEmPreparo :pedidoId="pedidoStore.customerOrderId" :cliente="pedidoStore.nameClient"
            @cancelar="handleCancelar" v-if="currentStatus === 'EM_PREPARO' || currentStatus === 'NO_LOCAL'" />

        <PedidoPronto :pedidoId="pedidoStore.customerOrderId" :cliente="pedidoStore.nameClient"
            :itens="pedidoStore.orderItems ? pedidoStore.orderItems.map(item => item.dishName) : []"
            :endereco="pedidoStore.clientData.address" @cancelar="handleCancelar"
            @sairParaEntrega="handleSairParaEntrega" v-if="currentStatus === 'PRONTO'" />

        <IndoAteCliente :endereco="pedidoStore.clientData.address" @entregar="handleEntregar"
            v-if="currentStatus === 'A_CAMINHO'" />

        <ConfirmacaoCodigo @finalizar="handleFinalizar" v-if="currentStatus === 'CONFIRMACAO'" />
    </div>
</template>

<script setup>
import { computed, onUpdated, watch } from 'vue';
import { usePedidoStore } from '@/stores/usePedidoStore';

import BuscandoPedidos from '@/components/delivery/status/BuscandoPedidos.vue';
import NovoPedido from '@/components/delivery/status/NovoPedido.vue';
import EmTrajeto from '@/components/delivery/status/EmTrajeto.vue';
import PedidoEmPreparo from '@/components/delivery/status/PedidoEmPreparo.vue';
import PedidoPronto from '@/components/delivery/status/PedidoPronto.vue';
import IndoAteCliente from '@/components/delivery/status/IndoAteCliente.vue';
import ConfirmacaoCodigo from '@/components/delivery/status/ConfirmacaoCodigo.vue';
import api from '@/services/api';

const pedidoStore = usePedidoStore();

// Computed para obter o status atual
const currentStatus = computed(() => {
    // Se `pedidoStore.status` estiver vazio, retornar "buscando"
    if (!Array.isArray(pedidoStore.status) || pedidoStore.status.length === 0) {
        return 'buscando';
    }

    // Quando `pedidoStore.status` está carregado, usa o status atual
    const status = pedidoStore.status[pedidoStore.status.length - 1].orderDeliveryStatus;
    if (status === 'CANCELADO' || status === 'CONCLUIDO') {
        pedidoStore.resetOrderData();
        return 'buscando';
    }
    return status;
});

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
