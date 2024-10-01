<script setup>
import { ref, watch, defineProps } from 'vue';

const widthLine = ref(0);

const emit = defineEmits(['returnBack']);

const returnBack = () => {
    if (props.currentStep > 1) {
        widthLine.value = (props.currentStep - 2) * 25;
        emit('returnBack');
    }
};

const props = defineProps({
    currentStep: Number,
});

watch(() => props.currentStep, (newStep) => {
    if (newStep > 1 && newStep < 7) {
        widthLine.value = (newStep - 1) * 25;
    }
});
</script>

<template>
    <header class="headerSteps" :class="props.currentStep > 1 &&  props.currentStep < 5? 'justify-center md:justify-between': 'justify-center md:justify-center'">
        <div class="arrow" @click="returnBack" v-if="props.currentStep > 1 &&  props.currentStep < 5">
            <img src="../../assets/img/store/arrow.svg" class="img">
        </div>
        <img src="../../assets/img/logo_header.png" class="img">
        <div></div>
        <div class="progressBar" :style="{ width: widthLine + '%' }"></div>
    </header>
</template>

<style lang="scss" scoped>
.headerSteps {
    @apply w-full h-[100px] bg-white flex flex-row items-center rounded-b-3xl font-default text-lg border drop-shadow-md;

    .img {
        @apply w-[100px] h-[100px] ml-[-1.25rem];
    }

    @media (min-width: 768px) {
        @apply rounded-none drop-shadow-none;
    }

    @media (max-width: 768px) {
        .progressBar {
            display: none;
        }
    }

    .arrow {
        @apply w-[50px] h-[50px] hidden md:flex justify-center items-center pl-5 cursor-pointer;
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
