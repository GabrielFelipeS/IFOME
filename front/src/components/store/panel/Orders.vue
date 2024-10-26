<template>
    <div class="w-[450px] h-[100vh] border-r border-soft-border">
        <FilterOrders @filter="ordemProducts" />
        <div class="w-full h-[calc(100vh_-_80px)] overflow-y-auto overflow-x-hidden">
            <CardOrder />
        </div>
    </div>
</template>

<script setup>
import CardOrder from './CardOrder.vue';
import FilterOrders from './FilterOrders.vue';

function ordemProducts(value) {
    console.log(value);
}

</script>
