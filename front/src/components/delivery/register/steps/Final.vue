<template>
    <div class="step">
        <div class="final">
            <img src="../../../../assets/img/delivery/delivery_man.png" alt="Logo Ifome">
            <h2>Seja Bem-Vindo, {{ props.currentData.name }}</h2>
            <router-link :to="{name: 'delivery-login'}" class="btn-text">Começar a Entregar</router-link>
        </div>
    </div>
</template>

<script setup>
const props = defineProps(['currentData']);
</script>