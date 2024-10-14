<template>
    <div class="step">
        <h2>Responsável da loja</h2>
        <p>Informe os dados da pessoa que tem o nome no contrato social da empresa, seja como dona, sócia ou sócia
            administrativa.</p>
        <div class="form-group">
            <label for="name">Nome</label>
            <input type="text" id="name" name="name" v-model="name" placeholder="Nome" required />
            <template v-if="stepErros.name">
                <p v-for="error in stepErros.name">{{ error }}</p>
            </template>
        </div>
        <div class="form-group">
            <label for="cpf">CPF</label>
            <MaskInput type="text" id="cpf" v-model="cpf" name="cpf" :value="cpf" placeholder="CPF"
                mask="###.###.###-##" required />
            <template v-if="stepErros.cpf">
                <p v-for="error in stepErros.cpf">{{ error }}</p>
            </template>
        </div>
        <button type="submit" class="btn-primary" :class="stepCompleted ? '' : 'disable'" :disabled="!stepCompleted"
            @click="nextStep">Próximo</button>
    </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { MaskInput } from 'vue-3-mask';

const emit = defineEmits(['nextStep']);
const props = defineProps(['currentData']);

const name = ref('');
const cpf = ref('');

onMounted(() => {
    if (props.currentData) {
        name.value = props.currentData.name || '';
        cpf.value = props.currentData.cpf || '';
    }
});

const stepCompleted = ref(false);

const stepErros = ref({
    name: [],
    cpf: []
});

watch([name, cpf], () => {
    let cpfValue = cpf.value.replace(/\D/g, '');
    if (!name.value || name.value.length < 3) {
        stepErros.value.name = ['Nome inválido'];
    } else {
        stepErros.value.name = false;
    }
    if (!cpfValue || cpfValue.length < 11) {
        stepErros.value.cpf = ['CPF inválido'];
    } else {
        stepErros.value.cpf = false;
    }

    if (Object.values(stepErros.value).every(value => !value)) {
        stepCompleted.value = true;
    } else {
        stepCompleted.value = false;
    }
});

function nextStep() {
    emit('nextStep', { name: name.value, cpf: cpf.value });
}

</script>