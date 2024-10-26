<template>
    <div class="flex flex-row">
        <SidebarMenu />
        <main class="h-[100vh] w-[calc(100vw_-_150px)]">
            <Orders />
        </main>
    </div>
</template>


<script setup>
import Orders from '@/components/store/panel/Orders.vue';
import SidebarMenu from '@/components/store/panel/sidebarMenu.vue';
</script>