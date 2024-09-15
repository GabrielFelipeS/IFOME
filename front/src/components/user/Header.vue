<script setup>
import Button from '@/components/Button.vue';



</script>

<template>
	<header class="header">
		<img src="../../assets/img/logo_header.png" class="w-[25%] hidden" alt="Logo">
		<div class="flex flex-row gap-4 w-[75%] items-center justify-around h-full">
			<Button href="#">Entrar No Portal</Button>
			<Button href="#" class="h-full w-[50%] flex items-center justify-center border-b-2 border-primary"
					inversed>Cadastrar</Button>
		</div>
	</header>
</template>

<style lang="scss" scoped>
.header{
	@apply w-full h-[70px] bg-white flex flex-row  justify-center items-center rounded-b-3xl font-default text-lg;
}
</style>