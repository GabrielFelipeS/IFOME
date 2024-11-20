<template>
    <div class="w-full bg-white rounded-t-3xl drop-shadow-2xl fixed bottom-0 left-0 p-4">
        <div class="w-full flex flex-col justify-center items-center">
            <!-- Barra Vermelha no Topo -->
            <div class="w-16 h-1 bg-red-500 rounded-full mb-2"></div>

            <!-- Título -->
            <h2 class="text-lg font-bold text-red-600">Pedido Pronto</h2>

            <!-- Número do Pedido, Nome do Cliente e Itens do Pedido -->
            <div class="mt-4 w-full bg-gray-100 p-3 rounded-lg text-left">
                <p class="text-gray-700 text-sm mb-2">
                    <span class="font-bold text-black">#{{ pedidoId }}</span> - {{ cliente }}
                </p>
                <ul class="text-gray-600 text-sm list-disc pl-4">
                    <li v-for="(item, index) in itens" :key="index">{{ item }}</li>
                </ul>
            </div>

            <!-- Endereço de Entrega -->
            <div class="mt-4 w-full bg-gray-100 p-3 rounded-lg flex items-start gap-2">
                <span class="w-3 h-3 bg-black rounded-full mt-1"></span>
                <p class="text-gray-700 text-sm">{{ endereco }}</p>
            </div>

            <!-- Botões de Ação -->
            <div class="flex w-full gap-2 mt-4">
                <button @click="$emit('cancelar')" class="w-full bg-red-500 text-white py-2 rounded-lg font-semibold">
                    Cancelar
                </button>
                <button @click="$emit('sairParaEntrega')" class="w-full bg-green-500 text-white py-2 rounded-lg font-semibold">
                    Sair Para Entrega
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
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
    },
    itens: {
        type: Array,
        required: true
    },
    endereco: {
        type: String,
        required: true
    }
});

// Definindo os eventos
const emit = defineEmits(['cancelar', 'sairParaEntrega']);
</script>

<style scoped>
/* Definição de cor primary */

</style>
