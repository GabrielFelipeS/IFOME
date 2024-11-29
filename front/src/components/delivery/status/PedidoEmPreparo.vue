<template>
    <div class="w-full bg-white rounded-t-3xl drop-shadow-2xl fixed bottom-0 left-0 p-4">
        <div class="w-full flex flex-col justify-center items-center">
            <!-- Barra Vermelha no Topo -->
            <div class="w-16 h-1 bg-red-500 rounded-full mb-2"></div>

            <!-- Título -->
            <h2 class="text-lg font-bold text-red-600">Pedido em preparo</h2>

            <!-- Número do Pedido e Nome do Cliente -->
            <div class="mt-4 w-full bg-gray-100 p-3 rounded-lg text-center">
                <p class="text-gray-700 text-sm">
                    <span class="font-bold text-black">#{{ pedidoId }}</span> - {{ cliente }}
                </p>
            </div>

            <!-- Botões de Ação -->
            <div class="flex w-full gap-2 mt-4">
                <button @click="$emit('cancelar')" class="w-full bg-red-500 text-white py-2 rounded-lg font-semibold">
                    Cancelar
                </button>

                <button class="w-full bg-blue-500 text-white py-2 rounded-lg font-semibold" @click="$emit('chat')">
                    <v-icon name="fa-comment"></v-icon>
                    Chat
                </button>
            </div>
            <div class="flex w-full mt-1">
                <button
                    :disabled="currentStatus !== 'PRONTO'"
                    @click="$emit('sairParaEntrega')"
                    :class="{
                        'bg-gray-400': currentStatus !== 'PRONTO',
                        'bg-green-500': currentStatus === 'PRONTO'
                    }"
                    class="w-full text-white py-5 rounded-lg font-semibold"
                >
                    Sair Para Entrega
                </button>
            </div>

        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue';
import { usePedidoStore } from '@/stores/usePedidoStore';
import { defineProps, defineEmits } from 'vue';

// Definindo as props para dados dinâmicos
const props = defineProps({
    pedidoId: {
        type: String,
        required: true
    },
    cliente: {
        type: String,
        required: true
    }
});

// Definindo os eventos
const emit = defineEmits(['cancelar', 'sairParaEntrega', 'chat']);

const pedidoStore = usePedidoStore();

const currentStatus = computed(() => {
    // Pega o último status do array de status
    return pedidoStore.status.length > 0 ? pedidoStore.status[pedidoStore.status.length - 1].orderDeliveryStatus : 'buscando';
});
</script>

<style scoped>
/* Definição de cor primary */
</style>
