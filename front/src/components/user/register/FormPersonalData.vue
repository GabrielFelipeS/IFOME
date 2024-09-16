<script setup>
import Modal from "@/components/user/register/Modal.vue";
import Button from "@/components/Button.vue";
import { MaskInput } from "vue-3-mask";
import {ref, watch} from "vue";

const props = defineProps({
	formData: {
		type: Object,
		required: true,
	}
})

const emit = defineEmits(['next-step']);

const errors = ref({
	name: '',
	phone: '',
	dateOfBirth: '',
	cpf: '',
	password: '',
	passwordConfirmation: '',
})

const validateForm = () => {
	let valid = true;

	if (props.formData.name.length === 0) {
		errors.value.name = 'Preencha o campo de nome.';
		valid = false;
	} else {
		errors.value.name = '';
	}

	if (props.formData.phone.length < 15) {
		errors.value.phone = 'Preencha o campo de telefone.';
		valid = false;
	} else {
		errors.value.phone = '';
	}

	if (props.formData.dateOfBirth.length < 10) {
		errors.value.dateOfBirth = 'Preencha o campo de data de nascimento.';
		valid = false;
	} else {
		errors.value.dateOfBirth = '';
	}

	if (props.formData.cpf.length < 14) {
		errors.value.cpf = 'Preencha o campo de CPF.';
		valid = false;
	} else {
		errors.value.cpf = '';
	}

	if (props.formData.password.length < 8) {
		errors.value.password = 'A senha deve conter pelo menos 8 caracteres.';
		valid = false;
	} else if (!/[A-Z]/.test(props.formData.password)) {
		errors.value.password = 'A senha deve conter pelo menos uma letra maiúscula.';
		valid = false;
	} else if (!/[a-z]/.test(props.formData.password)) {
		errors.value.password = 'A senha deve conter pelo menos uma letra minúscula.';
		valid = false;
	} else if (!/[@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(props.formData.password)) {
		errors.value.password = 'A senha deve conter pelo menos um caractere especial.';
		valid = false;
	} else {
		errors.value.password = '';
	}

	if (props.formData.passwordConfirmation.length < 8) {
		errors.value.passwordConfirmation = 'A senha deve conter pelo menos 8 caracteres.';
		valid = false;
	} else if (props.formData.passwordConfirmation !== props.formData.password) {
		errors.value.passwordConfirmation = 'As senhas não correspondem.';
		valid = false;
	} else {
		errors.value.passwordConfirmation = '';
	}

	return valid;
};

const nextStep = () => {
	if (validateForm()) {
		emit('next-step')
	}
};

watch(props.formData, () => {
	if (props.formData.name.length >= 1) {
		errors.value.name = '';
	}
	if (props.formData.phone.length >= 1) {
		errors.value.phone = '';
	}
	if (props.formData.dateOfBirth.length >= 1) {
		errors.value.dateOfBirth = '';
	}
	if (props.formData.cpf.length >= 1) {
		errors.value.cpf = '';
	}
	if (props.formData.password.length >= 1) {
		errors.value.password = '';
	}
	if (props.formData.passwordConfirmation.length >= 1) {
		errors.value.passwordConfirmation = '';
	}
}, { deep: true });
</script>

<template>
	<Modal>
		<header class="title">
			Complete os dados do seu cadastro
		</header>
		<div class="flex flex-row pl-4 mt-4">
			Preencha os dados a seguir para prosseguirmos com seu cadastro
		</div>
		<div class="main">
			<div class="form-group">
				<label for="" class="form-label">Nome completo</label>
				<input type="text" placeholder="Nome completo" class="form-input" id="name"
					   v-model="formData.name" >
				<p class="invalid-input-text" v-if="errors.name !== ''">{{ errors.name }}</p>
				<label for="" class="form-label">Celular</label>
				<MaskInput type="text" placeholder="(00) 90000-0000" class="form-input" id="phone"
					   v-model="formData.phone" mask="(##) #####-####" />
				<p class="invalid-input-text" v-if="errors.phone !== ''">{{ errors.phone }}</p>
				<label for="" class="form-label">Data de nascimento</label>
				<MaskInput type="text" placeholder="31/12/1969" class="form-input"
					   v-model="formData.dateOfBirth" mask="##/##/####" />
				<p class="invalid-input-text" v-if="errors.dateOfBirth !== ''">{{ errors.dateOfBirth }}</p>
				<label for="" class="form-label">CPF</label>
				<MaskInput type="text" placeholder="000.000.000-00" class="form-input"
					   v-model="formData.cpf" mask="###.###.###-##" />
				<p class="invalid-input-text" v-if="errors.cpf !== ''">{{ errors.cpf }}</p>
				<label for="" class="form-label">Senha</label>
				<input type="password" placeholder="******" class="form-input"
					   v-model="formData.password" >
				<p class="invalid-input-text" v-if="errors.password !== ''">{{ errors.password }}</p>
				<label for="" class="form-label">Confirmar Senha</label>
				<input type="password" placeholder="******" class="form-input"
					   v-model="formData.passwordConfirmation" >
				<p class="invalid-input-text" v-if="errors.passwordConfirmation !== ''">{{ errors.passwordConfirmation }}</p>
			</div>
			<div class="btn-container">
				<Button href="#" @click="$emit('previous-step')" inversed
						class="button text-primary-subtle border border-primary mr-[15%]">Voltar</Button>
				<Button href="#" @click="nextStep"
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
	@apply flex flex-row justify-between mt-5  px-[10%];
}
.button {
	@apply rounded-md w-full flex justify-center align-middle py-[2%];
}
.form-group {
	@apply flex flex-col justify-center items-start align-baseline mx-4 mb-2;
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
</style>