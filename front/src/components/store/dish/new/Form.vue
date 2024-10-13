<script setup>
import Modal from "@/components/user/Modal.vue";
import {onMounted, ref, watch} from "vue";
import Autocomplete from "@trevoreyre/autocomplete-vue";
import {data} from "autoprefixer";
import axios from "axios";

const selectedFiles = ref([]);
const errorPhotos = ref(false);

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

const categorias = ref([]);
async function getCategories() {
	const apiCategories = await axios.get('https://www.themealdb.com/api/json/v1/1/categories.php');
	apiCategories.data.categories.forEach(category => {
		categorias.value.push(category.strCategory);
	});
}
getCategories();

function search(input) {
	console.log(categorias.value);
	if (!input) {
		return categorias.value;
	}
	return categorias.value.filter(category => {
		return category.toLowerCase().includes(input.toLowerCase());
	});
}

watch(selectedFiles, () => {
	if (selectedFiles.value.length > 0) {
		document.getElementById('drag-drop-photo').style.display = 'none';
	} else {
		document.getElementById('drag-drop-photo').style.removeProperty('display');
	}
});
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
					   required id="dishName">
				<label for="description">Descrição</label>
				<input type="text" placeholder="Digite a descrição do prato" class="form-input"
					   required id="description">
				<label for="price">Valor</label>
				<input type="text" placeholder="R$ 00,00" class="form-input"
					   required id="price">
				<label for="category">Categoria</label>
				<autocomplete :search="search" placeholder="Selecione ou digite uma categoria"
							  class="w-full">
				</autocomplete>
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
				<div class="mt-4 w-full">
					<div v-for="(file, index) in selectedFiles" :key="index"
						 class="flex items-center justify-between border-gray-300 border-dashed border-2 rounded-3xl font-thin pl-5">
						<span class="h-full">{{ file.name }}</span>
						<button class="bg-primary text-white w-[20%] md:w-[10%] h-[50px] py-2 rounded-r-3xl"
								@click="removeFile">
							<v-icon name="fa-times"/>
						</button>
					</div>
				</div>
			</div>
			<div class="btn-container">
				<button type="button" class="button text-primary hover:text-white border border-primary
					hover:bg-primary active:bg-primary-dark">
					<v-icon name="fa-times"/>
					Cancelar
				</button>
				<button type="button" class="button bg-primary border border-primary
				 	text-white hover:bg-primary-dark active:bg-primary-darker">
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

.invalid-input-text {
	@apply font-normal text-sm text-primary-dark px-2;
}
</style>