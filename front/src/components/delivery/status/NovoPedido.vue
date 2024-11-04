<template>
    <div class="w-full bg-white rounded-t-3xl drop-shadow-2xl fixed bottom-0 left-0 p-4">
        <div class="w-full flex flex-col justify-center items-center">
            <div class="w-16 h-1 bg-red-500 rounded-full mb-2"></div>

            <h2 class="text-3xl font-bold text-red-600">
                R$ {{ new Intl.NumberFormat('pt-BR', {
                    minimumFractionDigits: 2, maximumFractionDigits: 2
                }).format(valor) }}
            </h2>

            <div class="mt-4 w-full bg-gray-100 p-3 rounded-lg flex flex-col gap-2">
                <div class="flex items-start gap-2">
                    <span class="w-3 h-3 bg-black rounded-full mt-1"></span>
                    <p class="text-gray-700 text-sm">{{ enderecoColeta }}</p>
                </div>

                <div class="w-px h-4 bg-gray-400 mx-2"></div>

                <div class="flex items-start gap-2">
                    <span class="w-3 h-3 bg-black rounded-full mt-1"></span>
                    <p class="text-gray-700 text-sm">{{ enderecoDestino }}</p>
                </div>
            </div>

            <div class="flex w-full gap-2 mt-4">
                <button @click="$emit('negar')"
                    class="w-full bg-primary text-white py-2 rounded-lg font-semibold">Negar</button>
                <button @click="$emit('aceitar')"
                    class="w-full bg-green-500 text-white py-2 rounded-lg font-semibold">Aceitar</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
    valor: {
        type: String,
        required: true
    },
    enderecoColeta: {
        type: String,
        required: true
    },
    enderecoDestino: {
        type: String,
        required: true
    }
});

const emit = defineEmits(['negar', 'aceitar']);
</script>

<style scoped></style>
