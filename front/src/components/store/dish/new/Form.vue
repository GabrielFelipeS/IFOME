<script setup>
import Modal from "@/components/user/Modal.vue";
import {onMounted, ref, watch} from "vue";
import Autocomplete from "@trevoreyre/autocomplete-vue";
import axios from "axios";
import Form from "@/components/store/dish/new/Form.vue";
import {useToast} from "vue-toast-notification";

const selectedFiles = ref([]);
const errorPhotos = ref(false);

// Data do cadastro do prato, a imagem fica em um Ref diferente
const dishData = ref({
	name: '',
	description: '',
	price: '',
	dishCategory: '',
	availability: '',
});
const preco = ref('');

// DRAG DROP
function handleFileChange(event) {
	const files = Array.from(event.target.files);
	if (files.length > 0) {
		const file = files[0];
		if (file.size > 2 * 1024 * 1024) {
			errorPhotos.value = ['O arquivo deve ter no máximo 2MB'];
			selectedFiles.value = [];
		} else {
			errorPhotos.value = false;
			selectedFiles.value = [file];
		}
	}
}
function dragOver(event) {
	event.preventDefault();
	event.currentTarget.classList.add('bg-gray-100');
}
function dragLeave(event) {
	event.currentTarget.classList.remove('bg-gray-100');
}
function drop(event) {
	event.currentTarget.classList.remove('bg-gray-100');
	const files = Array.from(event.dataTransfer.files);
	if (files.length > 0) {
		const file = files[0];
		if (file.size > 2 * 1024 * 1024) {
			errorPhotos.value = ['O arquivo deve ter no máximo 2MB'];
			selectedFiles.value = [];
		} else {
			errorPhotos.value = false;
			selectedFiles.value = [file];
		}
	}
}
function removeFile() {
	selectedFiles.value = [];
}
watch(selectedFiles, () => {
	if (selectedFiles.value.length > 0) {
		document.getElementById('drag-drop-photo').style.display = 'none';
	} else {
		document.getElementById('drag-drop-photo').style.removeProperty('display');
	}
});
// End DRAG DROP

// SELECT CATEGORIAS
const categorias = ref(["Aves", "Bebida", "Bebidas Alcoólicas", "Bebidas Não Alcoólicas", "Café e Chá", "Carnes", "Cozinha Árabe", "Cozinha Brasileira", "Cozinha Chinesa", "Cozinha Francesa", "Cozinha Indiana", "Cozinha Italiana", "Cozinha Mexicana", "Cozinha Tailandesa", "Entrada", "Frutos do Mar", "Hambúrgueres", "Massas", "Peixes", "Pizzas", "Prato Principal", "Pratos Veganos", "Pratos Vegetarianos", "Risotos", "Saladas", "Sanduíches", "Sobremesa", "Sorvetes", "Sopas", "Sushi e Sashimi", "Tacos e Burritos", "Temakis"]);
// END SELECT CATEGORIAS

// Máscara para o input de preço para ele ficar com a vírgula do centavo
watch(preco, () => {
	let dinheiro = preco.value.replace(/\D/g, '');
	if (dinheiro.length > 2) {
		let parte1 = dinheiro.slice(0, -2);
		let parte2 = dinheiro.slice(-2);
		dinheiro = parte1 + ',' + parte2;
	}
	dishData.value.price = dinheiro.replace(',', '.');
	preco.value = dinheiro;
});

const errors = ref({
	name: [],
	description: [],
	price: [],
	dishCategory: [],
	availability: [],
	file: [],
});
function validateForm() {
	let validated = true;
	errors.value = {
		name: [],
		description: [],
		price: [],
		dishCategory: [],
		availability: [],
		file: [],
	}

	if (dishData.value.name.length === 0) {
		errors.value.name.push('Preencha o campo Nome do prato.');
		validated = false;
	} else {
		errors.value.name = [];
	}
	if (dishData.value.description.length === 0) {
		errors.value.description.push('Preencha o campo Descrição.');
		validated = false;
	} else {
		errors.value.description = [];
	}
	if (dishData.value.price.length === 0) {
		errors.value.price.push('Preencha o campo Preço.');
		validated = false;
	} else {
		errors.value.price = [];
	}
	if (dishData.value.dishCategory.length === 0) {
		errors.value.dishCategory.push('Preencha o campo Categoria.');
		validated = false;
	} else {
		errors.value.dishCategory = [];
	}
	if (dishData.value.availability.length === 0) {
		errors.value.availability.push('Preencha o campo Disponibilidade.');
		validated = false;
	} else {
		errors.value.availability = [];
	}
	if (selectedFiles.value.length === 0) {
		errors.value.file.push('Insira uma foto.');
		validated = false;
	} else {
		errors.value.file = [];
	}

	return validated;
}

const $toast = useToast();

async function saveDish() {
	if (validateForm()) {
		let formData = new FormData();
		formData.append('file', selectedFiles.value[0]);
		formData.append('dish', new Blob([JSON.stringify(dishData.value)], {
			type: 'application/json',
		}));

		const response = await axios.post(`${import.meta.env.VITE_API_URL}dish`, formData, {
			headers: {
				'Content-Type': 'multipart/form-data',
				'Authorization': `Bearer ${localStorage.getItem('token')}`,
			}
		});

		if (response.status === 201) {
			$toast.success('Prato cadastrado com sucesso!');
			console.log(response.data);
		} else {
			$toast.error(response.data.message, {});
			console.log(response.data);
		}
	}
}
</script>

<template>
	<Modal>
		<header class="title">
			Cadastro de pratos
		</header>
		<div class="main">
			<div class="form-group">
				<label for="dishName">Nome do prato</label>
				<input type="text" placeholder="Digite o nome do prato" class="form-input"
					   required id="dishName" v-model="dishData.name">
				<p class="invalid-input-text" v-for="error in errors.name">{{ error }}</p>
				<label for="description">Descrição</label>
				<input type="text" placeholder="Digite a descrição do prato" class="form-input"
					   required id="description" v-model="dishData.description">
				<p class="invalid-input-text" v-for="error in errors.description">{{ error }}</p>
				<label for="price">Valor</label>
				<div class="input-money">
					<input type="text" placeholder="00,00" class="form-input input-money"
						   required id="price" v-model="preco">
				</div>
				<p class="invalid-input-text" v-for="error in errors.price">{{ error }}</p>
				<label for="category">Categoria</label>
				<select class="form-input w-full rounded-md bg-white" v-model="dishData.dishCategory">
					<option value="" selected>Selecione uma categoria</option>
					<option v-for="category in categorias" :key="category" :value="category">{{ category }}</option>
				</select>
				<p class="invalid-input-text" v-for="error in errors.dishCategory">{{ error }}</p>
				<p class="text-center w-full mb-1 mt-3 font-semibold">Foto do prato</p>
				<div
					class="border-2 border-dashed border-gray-300 rounded-lg p-5 w-full text-center h-[20%] flex items-center justify-center"
					@dragover.prevent="dragOver" @dragleave.prevent="dragLeave" @drop.prevent="drop"
					id="drag-drop-photo">
					<input type="file" id="photo" name="photo" class="hidden" @change="handleFileChange"/>
					<label for="photo" class="cursor-pointer flex flex-col items-center justify-center">
						<svg data-name="Livello 1" id="Livello_1" viewBox="0 0 128 128"
							 xmlns="http://www.w3.org/2000/svg"
							 width="30" height="30" class="mb-2">
							<title/>
							<path
								d="M37.09,32.91A3,3,0,0,0,39.21,32L61,10.24V91a3,3,0,0,0,6,0V10.24L88.79,32A3,3,0,0,0,93,27.79L66.12.88A3,3,0,0,0,65.66.5L65.43.38a3,3,0,0,0-.29-.15,3,3,0,0,0-.31-.1L64.59.06a3,3,0,0,0-1.18,0l-.25.08a2.93,2.93,0,0,0-.31.1,3,3,0,0,0-.29.15L62.34.5a3,3,0,0,0-.46.38L35,27.79a3,3,0,0,0,2.12,5.12Z"/>
							<path
								d="M125,88a3,3,0,0,0-3,3v22a9,9,0,0,1-9,9H15a9,9,0,0,1-9-9V91a3,3,0,0,0-6,0v22a15,15,0,0,0,15,15h98a15,15,0,0,0,15-15V91A3,3,0,0,0,125,88Z"/>
						</svg>
						<span>Arraste e solte sua foto aqui</span>
						<span class="text-sm text-gray-400">ou <span class="text-blue-500">clique aqui</span> e
                    selecione</span>
					</label>
				</div>
				<div class="mt-3 mb-5 w-full">
					<div v-for="(file, index) in selectedFiles" :key="index"
						 class="flex items-center justify-between border-gray-300 border-dashed border-2 rounded-3xl font-thin pl-5">
						<span class="h-full">{{ file.name }}</span>
						<button class="bg-primary text-white w-[20%] md:w-[10%] h-[50px] py-2 rounded-r-3xl"
								@click="removeFile">
							<v-icon name="fa-times"/>
						</button>
					</div>
				</div>
				<p class="invalid-input-text mb-3" v-for="error in errors.file">{{ error }}</p>
				<label class="form-label">Disponibilidade</label>
				<div class="checkbox-div">
					<div class="checkbox-row">
						<input type="radio" placeholder="Exemplo: Em frente a padaria do seu zé" class="form-checkbox" id="checkbox-ativo"
							   v-model="dishData.availability" value="Disponível">
						<label for="checkbox-ativo" class="form-label">Disponível</label>
					</div>
					<div class="checkbox-row">
						<input type="radio" placeholder="Exemplo: Em frente a padaria do seu zé" class="form-checkbox" id="checkbox-inativo"
							   v-model="dishData.availability" value="Indisponível">
						<label for="checkbox-inativo" class="form-label">Indisponível</label>
					</div>
				</div>
				<p class="invalid-input-text" v-for="error in errors.availability">{{ error }}</p>
			</div>
			<div class="btn-container">
				<button type="button" class="button text-primary hover:text-white border border-primary
					hover:bg-primary active:bg-primary-dark">
					<v-icon name="fa-times"/>
					Cancelar
				</button>
				<button type="button" class="button bg-primary border border-primary
				 	text-white hover:bg-primary-dark active:bg-primary-darker" @click="saveDish">
					Salvar prato
					<v-icon name="fa-save"/>
				</button>
			</div>
		</div>
	</Modal>
</template>

<style scoped>
.title {
	@apply flex flex-row font-semibold pl-4 text-2xl ;

}

.main {
	@apply mt-5
}

.btn-container {
	@apply flex flex-col-reverse justify-center items-center align-baseline mt-10 px-[10%] gap-5;
	@apply md:flex-row md:gap-10;
}

.button {
	@apply rounded-md px-[2%] py-3 w-full flex justify-center items-center gap-3;
	@apply md:p-[2%];
}

.form-group {
	@apply flex flex-col justify-start items-start mx-3 ;
}

.form-input {
	@apply rounded-md border border-tertiary-subtle p-2 w-full mb-2;

	&:disabled {
		@apply bg-soft-border;
	}
}

.input-money {
	@apply flex flex-row w-full;
	&::before {
		@apply h-full flex align-middle justify-center items-center p-2;
		@apply border border-r-0 border-tertiary-subtle rounded-md rounded-e-none;
		content: 'R$';
	}
}
.input-money .form-input {
	@apply rounded-s-none;
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
	@apply font-normal text-sm text-primary-dark ;
}
</style>