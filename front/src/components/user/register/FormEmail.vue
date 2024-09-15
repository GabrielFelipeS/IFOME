<script setup>
import Modal from "@/components/user/register/Modal.vue";
import Button from "@/components/Button.vue";

defineProps({
	formData: {
		type: Object,
		required: true,
	}
})
</script>

<template>
	<Modal>
		<header class="title">
			Informe seu e-mail para continuarmos
		</header>
		<div class="main">
			<div class="form-group">
				<input type="email" placeholder="Digite o seu e-mail" class="form-input"
					v-model="formData.email"
				>
			</div>
			<div class="btn-container">
				<Button href="#" @click="$emit('next-step')"
						class="button bg-primary text-white">Continuar</Button>
			</div>
		</div>
	</Modal>
</template>

<style scoped>
.title {
	@apply flex flex-row font-semibold pl-4 ;
}
.main {
	@apply mt-5
}
.btn-container {
	@apply flex flex-row justify-center items-center align-baseline my-3 px-[20%];
}
.button {
	@apply rounded-md w-full flex justify-center align-middle py-[2%];
}
.form-group {
	@apply flex flex-row justify-center items-center align-baseline mx-3;
}
.form-input {
	@apply rounded-md border border-tertiary-subtle p-2 w-full
}
</style>