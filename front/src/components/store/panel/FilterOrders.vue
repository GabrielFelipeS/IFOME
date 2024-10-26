<template>
    <div class="w-full flex flex-row justify-center">
        <select name="" id=""
            class="w-[95%] p-3 outline-none bg-white border border-soft-border rounded-md my-3 px-4 appearance-none pr-8"
            style="background-image: url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgdmlld0JveD0iMCAwIDE2IDE2Ij4KICA8cGF0aCBkPSJNNC4wNjcgNi42NjdsNC4yOTQgNC4yOTMgNC4yOTQtNC4yOTMuNzA4LjcwOC00Ljk5NyA0Ljk5Ny00Ljk5Ny00Ljk5Ny43MDgtLjcwOHoiLz4KPC9zdmc+'); /* Ícone da seta */
                background-repeat: no-repeat;
                background-position: right 1rem center;
                background-size: 1em;
            " v-model="filterSelected">
            <option value="1">Todos</option>
            <option value="2">Order 1</option>
            <option value="3">Order 2</option>
            <option value="4">Order 3</option>
        </select>
    </div>
</template>

<script setup>
import { onUpdated, ref } from 'vue';

const emits = defineEmits(['filter']);
const filterSelected = ref(1);

onUpdated(() => {
    emits('filter', filterSelected.value);  
});

</script>