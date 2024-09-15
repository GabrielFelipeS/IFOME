<script setup>
import FormHome from '@/components/store/FormHome.vue';
import '@/components/store/Header.vue';
import Header from '@/components/store/Header.vue';
import '@/components/store/FormHome.vue';
import StepsFormHome from '@/components/store/StepsFormHome.vue';


const submitForm = (data) => {
    console.log(data);
}
</script>

<template>
    <div class="content">
        <Header />
        <FormHome @submit="submitForm"/>
        <StepsFormHome name="Teste"/>
    </div>
</template>

<style lang="scss" scoped>
.content {
    @apply w-full h-screen flex flex-col justify-end items-center;
    background-image: url('../../assets/img/store/background_register_store_mob.jpg');
    background-position: top;
    background-size: cover;
    background-repeat: no-repeat;

    @media (min-width: 768px) {
        background-image: url('../../assets/img/store/background_register_store.webp');
        @apply flex-row justify-end items-end;
    }
}
</style>