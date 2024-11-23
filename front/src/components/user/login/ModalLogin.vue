<script setup>
import Button from "@/components/Button.vue";
import Modal from "@/components/user/Modal.vue";
import router from "@/router/index.js";

</script>

<template>
	<Modal class="modal">
		<div class="title">
			Fazer login
		</div>
		<div class="subtitle">
			Como deseja continuar ?
		</div>
		<div class="main">
			<div class="btn-container">
				<Button href="#" class="button bg-[#4065B4] text-white hover:bg-[#335190] active:bg-[#20335a]">
					Continuar com Facebook <v-icon name="fa-facebook-square" class="icon"/></Button>
			</div>
			<div class="btn-container">
				<Button href="#" class="button bg-[#F2F2F2] hover:bg-[#c2c2c2] active:bg-[#797979]">
					Fazer login com o Google <v-icon name="fa-google" class="icon" /></Button>
			</div>
			<div class="btn-container">
				<Button href="#" class="button bg-primary text-white hover:bg-primary-dark active:bg-primary-darker"
						@click="$emit('load-component', 'form-login')">Continuar com e-mail</Button>
			</div>
			<div class="btn-container">
				<Button href="#" inversed class="button border border-primary hover:text-white hover:bg-primary
				 active:bg-primary-dark hover:border-white" @click="router.push('/user/register')">Ainda não tem uma conta? Cadastre-se agora</Button>
			</div>
		</div>
		<footer class="footer">
			<span>Ao continuar, você concorda em receber comunicações do iFome.
				Confira nossa <a href="#">Declaração de Privacidade</a>.</span>
		</footer>
	</Modal>
</template>

<style scoped>

@media (min-width: 768px) {
	.modal {
		@apply w-full justify-end gap-4 max-w-[800px];
		@apply md:ml-80;
		margin-left: 60%;
		padding: 1%;
	}
}


.title {
	@apply flex flex-row font-semibold pl-4 md:text-4xl text-2xl;
}

.subtitle {
	@apply flex flex-row pl-4 mt-4 md:text-2xl text-xl;
}

.main {
	@apply mt-5
}

.btn-container {
	@apply flex flex-row justify-center items-center align-baseline my-3 px-5;
}

.button {
	@apply rounded-md w-full flex flex-row justify-center items-center py-[2%];
}

.icon {
	@apply h-full w-5 ml-1.5;
}

.footer {
	@apply flex items-center justify-center align-baseline px-4;
	@apply font-light text-xs
}

.footer > span > a {
	@apply underline
}
</style>