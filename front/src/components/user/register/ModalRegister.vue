<script setup>
import Button from "@/components/Button.vue";
</script>

<template>
	<div class="body">
		<div class="title">
			Cadastre-se agora
		</div>
		<div class="subtitle">
			Como deseja continuar ?
		</div>
		<div class="main">
			<div class="btn-container">
				<Button href="#" class="button bg-[#4065B4] text-white">Continuar com Facebook</Button>
			</div>
			<div class="btn-container">
				<Button href="#" class="button bg-[#F2F2F2]">Fazer login com o Google</Button>
			</div>
			<div class="btn-container">
				<Button href="#" class="button bg-primary text-white">Continuar com e-mail</Button>
			</div>
			<div class="btn-container">
				<Button href="#" inversed class="button border border-primary">Já tenho uma conta, fazer login</Button>
			</div>
		</div>
	</div>
</template>

<style scoped>
	.title {
		@apply flex flex-row font-semibold pl-4 text-xl;
	}

	.subtitle {
		@apply flex flex-row pl-4 mt-4;
	}

	.body {
		@apply bg-white flex flex-col h-full my-[5%] mx-5 justify-center;
		@apply rounded-2xl border-2 border-b-stone-400;
	}

	.main {
		@apply mt-5
	}

	.btn-container {
		@apply flex flex-row justify-center items-center align-baseline my-3 px-5;
	}

	.button {
		@apply rounded-md w-full flex justify-center align-middle py-[2%];
	}
</style>