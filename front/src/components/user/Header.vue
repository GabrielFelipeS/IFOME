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
				<Button href="#">Login</Button>
			</div>
			<div>
				<Button @click="goToPage('user-register')"
						class="h-full w-[50%] flex items-center justify-center border-b-2 border-primary"
						inversed href="#">Cadastrar</Button>
			</div>
		</div>
	</header>
	<header class="header-desktop">
		<div class="flex flex-row gap-4 w-[75%] items-center justify-center h-full" v-if="logo">
			<img src="../../assets/img/logo_header.png" class="logo" alt="Logo">
		</div>
		<div class="flex flex-row gap-4 w-[75%] items-center justify-between h-full" v-if="!logo">
			<div>
				<Button href="#">Login</Button>
			</div>
			<div>
				<Button @click="goToPage('user-register')"
						class="h-full w-[50%] flex items-center justify-center border-b-2 border-primary"
						inversed href="#">Cadastrar</Button>
			</div>
		</div>
	</header>
</template>

<style lang="scss" scoped>
	* {
		@apply max-w-full
	}

	.header {
		@apply w-full h-[70px] bg-white flex flex-row  justify-center items-center rounded-b-3xl font-default text-lg border-b-4;
	}
	.header-desktop {
		@apply hidden;
	}

	.header {
		@apply md:hidden;
	}

	.header-desktop {
		@apply w-full h-[70px] bg-white flex flex-row justify-center items-center font-default border-b-4 text-lg;
	}


.logo {
	@apply w-[70px] h-[70px]
}
</style>