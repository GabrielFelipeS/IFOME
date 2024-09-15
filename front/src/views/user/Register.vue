<script setup>
import '@/components/user/Header.vue'
import Header from "@/components/user/Header.vue";
import ModalRegister from "@/components/user/register/ModalRegister.vue";
import FormEmail from "@/components/user/register/FormEmail.vue";
import {computed, ref} from "vue";
import FormPersonalData from "@/components/user/register/FormPersonalData.vue";
import FormAddress from "@/components/user/register/FormAddress.vue";

const formData = ref({
	email: '',
	name: '',
	phone: '',
	dateOfBirth: '',
	cpf: '',
	password: '',
	passwordConfirmation: '',
	addressName: '',
	cep: '',
	state: '',
	city: '',
	neighborhood: '',
	address: '',
	houseNumber: '',
	complement: '',
	details: '',
});

const currentComponent = ref(ModalRegister);

const currentStep = ref(0);
const steps = {
	0: ModalRegister,
	1: FormEmail,
	2: FormPersonalData,
	3: FormAddress,
};
const nextStep = () => {
	currentStep.value++;
}
const previousStep = () => {
	currentStep.value--;
}

const loadComponent = computed(() => {
	return steps[currentStep.value];
});

const sendForm = () => {
	const form = new FormData();
	form.append("email", formData.value.email);
	form.append("password", formData.value.password);
	form.append("confirmationPassword", formData.value.passwordConfirmation);
	form.append("dateOfBirth", formData.value.dateOfBirth);
	form.append("cpf", formData.value.cpf);
	form.append("phone", formData.value.phone);

	const address = new Array(
		{
			nameAddress: 'Endereço Principal',
			cep: formData.value.cep,
			neighborhood: formData.value.neighborhood,
			city: formData.value.city,
			state: formData.value.state,
			address: formData.value.address,
			complement: formData.value.complement,
			number: formData.value.houseNumber,
			details: formData.value.details,
		},
	);
	form.append("address", address);

	console.log(Object.fromEntries(form));
};

</script>

<template>
	<div class="content">
		<div class="header">
			<Header v-if="currentStep === 0" />
			<Header logo v-if="currentStep > 0" />
		</div>
		<div class="main">
			<KeepAlive>
				<component
					:is="loadComponent"
					v-model:formData="formData"
					@next-step="nextStep"
					@previous-step="previousStep"
					@submit-form="sendForm"
				/>
			</KeepAlive>
		</div>
	</div>
</template>

<style lang="scss" scoped>

	.content {
		@apply w-screen min-h-screen flex flex-col align-middle;
		background-image: url("../../assets/img/user/bg-cadastro.jpg");
		background-color: #fff;
		background-position: center;
		background-size: cover;
	}

	.header {
		@apply flex flex-row
	}

	.main {
		@apply flex flex-row flex-grow h-full w-full justify-center items-center;
	}

</style>