<script setup>
import Modal from "@/components/user/Modal.vue";
import Button from "@/components/Button.vue";
import {onMounted, ref} from "vue";

const props = defineProps({
	formData: {
		type: Object,
		required: true,
	}
})

const emit = defineEmits(["next-step"]);

const error = ref('');


const nextStep = () => {
	if (props.formData.email.length === 0) {
		error.value = 'O campo de e-mail não pode estar vazio';
	} else if (!props.formData.email.includes('@') || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(props.formData.email)) {
		error.value = 'Digite um e-mail válido';
	} else {
		error.value = '';
		emit('next-step');
	}
}
onMounted(() => {
	const emailInput = document.querySelector('#email');
	document.querySelector('input').addEventListener('click', () => {
		emailInput.classList.remove('btn-error');
	});
});

</script>

<template>
	<Modal>
		<header class="title">
			Informe seu e-mail para continuarmos
		</header>
		<div class="main">
			<div class="form-group">
				<input type="email" placeholder="Digite o seu e-mail" class="form-input"
					v-model="formData.email" required id="email"
				>
				<p class="invalid-input-text" v-if="error">{{ error }}</p>
			</div>
			<div class="btn-container">
				<Button href="#" @click="nextStep"
						class="button bg-primary text-white hover:bg-primary-dark active:bg-primary-darker">Continuar</Button>
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
	@apply flex flex-row justify-center items-center align-baseline mt-5 px-[10%];
}
.button {
	@apply rounded-md w-full flex justify-center align-middle py-[2%];
}
.form-group {
	@apply flex flex-col justify-start items-start mx-3;
}
.form-input {
	@apply rounded-md border border-tertiary-subtle p-2 w-full
}

.invalid-input-text {
	@apply font-normal text-sm text-primary-dark px-2;
}

</style>