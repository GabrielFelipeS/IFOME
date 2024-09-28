<script setup>
import Modal from "@/components/user/Modal.vue";
import {ref} from "vue";
import Button from "@/components/Button.vue";

const emit = defineEmits(["submit-password-reset"]);
const errors = ref({
	email: '',
});

const email = ref('');
const validateInputs = () => {
	let valid = true;
	if (email.value.length === 0) {
		errors.value.email = 'O campo de e-mail não pode estar vazio';
		valid = false;
	} else if (!email.value.includes('@') || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
		errors.value.email = 'Digite um e-mail válido';
		valid = false;
	} else {
		errors.value.email = '';
	}

	return valid;
}

const submitPasswordReset = () => {
	if (validateInputs()) {
		errors.value.email = '';
		emit('submit-password-reset', {
			email: email.value,
		});
	}
}
</script>

<template>
	<div class="max-w-[600px] flex flex-row w-full">
		<Modal class="gap-2">
			<header class="title">
				Recuperação de Senha
			</header>
			<div class="subtitle">
				Informe seu endereço de e-mail para podermos recuperar a sua senha
			</div>
			<div class="main">
				<div class="form-group">
					<input type="email" placeholder="nome@email.com" class="form-input"
						   v-model="email" required id="email">
					<p class="invalid-input-text" v-if=" errors.email !== '' ">{{ errors.email }}</p>
				</div>
				<div class="btn-container">
					<Button href="#" @click="submitPasswordReset" class="button bg-primary text-white hover:bg-primary-dark
					 active:bg-primary-darker">Recuperar senha <v-icon name="fa-chevron-right"/></Button>
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
	@apply flex flex-row font-light pl-4 text-xl ;
}
.main {
	@apply mt-5
}
.btn-container {
	@apply flex flex-row justify-center items-center align-baseline mt-16 px-[10%];
}
.button {
	@apply rounded-md w-full flex justify-center items-center py-[2%] gap-2 max-w-[250px] max-h-[50px];
}
.form-group {
	@apply flex flex-col justify-start items-start mx-3;
}
.form-input {
	@apply rounded-md border border-tertiary-subtle p-2 w-full
}

.invalid-input-text {
	@apply font-light text-sm text-red-500 px-2;
}

</style>