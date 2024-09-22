<script setup>
import Button from '@/components/Button.vue';
import {useRouter} from "vue-router";

const router = useRouter();

const goToPage = async (page) => {
	await router.push({ name: page }); // Substitua pelo nome da rota
};

const props = defineProps({
	logo: {
		type: Boolean,
		default: false,
	}
})

</script>

<template>
	<header class="header">
		<div class="flex flex-row gap-4 w-[75%] items-center justify-center h-full" v-if="logo">
			<img src="../../assets/img/logo_header.png" class="logo" alt="Logo">
		</div>
		<div class="flex flex-row gap-4 w-[75%] items-center justify-between h-full" v-if="!logo">
			<div>
				<Button @click="goToPage('user-register')"
						class="h-full flex items-center justify-center border-b-2 border-white
						active:border-primary active:text-primary"
						href="#">Login</Button>
			</div>
			<div>
				<Button @click="goToPage('user-register')"
						class="h-full flex items-center justify-center border-b-2 border-white
						 active:border-primary active:text-primary"
						href="#">Cadastrar</Button>
			</div>
		</div>
	</header>
	<header class="header-desktop">
		<div class="desktop-content">
			<Button href="#" @click="goToPage('home')">
				<img src="../../assets/img/logo_header.png" class="logo" alt="Logo"></Button>
		</div>
	</header>
</template>

<style lang="scss" scoped>
	* {
		@apply max-w-full
	}

	.header {
		@apply w-full h-[70px] bg-white flex flex-row justify-center items-center rounded-b-3xl font-default text-lg border-b-4;
		@apply md:hidden;
	}

	.header-desktop {
		@apply hidden;
		@apply w-full h-[70px] bg-white md:flex flex-row justify-center items-center font-default border-b-4 text-lg;
	}
	.desktop-content {
		@apply w-full flex flex-row justify-center;
		@apply max-w-[1278px];
	}

	.button {
		@apply flex items-center justify-center border-2 border-primary px-2 py-1 rounded-md
	}


.logo {
	@apply w-[70px] h-[70px]
}
</style>