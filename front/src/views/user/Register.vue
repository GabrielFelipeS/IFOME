<script setup>
import '@/components/user/Header.vue'
import Header from "@/components/user/Header.vue";
import ModalRegister from "@/components/user/register/ModalRegister.vue";
import FormEmail from "@/components/user/register/FormEmail.vue";
import {computed, ref} from "vue";
import FormPersonalData from "@/components/user/register/FormPersonalData.vue";
import FormAddress from "@/components/user/register/FormAddress.vue";
import api from "@/services/api.js";
import FormSuccess from "@/components/user/register/FormSuccess.vue";
import axios from "axios";

document.querySelector('#app').setAttribute('style', 'overflow-x: hidden');

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
	typeResidence: '',
});

const currentStep = ref(0);
const steps = {
	0: ModalRegister,
	1: FormEmail,
	2: FormPersonalData,
	3: FormAddress,
	4: FormSuccess,
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
	let [day, month, year] = formData.value.dateOfBirth.split('/').map(String);
	let date = year + '-' + month + '-' + day;
	let data = {
		"name": formData.value.name,
		"email": formData.value.email,
		"password": formData.value.password,
		"confirmationPassword": formData.value.passwordConfirmation,
		"dateOfBirth": date,
		"cpf": formData.value.cpf,
		"phone": formData.value.phone,
		"address": [
			{
				"nameAddress": "Endereço Principal",
				"typeResidence": formData.value.typeResidence,
				"cep": formData.value.cep,
				"neighborhood": formData.value.neighborhood,
				"city": formData.value.city,
				"state": formData.value.state,
				"address": formData.value.address,
				"complement": formData.value.complement,
				"number": formData.value.houseNumber + '',
				"details": formData.value.details
			}
		]
	}

	axios.post(import.meta.env.VITE_API_URL + 'auth/client', JSON.stringify(data), {
		headers: {
			"Content-Type": "application/json",
		}
	})
		.then(response => {
			console.log('Response:', response.data);
			currentStep.value++;
		})
		.catch(error => {
			console.error('Error:', error);
		});
	console.log(JSON.stringify(data));
};

</script>

<template>
	<div class="content"
		 v-bind:class="[currentStep === 0 ? 'bg-image' : '']">
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
		background-color: #fff;
		background-position: center;
		background-size: cover;
	}
	.bg-image {
		background-image: url("../../assets/img/user/bg-cadastro.jpg");
	}

	.header {
		@apply flex flex-row
	}

	.main {
		@apply flex flex-row flex-grow h-full w-full justify-center items-center;
	}

</style>