<script setup>
import { ref } from 'vue';

const widthLine = ref(0);

const increaseProgress = () => {
    if (widthLine.value < 100) {
        widthLine.value += 25;
    }
};
</script>

<template>
    <header class="headerSteps">
        <img src="../../assets/img/logo_header.png" class="img">
        <div class="progressBar" :style="{ width: widthLine + '%' }"></div>
    </header>
</template>

<style lang="scss" scoped>
.headerSteps {
    @apply w-full h-[100px] top-0 left-0 bg-tertiary flex flex-row justify-center items-center rounded-b-3xl font-default text-lg border drop-shadow-md relative;

    .img {
        @apply w-[100px] h-[100px];
    }

    @media (min-width: 768px) {
        @apply rounded-none drop-shadow-none;
    }

    @media (max-width: 768px) {
        .progressBar {
            display: none;
        }
    }

    .progressBar {
        position: absolute;
        bottom: 0;
        left: 0;
        height: 5px;
        @apply bg-primary;
        transition: width 0.3s ease-in-out;
    }
}
</style>
