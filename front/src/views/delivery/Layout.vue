<script setup>
import Header from "@/components/delivery/panel/Header.vue";
import Footer from "@/components/delivery/panel/Footer.vue";
import { useRoute } from "vue-router";
import { computed } from "vue";

const route = useRoute();

const isLogged = computed(() => {
	return !(route.name === 'delivery-login' || route.name === 'delivery-register');
});
</script>

<template>
	<Header v-if="isLogged" />
	<router-view />
	<Footer v-if="isLogged" />
</template>
