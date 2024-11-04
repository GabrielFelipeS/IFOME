<template>
    <div class="w-full bg-white rounded-t-3xl drop-shadow-2xl fixed bottom-0 left-0">
        <div class="w-full flex flex-row h-[100px] justify-center items-center gap-3">
            <span class="w-3 h-3  rounded-full animate-ping duration-300" :class="(status.status) ? 'bg-green-500' : 'bg-red-500'"></span>
            <p class="text-2xl font-thin">{{ (status.status) ? 'Buscando pedidos' : 'off-line' }}</p>
        </div>
    </div>
</template>

<script setup>
import { useStatus } from '@/stores/useStatus';
const status = useStatus();

</script>
