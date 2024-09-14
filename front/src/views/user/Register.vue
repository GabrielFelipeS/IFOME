<script setup>
import '@/components/user/Header.vue'
import Header from "@/components/user/Header.vue";
</script>

<template>
	<div class="content">
		<Header />
	</div>
</template>

<style scoped>
	.content {
		@apply w-full h-screen;
		background-image: url("../../assets/img/user/bg-cadastro.jpg");
		background-color: #fff;
		background-position: center;
		background-size: cover;
	}
</style>