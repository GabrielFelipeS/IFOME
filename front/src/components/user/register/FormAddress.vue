<script setup>
import Modal from "@/components/user/register/Modal.vue";
import Button from "@/components/Button.vue";
import {ref, watch} from "vue";
import { MaskInput } from "vue-3-mask";
import {fetchViaCep} from "@/services/viaCep.js";


const props = defineProps({
	formData: {
		type: Object,
		required: true,
	}
});

const emit = defineEmits(['submit-form']);

watch(() => props.formData.cep, async (value) => {
	if (value.length === 9) {
		let cep = value.replace('-', '');
		const data = await fetchViaCep(cep);
		props.formData.state = data.estado;
		props.formData.city = data.localidade;
		props.formData.neighborhood = data.bairro;
		props.formData.address = data.logradouro;
		document.getElementById('state').setAttribute('disabled', 'true');
		document.getElementById('city').setAttribute('disabled', 'true');

	} else {
		document.getElementById('state').removeAttribute('disabled');
		document.getElementById('city').removeAttribute('disabled');
	}
});

const sendSubmit = () => {
	if (props.formData.cep.length > 0 &&
		props.formData.state.length > 0 &&
		props.formData.city.length > 0 &&
		props.formData.neighborhood.length > 0 &&
		props.formData.address.length > 0 &&
		props.formData.houseNumber.length > 0 &&
		props.formData.complement.length > 0 &&
		props.formData.details.length > 0
	) {
		emit('submit-form');
	} else {

	}
}

</script>

<template>
	<Modal>
		<header class="title">
			Insira seus dados de endereço
		</header>
		<div class="flex flex-row pl-4 mt-4">
			Preencha os dados a seguir para prosseguirmos com seu cadastro
		</div>
		<div class="main">
			<div class="form-group">
				<label for="cep" class="form-label">CEP</label>
				<MaskInput type="text" placeholder="00000-000" class="form-input" id="cep"
					   v-model="formData.cep" mask="#####-###" />
				<label for="state" class="form-label">Estado</label>
				<input type="text" placeholder="Exemplo: São Paulo" class="form-input" id="state"
					   v-model="formData.state" >
				<label for="city" class="form-label">Cidade</label>
				<input type="text" placeholder="Exemplo: Guarulhos" class="form-input" id="city"
					   v-model="formData.city" >
				<label for="neighborhood" class="form-label">Bairro</label>
				<input type="text" placeholder="Exemplo: Parque Santos Dumont" class="form-input" id="neighborhood"
					   v-model="formData.neighborhood" >
				<label for="address" class="form-label">Endereço</label>
				<input type="text" placeholder="Exemplo: Rua Dantas" class="form-input" id="address"
					   v-model="formData.address" >
				<label for="houseNumber" class="form-label">Número</label>
				<input type="text" placeholder="Exemplo: 12A" class="form-input" id="houseNumber"
					   v-model="formData.houseNumber" >
				<label for="complement" class="form-label">Complemento</label>
				<input type="text" placeholder="Exemplo: AP12 BL5" class="form-input" id="complement"
					   v-model="formData.complement" >
				<label for="details" class="form-label">Detalhes</label>
				<input type="text" placeholder="Exemplo: Em frente a padaria do seu zé" class="form-input" id="details"
					   v-model="formData.details" >
			</div>
			<div class="btn-container">
				<Button href="#" @click="$emit('previous-step')" inversed
						class="button text-primary-subtle border border-primary mr-[15%]">Voltar</Button>
				<Button href="#" @click="sendSubmit"
						class="button bg-primary text-white">Finalizar</Button>
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
	@apply flex flex-row justify-between mt-4  px-[10%];
}
.button {
	@apply rounded-md w-full flex justify-center align-middle py-[2%];
}
.form-group {
	@apply flex flex-col justify-center items-start align-baseline mx-4;
}
.form-input {
	@apply rounded-md border border-tertiary-subtle p-2 w-full mb-2;

	&:disabled {
		@apply bg-tertiary-subtle
	}
}
.form-label {
	@apply my-1 text-sm;
}
</style>