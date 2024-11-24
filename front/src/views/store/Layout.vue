<script setup>
import Header from '@/components/store/Header.vue';
import {useRoute} from "vue-router";
import {computed} from "vue";

const route = useRoute();

const isLogged = computed(() => {
	return (route.name === 'store-login' || route.name === 'store-register');
});
</script>

<template>
	<Header v-if="isLogged"/>
	<router-view />
</template>