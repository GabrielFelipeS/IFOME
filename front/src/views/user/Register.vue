<script setup>
import '@/components/user/Header.vue'
import Header from "@/components/user/Header.vue";
import ModalRegister from "@/components/user/register/ModalRegister.vue";
import FormEmail from "@/components/user/register/FormEmail.vue";
import {computed, ref} from "vue";

const formData = ref({
	email: '',
	name: '',
	phone: '',
	dateOfBirth: '',
	cpf: '',
	password: '',
	passwordConfirmation: '',
	cep: '',
	state: '',
	city: '',
	neighbourhood: '',
	street: '',
	houseNumber: '',
	complement: ''
});

const currentComponent = ref(ModalRegister);

const currentStep = ref(0);
const steps = {
	0: ModalRegister,
	1: FormEmail,
};
const nextStep = () => {
	currentStep.value++;
	console.log(currentStep.value);
}
const previousStep = () => {
	currentStep.value--;
}

const loadComponent = computed(() => {
	return steps[currentStep.value];
});

const teste = () => {
	console.log(formData.value);
	console.log(currentStep.value);
};

</script>

<template>
	<div class="content">
		<Header v-if="currentStep === 0" />
		<Header logo v-if="currentStep > 0" />
		<div class="main">
			<KeepAlive>
				<component
					:is="loadComponent"
					v-model:formData="formData"
					@next-step="nextStep"
					@previous-step="previousStep"
				/>
			</KeepAlive>
		</div>
		<button type="button" class="w-full mt-10 bg-primary" @click="teste">Teste</button>
		<button type="button" class="w-full mt-10 bg-primary" @click="currentStep = 0">voltar ao inicio</button>

	</div>
</template>

<style lang="scss" scoped>

	.content {
		@apply w-screen h-screen flex flex-col align-middle;
		background-image: url("../../assets/img/user/bg-cadastro.jpg");
		background-color: #fff;
		background-position: center;
		background-size: cover;
	}

	.main {
		@apply mt-[50%];
	}

</style>