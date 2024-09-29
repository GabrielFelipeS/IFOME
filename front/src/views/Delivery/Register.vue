<script setup>
import FormHome from '@/components/Delivery/FormHome.vue';
import Header from '@/components/Delivery/Header.vue';
import StepsFormHome from '@/components/Delivery/StepsFormHome.vue';
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
                if (Array.isArray(errorMessage)) {
                    errorMessage.forEach(error => {
                        toast.error(error);
                    });
                } else {
                    toast.error(errorMessage);
                }
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
        <FormHome @submit="submitForm" @responseApi="showToast"/>
        <StepsFormHome :data="data" @responseApi="showToast" />
    </div>
</template>

<style lang="scss" scoped>
.content {
    @apply w-full h-screen flex flex-col justify-end items-center;
    background-image: url('../../assets/img/delivery/delivery_man_mob.jpg');
    background-position: top;
    background-size: cover;
    background-repeat: no-repeat;

    @media (min-width: 768px) {
        background-image: url('../../assets/img/delivery/delivery_man.jpg');
        @apply flex-row justify-end items-end;
    }
}
</style>
