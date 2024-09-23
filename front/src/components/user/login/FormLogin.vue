<script setup>
import Modal from "@/components/user/Modal.vue";
import {onMounted, ref} from "vue";
import Button from "@/components/Button.vue";

const emit = defineEmits(["submit-login"]);
const errors = ref({
	email: '',
	password: '',
});

const email = ref('');
const password = ref('');
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

	if (password.value.length === 0) {
		errors.value.password = 'O campo de senha não pode estar vazio';
		valid = false;
	} else {
		errors.value.password = '';
	}

	return valid;
}

const submitLogin = () => {
	if (validateInputs()) {
		errors.value.email = '';
		errors.value.password = '';
		emit('submit-login', [email.value, password.value]);
	}
}
</script>

<template>
	<Modal class="gap-2">
		<header class="title">
			Login
		</header>
		<div class="main" @submit="submitLogin">
			<div class="form-group">
				<label for="email" class="form-label">E-mail</label>
				<input type="email" placeholder="nome@email.com" class="form-input"
					   v-model="email" required id="email">
				<p class="invalid-input-text" v-if=" errors.email !== '' ">{{ errors.email }}</p>
				<label for="password" class="form-label">Senha</label>
				<input type="password" placeholder="********" class="form-input"
					   v-model="password" required id="password">
				<p class="invalid-input-text" v-if=" errors.password !== '' ">{{ errors.password }}</p>
				<a class="hiper-link" href="#" @click="$emit('load-component', 'password-reset')">
					Esqueceu a Senha ?</a>
			</div>
			<div class="btn-container">
				<Button href="#" @click="submitLogin" class="button bg-primary text-white hover:bg-primary-dark
					 active:bg-primary-darker">Fazer login <v-icon name="fa-chevron-right"/></Button>
			</div>
		</div>
	</Modal>
</template>

<style scoped>

.title {
	@apply flex flex-row font-semibold pl-4 text-3xl ;
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
.form-label {
	@apply my-1 text-sm;
}

.invalid-input-text {
	@apply font-light text-sm text-red-500 px-2;
}
.hiper-link {
	@apply underline mt-3 text-primary;
}

</style>