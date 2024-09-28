<script setup>
import FormHome from '@/components/store/FormHome.vue';
import Header from '@/components/store/Header.vue';
import StepsFormHome from '@/components/store/StepsFormHome.vue';
import { ref } from 'vue';

const data = ref({});

const submitForm = (formData) => {
    data.value = formData;
};

</script>

<template>
    <div class="content">
        <Header />
        <FormHome @submit="submitForm"/>
        <StepsFormHome :data="data"/>
    </div>
</template>

<style lang="scss" scoped>
.content {
    @apply w-full h-screen flex flex-col justify-center items-center px-2.5;
    background-image: url('../../assets/img/store/background_register_store_mob.jpg');
    background-position: top;
    background-size: cover;
    background-repeat: no-repeat;

    @media (min-width: 768px) {
        background-image: url('../../assets/img/store/background_register_store.webp');
        @apply flex-row justify-end items-center;
    }
}
</style>
