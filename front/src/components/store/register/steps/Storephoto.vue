<template>
    <div class="step">
        <h2>Fotos da loja</h2>
        <p>Selecione as fotos do seu negócio</p>

        <!-- Área de upload de fotos -->
        <div class="border-2 border-dashed border-gray-300 rounded-lg p-5 text-center h-[300px] flex items-center justify-center"
            @dragover.prevent="dragOver" @dragleave.prevent="dragLeave" @drop.prevent="drop">
            <input type="file" id="photo" name="photo" class="hidden" @change="handleFileChange" />
            <label for="photo" class="cursor-pointer flex flex-col items-center justify-center">
                <svg data-name="Livello 1" id="Livello_1" viewBox="0 0 128 128" xmlns="http://www.w3.org/2000/svg"
                    width="30" height="30" class="mb-2">
                    <title />
                    <path
                        d="M37.09,32.91A3,3,0,0,0,39.21,32L61,10.24V91a3,3,0,0,0,6,0V10.24L88.79,32A3,3,0,0,0,93,27.79L66.12.88A3,3,0,0,0,65.66.5L65.43.38a3,3,0,0,0-.29-.15,3,3,0,0,0-.31-.1L64.59.06a3,3,0,0,0-1.18,0l-.25.08a2.93,2.93,0,0,0-.31.1,3,3,0,0,0-.29.15L62.34.5a3,3,0,0,0-.46.38L35,27.79a3,3,0,0,0,2.12,5.12Z" />
                    <path
                        d="M125,88a3,3,0,0,0-3,3v22a9,9,0,0,1-9,9H15a9,9,0,0,1-9-9V91a3,3,0,0,0-6,0v22a15,15,0,0,0,15,15h98a15,15,0,0,0,15-15V91A3,3,0,0,0,125,88Z" />
                </svg>

                <span>Arraste e solte sua foto aqui</span>
                <span class="text-sm text-gray-400">ou <span class="text-blue-500">clique aqui</span> e
                    selecione</span>
            </label>
        </div>
        <p v-if="errorPhotos" class="alert">{{ errorPhotos[0] }}</p>

        <div class="mt-4 w-full">
            <div v-for="(file, index) in selectedFiles" :key="index"
                class="flex items-center justify-between border-gray-300 border-dashed border-2 rounded-3xl font-thin pl-5">
                <span class="truncate">{{ file.name }}</span>
                <button class="bg-red-500 text-white w-[30%] md:w-[10%] h-[50px] py-2 rounded-r-3xl"
                    @click="removeFile">
                    X
                </button>
            </div>

        </div>

        <button type="submit" class="btn-primary" :class="stepCompleted ? '' : 'disable'" :disabled="!stepCompleted"
            @click="nextStep">Próximo</button>
    </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';

const emit = defineEmits(['nextStep']);
const props = defineProps(['currentData']);

const selectedFiles = ref([]);
const errorPhotos = ref(false);

onMounted(() => {
    if (props.currentData && props.currentData.photos) {
        selectedFiles.value = props.currentData.photos || [];
    }
});

const stepCompleted = ref(false);

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

const stepErros = ref({
    photos: false,
});

watch(selectedFiles, () => {
    if (selectedFiles.value.length === 0) {
        stepErros.value.photos = true;
    } else {
        stepErros.value.photos = false;
    }

    if (stepErros.value.photos) {
        stepCompleted.value = false;
    } else {
        stepCompleted.value = true;
    }
});

function nextStep() {
    emit('nextStep', { photos: selectedFiles.value });
}

</script>