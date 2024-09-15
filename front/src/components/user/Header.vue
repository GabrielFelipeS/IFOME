<script setup>
import Button from '@/components/Button.vue';
import {useRouter} from "vue-router";

const router = useRouter();

const goToPage = async (page) => {
	await router.push({ name: page }); // Substitua pelo nome da rota
	window.location.reload(); // Força o recarregamento da página
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
			<div class="flex flex-col h-full w-1/3 justify-center hidden">
				<Button href="#" >Voltar</Button>
			</div>
			<div class="flex flex-col h-full justify-center items-center">
				<img src="../../assets/img/logo_header.png" class="w-[25%]" alt="Logo">
			</div>
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
.header{
	@apply w-full h-[70px] bg-white flex flex-row  justify-center items-center rounded-b-3xl font-default text-lg;
}
</style>