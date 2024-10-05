<script setup>
import Modal from "@/components/user/Modal.vue";
import Button from "@/components/Button.vue";


</script>

<template>
	<div class="max-w-[700px] flex flex-row w-full">
		<Modal class="gap-2">
			<header class="title">
				Pronto !
			</header>
			<div class="subtitle">
				Caso o e-mail informado esteja cadastrado, enviaremos um e-mail com
				instruções de como recuperar a sua conta
			</div>
			<div class="main">
				<div class="btn-container">
					<Button href="#" @click="$emit('go-back')" class="button border border-primary hover:bg-primary
					 		active:bg-primary-dark hover:text-white active:text-white"
							inversed> <v-icon name="fa-chevron-left"/> Tentar com outro e-mail</Button>
					<Button href="#" @click="$emit('load-component', 'form-login')"
							class="button bg-primary text-white hover:bg-primary-dark
							active:bg-primary-darker"> Voltar ao login </Button>
				</div>
			</div>
		</Modal>
	</div>
</template>

<style scoped>
.title {
	@apply flex flex-row font-semibold pl-4 text-3xl ;
}
.subtitle {
	@apply flex flex-row font-light pl-4 text-xl text-wrap ;
}
.main {
	@apply mt-5
}
.btn-container {
	@apply flex flex-row justify-between items-center align-baseline mt-16 w-full;
}
.button {
	@apply rounded-md w-full flex justify-center items-center py-[2%] gap-2 max-w-[250px] max-h-[50px];
}


</style>