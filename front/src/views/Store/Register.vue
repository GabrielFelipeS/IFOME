<script setup>
import '@/components/store/Header.vue';
import Header from '@/components/store/Header.vue';
</script>

<template>
    <div class="content">
        <Header />
    </div>
</template>

<style lang="scss" scoped>
.content {
    @apply w-full h-screen flex flex-col;
    background-image: url('../../assets/img/store/background_register_store_mob.jpg');
    background-position: top;
    background-size: cover;
    background-repeat: no-repeat;

    @media (min-width: 768px) {
        background-image: url('../../assets/img/store/background_register_store.webp');
    }
}
</style>