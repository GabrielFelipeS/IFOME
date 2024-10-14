<script setup>
import Button from '@/components/Button.vue';
</script>

<template>
    <header class="header">
        <img src="../../assets/img/logo_header.png" class="img">
        <div class="buttons">
            <router-link :to="{name: 'delivery-login'}" class="button1">Portal do Entregador</router-link>
        </div>
    </header>
</template>

<style lang="scss" scoped>
.header {

    @apply w-full h-[100px] fixed top-0 left-0 bg-white flex flex-row justify-center items-center rounded-b-3xl font-default text-lg;

    .buttons {
        @apply flex flex-row gap-4 w-[75%] items-center justify-around h-full;

        @media (min-width: 768px) {
            @apply justify-end;

            .button1 {
                @apply text-primary border border-primary p-2 rounded-md w-auto;
            }

            .button2 {
                @apply text-white bg-primary p-2 rounded-md w-[150px];
            }
        }

        .button2 {
            @apply h-full w-[50%] md:w-[150px] md:h-auto md:border-none flex items-center justify-center border-b-2 border-primary;
        }
    }

    .img {
        @apply w-[100px] h-[100px] hidden;
    }

    @media (min-width: 768px) {
        @apply rounded-none;

        .img {
            @apply block;
        }
    }
}
</style>