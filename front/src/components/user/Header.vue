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
	},
	backButton: {
		type: Boolean,
		default: false,
	},
	homePage: {
		type: String,
		default: 'home',
	},
});

const emit = defineEmits(['go-back']);
const goBack = () => {
	emit('go-back');
}

</script>

<template>
	<header class="header">
		<div class="flex flex-row gap-4 w-[75%] items-center justify-center h-full" v-if="logo">
			<img src="../../assets/img/logo_header.png" class="logo" alt="Logo">
		</div>
		<div class="flex flex-row gap-4 w-[75%] items-center justify-between h-full" v-if="!logo">
			<div>
				<Button @click="goToPage(props.homePage)"
						class="h-full flex items-center justify-center border-b-2 border-white
						active:border-primary active:text-primary"
						href="#">Login</Button>
			</div>
			<div>
				<Button @click="goToPage(props.homePage)"
						class="h-full flex items-center justify-center border-b-2 border-white
						 active:border-primary active:text-primary"
						href="#">Cadastrar</Button>
			</div>
		</div>
	</header>
	<header class="header-desktop">
		<div class="back-div"> <a v-if="props.backButton === true" href="#" @click="goBack"><v-icon name="fa-arrow-left" class="text-primary" /></a> </div>
		<div class="desktop-content">
			<Button href="#" @click="goToPage(props.homePage)">
				<img src="../../assets/img/logo_header.png" class="logo" alt="Logo"></Button>
		</div>
		<div></div>
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
		@apply w-full h-[70px] bg-white md:flex flex-row justify-between items-center font-default border-b-4 text-lg;
	}
	.desktop-content {
		@apply w-full flex flex-row justify-center;
		@apply max-w-[1278px];
	}

	.button {
		@apply flex items-center justify-center border-2 border-primary px-2 py-1 rounded-md
	}

	.back-div {
		@apply flex flex-row justify-center items-center pl-8;
	}

.logo {
	@apply w-[70px] h-[70px]
}
</style>