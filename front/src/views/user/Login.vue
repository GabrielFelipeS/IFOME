<script setup>
import {computed, ref} from "vue";
import ModalLogin from "@/components/user/login/ModalLogin.vue";
import Header from "@/components/user/Header.vue";
import {useRouter} from "vue-router";
import FormLogin from "@/components/user/login/FormLogin.vue";
import api from "@/services/api.js";
import {useToast} from "vue-toast-notification";
import PasswordReset from "@/components/user/login/PasswordReset.vue";
import PasswordResetSuccess from "@/components/user/login/PasswordResetSuccess.vue";

const components = {
	'modal-login': ModalLogin,
	'form-login': FormLogin,
	'password-reset': [
		PasswordReset,
		PasswordResetSuccess,
	],
}
const homeComponent = 'modal-login';
const componentName = ref('modal-login');
const lastComponent = ref('modal-login');
const componentStep = ref(0);

const currentComponent = computed(() => {
	if (components[componentName.value]) {
		if (Array.isArray(components[componentName.value])) {
			return components[componentName.value][componentStep.value];
		}
		return components[componentName.value];
	} else {
		return components['modal-login'];
	}
});

function loadComponent(name) {
	if (name) {
		lastComponent.value = componentName.value;
		componentName.value = name;
		componentStep.value = 0;
	}
}

function nextStep() {
	componentStep.value++;
}

function goBack() {
	if (componentName.value === lastComponent.value) {
		componentName.value = homeComponent;
		return;
	}

	if (Array.isArray(components[componentName.value]) && componentStep.value > 0) {
		componentStep.value--;
	} else {
		componentName.value = lastComponent.value;
		componentStep.value = 0;
	}
}

// Função para mandar como prop
const router = useRouter();
const goToPage = async (page) => {
	await router.push({ name: page });
};

const $toast = useToast();
async function submitLogin(data) {
	if (!data.email || !data.password) {
		$toast.error('Parâmetros Inválidos', {duration: 10000});
		return;
	}
	console.log(JSON.stringify(data));
	await api.post('auth/client/login', JSON.stringify(data))
		.then((response) => {
			if (response.status === 200 || response.status === 201) {
				$toast.success('Login realizado com sucesso!');
				localStorage.setItem('token', response.data.data.token);
				router.push({ name: 'home-site' });
			} else {
				console.log(response)
				$toast.error(response.data.message, {});
			}
		})
		.catch(error => {
			let errorMessage = error.response?.data?.message;
			$toast.error(errorMessage, {duration: 10000});
			if (error.response?.data?.errors) {
				let errors = error.response?.data?.errors;
				errors = new Map(Object.entries(errors));
				errors.forEach((messageArray) => {
					messageArray.forEach((message) => {
						$toast.error(message, {duration: 10000});
					})
				})
			}
		});
}

function submitPasswordReset (data) {
	console.log(data);
	nextStep();
}
</script>

<template>
	<div class="content"
		 v-bind:class="[componentName === 'modal-login' ? 'bg-image' : '']">
		<div class="header">
			<Header :logo="componentName !== 'modal-login'" @go-back="goBack"
				:home-page="'user-login'" :back-button="(componentName !== 'modal-login')" />
		</div>
		<div class="main">
			<component class="component"
				:is="currentComponent"
				@load-component="loadComponent"
				@submit-login="submitLogin"
				@submit-password-reset="submitPasswordReset"
				@go-back="goBack"
			/>
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

.component {
	@apply max-w-[600px];
}

</style>