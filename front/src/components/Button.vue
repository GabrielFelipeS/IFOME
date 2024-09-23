<script setup>

import { defineProps } from 'vue'

const props = defineProps({
    href: {
        type: String,
        required: true
    },
    inversed: {
        type: Boolean,
        default: false
    }
})

</script>

<template>
    <a :href="href" class="text-nowrap" :class="inversed ? 'text-primary' : 'text-black'">
        <slot></slot>
    </a>
</template>