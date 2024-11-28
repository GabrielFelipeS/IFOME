<template>
    <div class="w-full bg-white rounded-t-3xl drop-shadow-2xl fixed bottom-0 left-0 p-4">
        <div class="w-full flex flex-col justify-center items-center">
            <!-- Barra Vermelha no Topo -->
            <div class="w-16 h-1 bg-red-500 rounded-full mb-2"></div>

            <!-- Título -->
            <h2 class="text-lg font-bold text-red-600">Em Trajeto</h2>

            <!-- Endereço -->
            <div class="mt-4 w-full bg-gray-100 p-3 rounded-lg flex items-start gap-2">
                <span class="w-3 h-3 bg-black rounded-full mt-1"></span>
                <p class="text-gray-700 text-sm">{{ endereco }}</p>
            </div>

            <!-- Botões de Ação -->
            <div class="flex w-full gap-2 mt-4">
                <button @click="$emit('cancelar')" class="w-full bg-primary text-white py-2 rounded-lg font-semibold">Cancelar</button>
                <button class="w-full bg-blue-500 text-white py-2 rounded-lg font-semibold" @click="$emit('chat')">
                    <v-icon name="fa-comment"></v-icon>
                    Chat
                </button>            </div>
            <div class="flex w-full mt-1">
                <button @click="$emit('chegada')" class="w-full bg-green-500 text-white py-4 rounded-lg font-semibold">Chegada</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

// Definindo as props para dados dinâmicos
const props = defineProps({
    endereco: {
        type: String,
        required: true
    }
});

// Definindo os eventos
const emit = defineEmits(['cancelar', 'chegada']);
</script>

<style scoped>
/* Definição de cor primary */

</style>
