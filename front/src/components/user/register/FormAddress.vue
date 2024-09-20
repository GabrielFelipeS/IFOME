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

const erros = ref({
	cep: '',
	state: '',
	city: '',
	neighborhood: '',
	address: '',
	houseNumber: '',
	complement: '',
	details: ''
});

const emit = defineEmits(['submit-form', 'next-step']);

watch(() => props.formData.cep, async (value) => {
	if (value.length === 9) {
		let cep = value.replace('-', '');
		const data = await fetchViaCep(cep);
		if (data.erro) {
			erros.value.cep = 'Digite um CEP válido';
		}
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

watch(props.formData, () => {
	if (props.formData.cep.length === 0) {
		erros.value.cep = '';
	}
	if (props.formData.state.length === 0) {
		erros.value.state = '';
	}
	if (props.formData.city.length === 0) {
		erros.value.city = '';
	}
	if (props.formData.neighborhood.length === 0) {
		erros.value.neighborhood = '';
	}
	if (props.formData.address.length === 0) {
		erros.value.address = '';
	}
	if (props.formData.houseNumber.length === 0) {
		erros.value.houseNumber = '';
	}
	if (props.formData.complement.length === 0) {
		erros.value.complement = '';
	}
}, { deep:true })

const validateForm = () => {
	let valid = true;

	if (props.formData.cep.length === 0) {
		erros.value.cep = 'Preencha o campo CEP.';
		valid = false;
	} else {
		erros.value.cep = '';
	}

	if (props.formData.state.length === 0) {
		erros.value.state = 'Preencha o campo Estado.';
		valid = false;
	} else {
		erros.value.state = '';
	}

	if (props.formData.city.length === 0) {
		erros.value.city = 'Preencha o campo Cidade.';
		valid = false;
	} else {
		erros.value.city = '';
	}

	if (props.formData.neighborhood.length === 0) {
		erros.value.neighborhood = 'Preencha o campo Bairro.';
		valid = false;
	} else {
		erros.value.neighborhood = '';
	}

	if (props.formData.address.length === 0) {
		erros.value.address = 'Preencha o campo Endereço.';
		valid = false;
	} else {
		erros.value.address = '';
	}

	if (props.formData.houseNumber.length === 0) {
		erros.value.houseNumber = 'Preencha o campo Número.';
		valid = false;
	} else {
		erros.value.houseNumber = '';
	}

	if (props.formData.complement.length === 0) {
		erros.value.complement = 'Preencha o campo Complemento.';
		valid = false;
	} else {
		erros.value.complement = '';
	}

	return valid;
};

const sendSubmit = () => {
	if (validateForm()) {
		emit('submit-form');
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
				<p v-if="erros.cep !== ''" class="invalid-input-text">{{ erros.cep }}</p>

				<label for="state" class="form-label">Estado</label>
				<input type="text" placeholder="Exemplo: São Paulo" class="form-input" id="state"
					   v-model="formData.state" >
				<p v-if="erros.state !== ''" class="invalid-input-text">{{ erros.state }}</p>

				<label for="city" class="form-label">Cidade</label>
				<input type="text" placeholder="Exemplo: Guarulhos" class="form-input" id="city"
					   v-model="formData.city" >
				<p v-if="erros.city !== ''" class="invalid-input-text">{{ erros.city }}</p>

				<label for="neighborhood" class="form-label">Bairro</label>
				<input type="text" placeholder="Exemplo: Parque Santos Dumont" class="form-input" id="neighborhood"
					   v-model="formData.neighborhood" >
				<p v-if="erros.neighborhood !== ''" class="invalid-input-text">{{ erros.neighborhood }}</p>

				<label for="address" class="form-label">Endereço</label>
				<input type="text" placeholder="Exemplo: Rua Dantas" class="form-input" id="address"
					   v-model="formData.address" >
				<p v-if="erros.address !== ''" class="invalid-input-text">{{ erros.address }}</p>

				<label for="houseNumber" class="form-label">Número</label>
				<input type="text" placeholder="Exemplo: 12A" class="form-input" id="houseNumber"
					   v-model="formData.houseNumber" >
				<p v-if="erros.houseNumber !== ''" class="invalid-input-text">{{ erros.houseNumber }}</p>

				<label for="complement" class="form-label">Complemento</label>
				<input type="text" placeholder="Exemplo: AP12 BL5" class="form-input" id="complement"
					   v-model="formData.complement" >
				<p v-if="erros.complement !== ''" class="invalid-input-text">{{ erros.complement }}</p>

				<label for="details" class="form-label">Detalhes (opcional)</label>
				<input type="text" placeholder="Exemplo: Em frente a padaria do seu zé" class="form-input" id="details"
					   v-model="formData.details" >
				<p v-if="erros.details !== ''" class="invalid-input-text">{{ erros.details }}</p>
				<label for="" class="form-label">Tipo de residência</label>
				<div class="checkbox-div">
					<div class="checkbox-row">
						<input type="radio" placeholder="Exemplo: Em frente a padaria do seu zé" class="form-checkbox" id="checkbox-casa"
							   v-model="formData.typeResidence" value="casa">
						<label for="checkbox-casa" class="form-label">Casa</label>
					</div>
					<div class="checkbox-row">
						<input type="radio" placeholder="Exemplo: Em frente a padaria do seu zé" class="form-checkbox" id="checkbox-apartamento"
							   v-model="formData.typeResidence" value="apartamento">
						<label for="checkbox-apartamento" class="form-label">Apartamento</label>
					</div>
					<div class="checkbox-row">
						<input type="radio" placeholder="Exemplo: Em frente a padaria do seu zé" class="form-checkbox" id="checkbox-condominio"
								v-model="formData.typeResidence" value="condominio">
						<label for="checkbox-condominio" class="form-label">Condomínio</label>
					</div>
				</div>
				<p v-if="erros.details !== ''" class="invalid-input-text">{{ erros.details }}</p>
			</div>
			<div class="btn-container">
				<Button href="#" @click="$emit('previous-step')" inversed
						class="button text-primary-subtle border border-primary mr-[15%] hover:text-white
						 hover:bg-primary active:bg-primary-dark hover:border-white">Voltar</Button>
				<Button href="#" @click="sendSubmit"
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
		@apply bg-soft-border;
	}
}
.form-label {
	@apply my-1 text-sm;
}
.checkbox-div {
	@apply flex flex-row justify-start mx-4 my-2.5 w-full gap-5 ;
}
.checkbox-row {
	@apply flex flex-row justify-start gap-1.5;

	.form-label {
		@apply font-semibold;
	}
}
.invalid-input-text {
	@apply font-light text-sm text-red-500 px-2;
}
</style>