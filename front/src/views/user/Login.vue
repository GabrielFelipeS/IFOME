<script setup>
import {computed, ref} from "vue";
import ModalLogin from "@/components/user/login/ModalLogin.vue";
import Header from "@/components/user/Header.vue";
import {useRouter} from "vue-router";
import FormLogin from "@/components/user/login/FormLogin.vue";
import api from "@/services/api.js";
import axios from "axios";
import {useToast} from "vue-toast-notification";

const components = {
	'modal-login': ModalLogin,
	'form-login': FormLogin,
}
const componentName = ref('modal-login');
const currentComponent = computed(() => {
	if (components[componentName.value]) {
		return components[componentName.value];
	} else {
		return components['modal-login'];
	}
});

function loadComponent(name) {
	if (name) {
		componentName.value = name;
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
				console.log(response);
			} else {
				console.log(response)
				$toast.error(response.data.message, {});
			}
		})
		.catch(error => {
			const errorMessage = JSON.stringify(error.response?.data) || 'Erro ao enviar os dados.';
			$toast.error(errorMessage, {duration: 10000});
			console.error('Error:', error);
		});
}

function submitPasswordReset (email) {

}
</script>

<template>
	<div class="content"
		 v-bind:class="[componentName === 'modal-login' ? 'bg-image' : '']">
		<div class="header">
			<Header v-if="componentName === 'modal-login'" />
			<Header logo v-if="componentName !== 'modal-login'" />
		</div>
		<div class="main">
			<KeepAlive >
				<component
					:is="currentComponent"
					@load-component="loadComponent"
					@submit-login="submitLogin"
					@submit-password-reset="submitPasswordReset"
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