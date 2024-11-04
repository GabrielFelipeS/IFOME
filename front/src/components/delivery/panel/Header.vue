<template>
    <div class="w-full h-[100px] bg-white flex flex-row items-center justify-around">
        <v-icon name="fa-user" class="bg-primary text-white p-3 rounded-full" scale="2.5"></v-icon>
        <div class="px-7 py-3 text-white rounded-lg cursor-pointer flex flex-row items-center gap-1"
            :class="status.status == true ? 'bg-green-500' : 'bg-gray-500'" @click="setStatus">
            <span class="w-3 h-3 bg-white rounded-full"></span>
            <p>{{ (status.status) ? 'Ativo' : 'Desativado' }}</p>
        </div>
        <v-icon name="fa-sign-out-alt" class="w-8 h-8 text-primary cursor-pointer" @click="signOut"></v-icon>
    </div>
</template>

<script setup>
import api from '@/services/api';
import { useStatus } from '@/stores/useStatus';
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
const status = useStatus();
const router = useRouter();

async function setStatus() {
    const { data } = await api.put('delivery/info/available/');
    if (data.data.available == "Disponível") {
        status.setStatus(true);
    } else {
        status.setStatus(false);
    }
}

onMounted(async () => {
    const { data } = await api.get('delivery/info/');
    if (data.data.available == "Disponível") {
        status.setStatus(true);
    } else {
        status.setStatus(false);
    }
})

async function signOut() {
    if (status.status == true) {
        const { data } = await api.put('delivery/info/available/');
    }
    localStorage.removeItem('token');
    router.push({ name: 'delivery-login' });
}

</script>