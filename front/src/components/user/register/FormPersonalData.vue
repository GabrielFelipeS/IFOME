<script setup>
import Modal from "@/components/user/register/Modal.vue";
import Button from "@/components/Button.vue";
import { MaskInput } from "vue-3-mask";

const props = defineProps({
	formData: {
		type: Object,
		required: true,
	}
})

const emit = defineEmits(['next-step']);

const nextStep = () => {
	if (props.formData.name.length > 0 &&
		props.formData.phone.length > 0 &&
		props.formData.dateOfBirth.length > 0 &&
		props.formData.cpf.length > 0 &&
		props.formData.password.length > 0 &&
		props.formData.passwordConfirmation.length > 0
	) {
		emit('next-step')
	}
};
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
				<label for="" class="form-label">Celular</label>
				<MaskInput type="text" placeholder="(00) 90000-0000" class="form-input" id="phone"
					   v-model="formData.phone"  mask="(##) #####-####" />
				<label for="" class="form-label">Data de nascimento</label>
				<MaskInput type="text" placeholder="31/12/1969" class="form-input"
					   v-model="formData.dateOfBirth" mask="##/##/####" />
				<label for="" class="form-label">CPF</label>
				<MaskInput type="text" placeholder="000.000.000-00" class="form-input"
					   v-model="formData.cpf" mask="###.###.###-##" />
				<label for="" class="form-label">Senha</label>
				<input type="password" placeholder="******" class="form-input"
					   v-model="formData.password" >
				<label for="" class="form-label">Confirmar Senha</label>
				<input type="password" placeholder="******" class="form-input"
					   v-model="formData.passwordConfirmation" >
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
	@apply flex flex-col justify-center items-start align-baseline mx-4;
}
.form-input {
	@apply rounded-md border border-tertiary-subtle p-2 w-full mb-2
}
.form-label {
	@apply my-1 text-sm;
}
</style>