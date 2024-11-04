<template>
    <div class="w-full bg-white rounded-t-3xl drop-shadow-2xl fixed bottom-0 left-0 p-4">
        <div class="w-full flex flex-col justify-center items-center">
            <!-- Barra Vermelha no Topo -->
            <div class="w-16 h-1 bg-red-500 rounded-full mb-2"></div>

            <!-- Título -->
            <h2 class="text-lg font-bold text-red-600">Código</h2>

            <!-- Campo de Entrada para Código -->
            <div class="mt-4 w-full bg-gray-100 p-3 rounded-lg flex items-center gap-2">
                <!-- Ícone de Cadeado -->
                <span class="text-red-500 text-lg">
                    <i class="fas fa-lock"></i>
                </span>
                <MaskInput 
                    mask="####"
                    v-model="codigo"
                    class="bg-gray-100 text-center text-2xl text-red-500 w-full outline-none"
                    placeholder="0000"
                />
            </div>

            <!-- Botão de Ação -->
            <div class="flex w-full mt-4">
                <button @click="finalizar" class="w-full bg-green-500 text-white py-2 rounded-lg font-semibold">
                    Finalizar
                </button>
            </div>

            <!-- Mensagem de Erro -->
            <p v-if="erro" class="text-red-500 mt-2">Código incorreto. Tente novamente.</p>
        </div>
    </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';
import { MaskInput } from 'vue-3-mask';
import { usePedidoStore } from '@/stores/usePedidoStore';

const codigo = ref("");
const erro = ref(false);
const emit = defineEmits(['finalizar']);
const pedidoStore = usePedidoStore();
console.log('phoneClient:', pedidoStore.phoneClient);

function finalizar() {
    const phoneClient = pedidoStore.phoneClient || "";
    const ultimosQuatroDigitos = phoneClient.slice(-4);

    if (codigo.value === ultimosQuatroDigitos) {
        erro.value = false;
        emit('finalizar', codigo.value);
    } else {
        erro.value = true;
    }
}
</script>

