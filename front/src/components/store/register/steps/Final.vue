<template>
    <div class="step">
        <div class="final">
            <img src="../../../../assets/img/store/shop.png" alt="Logo Ifome">
            <h2>Seja Bem Vindo, {{ name }}</h2>
            <img src="../../../../assets/img/store/sushi.png" alt="Icone Sushi" class="baloon">
            <img src="../../../../assets/img/store/hamburguer-de-queijo.png" alt="Icone Hamburguer" class="baloon1">
            <img src="../../../../assets/img/store/plait.png" alt="Icone Prato" class="baloon2">
            <router-link :to="{ name: 'store-login' }" class="btn-text">Começar a Vender</router-link>
        </div>
    </div>
</template>

<script setup>

</script>