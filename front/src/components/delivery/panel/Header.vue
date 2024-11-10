<template>
    <div class="w-full h-[100px] bg-white flex flex-row items-center justify-around">
        <v-icon name="fa-user" class="bg-primary text-white p-3 rounded-full" scale="2.5"></v-icon>
        <div class="px-7 py-3 text-white rounded-lg cursor-pointer flex flex-row items-center gap-1"
            :class="status.status == true ? 'bg-green-500' : 'bg-gray-500'" @click="setStatus">
            <span class="w-3 h-3 bg-white rounded-full"></span>
            <p>{{ (status.status) ? 'Ativo' : 'Desativado' }}</p>
        </div>
        <v-icon name="fa-sign-out-alt" class="w-8 h-8 text-primary cursor-pointer" @click="signOut"></v-icon>
        <div v-if="showModal" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
            <div class="bg-white p-5 rounded-lg w-80 text-center" style="max-width: 90vw;">
                <p class="text-lg font-semibold mb-4">Ação não permitida</p>
                <p>Você não pode alterar o status enquanto o pedido está em andamento.</p>
                <button @click="closeModal" class="mt-4 bg-primary text-white px-4 py-2 rounded">Ok</button>
            </div>
        </div>

    </div>
</template>

<script setup>
import api from '@/services/api';
import { usePedidoStore } from '@/stores/usePedidoStore';
import { useStatus } from '@/stores/useStatus';
import { onMounted, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
const status = useStatus();
const router = useRouter();
const showModal = ref(false);

const pedidoStore = usePedidoStore();

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

async function setStatus() {
    if (currentStatus.value !== 'buscando') {
        showModal.value = true;
        return;
    }

    const { data } = await api.put('delivery/info/available/');
    if (data.data.available == "Disponível") {
        status.setStatus(true);
    } else {
        status.setStatus(false);
    }
}

function closeModal() {
    showModal.value = false;
}

onMounted(async () => {
    const { data } = await api.get('delivery/info/');
    if (data.data.available == "Disponível") {
        status.setStatus(true);
    } else {
        status.setStatus(false);
    }
});

async function signOut() {
    if (status.status == true) {
        const { data } = await api.put('delivery/info/available/');
    }
    localStorage.removeItem('token');
    router.push({ name: 'delivery-login' });
}

</script>
