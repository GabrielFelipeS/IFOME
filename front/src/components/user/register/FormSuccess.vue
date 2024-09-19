<script setup>

import Modal from "@/components/user/register/Modal.vue";
import Button from "@/components/Button.vue";

const goToPage = async (page) => {
	await router.push({ name: page }); // Substitua pelo nome da rota
};

</script>

<template>
	<Modal>
		<header class="title">
			Cadastro Finalizado !
		</header>
		<div class="main">
			<div class="btn-container">
				<Button href="#" @click="goToPage('home')"
						class="button bg-primary text-white font-semibold">Finalizar<v-icon name="fa-check" class="ml-5"/></Button>
			</div>
		</div>
	</Modal>
</template>

<style scoped>
.btn-container {
	@apply flex flex-row justify-center items-center align-baseline my-3 px-5;
}
.button {
	@apply rounded-md flex justify-center align-middle py-[2%] w-[50%];
}

.title {
	@apply flex flex-row font-semibold pl-4 text-xl justify-center;
}

.main {
	@apply mt-5
}
</style>