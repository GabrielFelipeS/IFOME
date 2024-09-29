<script setup>
import FormHome from '@/components/store/FormHome.vue';
import Header from '@/components/store/Header.vue';
import StepsFormHome from '@/components/store/StepsFormHome.vue';
import { ref } from 'vue';
import { useToast } from 'vue-toast-notification';

const data = ref({});

const toast = useToast();

const submitForm = (formData) => {
    data.value = formData;
};

const showToast = (response) => {
    if (response.errors) {
        Object.keys(response.errors).forEach(key => {
            response.errors[key].forEach(errorMessage => {
                toast.error(`${errorMessage}`).duration = 5000;
            });
        });
    } else {
        toast.success(response.message);
    }
};

</script>

<template>
    <div class="content">
        <Header />
        <FormHome @submit="submitForm" />
        <StepsFormHome :data="data" @responseApi="showToast" />
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
