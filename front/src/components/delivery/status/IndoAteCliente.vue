<template>
    <div class="w-full bg-white rounded-t-3xl drop-shadow-2xl fixed bottom-0 left-0 p-4">
        <div class="w-full flex flex-col justify-center items-center">
            <!-- Barra Vermelha no Topo -->
            <div class="w-16 h-1 bg-red-500 rounded-full mb-2"></div>

            <!-- Título -->
            <h2 class="text-lg font-bold text-red-600">Indo Até o Cliente</h2>

            <!-- Endereço de Entrega -->
            <div class="mt-4 w-full bg-gray-100 p-3 rounded-lg flex items-start gap-2">
                <span class="w-3 h-3 bg-black rounded-full mt-1"></span>
                <p class="text-gray-700 text-sm">{{ endereco }}</p>
            </div>

            <!-- Botão de Ação -->
            <div class="flex w-full mt-4">
                <button @click="$emit('entregar')" class="w-full bg-green-500 text-white py-2 rounded-lg font-semibold">
                    Entregar
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

// Definindo as props para o endereço
const props = defineProps({
    endereco: {
        type: String,
        required: true
    }
});

// Definindo os eventos
const emit = defineEmits(['entregar']);
</script>

<style scoped>
/* Definição de cor primary */

</style>
