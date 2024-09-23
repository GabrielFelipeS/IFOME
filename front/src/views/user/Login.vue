<script setup>
import {computed, ref} from "vue";
import ModalLogin from "@/components/user/login/ModalLogin.vue";
import Header from "@/components/user/Header.vue";
import {useRouter} from "vue-router";
import FormLogin from "@/components/user/login/FormLogin.vue";

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

function submitLogin(email, password) {
	console.log('login-submitado');
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