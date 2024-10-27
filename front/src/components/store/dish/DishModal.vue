<script setup>

</script>

<template>
	<div class="main">
		<div class="img-container">
			<button class="close-btn"><v-icon name="fa-times" scale="1.5" /></button>
			<img src="https://placehold.co/100" alt="Prato" class="image">
		</div>
		<div class="px-2 md:flex md:flex-col md:justify-between md:py-3 md:min-w-[120%] md:-translate-x-[17%]">
			<div class="dish-description">
				<span class="uppercase font-semibold">Tacos</span>
				<span class="text-xs text-tertiary-light line-clamp-3 md:line-clamp-none">Criação de sistema de cadastro de pratos para restaurantes já cadastrados.Criação de sistema de cadastro de pratos para restaurantes já cadastrados.Criação de sistema de cadastro de pratos para restaurantes já cadastrados.Criação de sistema de cadastro de pratos para restaurantes já cadastrados.Criação de sistema de cadastro de pratos para restaurantes já cadastrados.</span>
				<span class="uppercase text-green-500 font-semibold">R$ 30,00</span>
			</div>
			<div class="restaurant-description">
				<span class="font-semibold md:mb-2">Antojitos <v-icon name="fa-store"/></span>
				<hr>
				<span class="self-end md:w-full md:self-start md:mt-2">80 a 90 Minutos</span>
			</div>
			<div class="observations">
				<label>Observações:</label>
				<textarea class="border border-soft-border rounded-md h-24 resize-none text-xs px-1"></textarea>
			</div>
		</div>
		<div class="hidden md:flex"></div>
		<div class="button-container">
			<div class="btn-group">
				<button><v-icon name="fa-plus" /></button>
				<span class="mx-3 text-black">1</span>
				<button><v-icon name="fa-minus" /></button>
			</div>
			<button class="btn">Adicionar <v-icon name="fa-chevron-right"/></button>
		</div>
	</div>
</template>

<style scoped>
	.main {
		@apply flex flex-col justify-start mt-4 p-3 z-30;
		@apply min-h-screen w-full mt-10 border shadow rounded-xl bg-white;
		@apply fixed inset-0 mb-[91px] overflow-y-scroll;
		@apply scroll-m-0;

		@apply md:top-1/2 md:left-1/2 md:-translate-x-1/2 md:-translate-y-1/2;
		@apply md:max-w-[1000px] md:min-h-0 md:max-h-[650px] md:h-fit;
		@apply md:grid md:grid-cols-2 md:overflow-hidden md:shadow-2xl;

		@apply no-scrollbar;
	}
	.img-container {
		@apply flex flex-col justify-start items-center align-baseline p-3 w-full;

		@apply md:flex-col md:justify-start md:w-[78%] md:h-fit;
	}
	.image {
		@apply max-h-full max-w-full min-h-0 min-w-full rounded-md h-fit mt-10;

		@apply md:mt-8;
	}
	.dish-description {
		@apply flex flex-col justify-between items-start align-baseline w-full gap-2;
	}
	.restaurant-description {
		@apply flex flex-row justify-between mt-4 p-4 rounded-md border border-soft-border;
		@apply text-tertiary-light text-xs;

		@apply md:flex-col md:p-3;
	}
	.observations {
		@apply flex flex-col mt-5 gap-2;
		@apply text-sm text-tertiary-light font-semibold;
	}
	.button-container {
		@apply flex flex-row justify-between items-end align-baseline w-full gap-2 px-[10%] h-full self-end mb-[112px];
		@apply mt-6;

		@apply md:m-0 md:items-center md:h-fit md:w-full md:justify-end md:gap-8;
	}
	.btn-group {
		@apply inline-flex border border-soft-border p-2 rounded-md text-primary;

		& button {
			@apply w-full h-full;
			@apply text-primary active:text-primary-dark;
			@apply transition duration-300 ease-in-out;
		}
	}
	.btn {
		@apply bg-primary p-2 px-4 rounded-lg text-white border border-primary;
		@apply active:bg-primary-dark active:border-primary-dark;
		@apply transition duration-300 ease-in-out;

		@apply md:px-10;
	}
	.close-btn {
		@apply self-end text-tertiary-light fixed bg-white rounded-2xl border border-tertiary-light;
		@apply -translate-y-3 translate-x-3;

		@apply md:self-start md:flex md:bg-transparent md:border-none md:rounded-none md:translate-x-0 md:translate-y-0;
	}
</style>