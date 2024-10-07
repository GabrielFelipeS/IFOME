<template>
    <div class="step">
        <h2>Endereço do Entregador</h2>
        <p>Preencha as informações de endereço</p>
        <div class="form-group">
            <label for="cep">CEP *</label>
            <MaskInput type="text" id="cep" name="cep" v-model="cep" :value="cep" placeholder="CEP" mask="#####-###" required />
            <template v-if="stepErros.cep">
                <p v-for="error in stepErros.cep" :key="error">{{ error }}</p>
            </template>
        </div>
        <div class="mid">
            <div class="form-group">
                <label for="state">Estado *</label>
                <input type="text" id="state" name="state" v-model="state" placeholder="Estado" required disabled />
                <template v-if="stepErros.state">
                    <p v-for="error in stepErros.state" :key="error">{{ error }}</p>
                </template>
            </div>
            <div class="form-group">
                <label for="city">Cidade *</label>
                <input type="text" id="city" name="city" v-model="city" placeholder="Cidade" required disabled />
                <template v-if="stepErros.city">
                    <p v-for="error in stepErros.city" :key="error">{{ error }}</p>
                </template>
            </div>
        </div>
        <div class="form-group">
            <label for="neighborhood">Bairro *</label>
            <input type="text" id="neighborhood" name="neighborhood" v-model="neighborhood" placeholder="Bairro" required />
            <template v-if="stepErros.neighborhood">
                <p v-for="error in stepErros.neighborhood" :key="error">{{ error }}</p>
            </template>
        </div>
        <div class="form-group">
            <label for="address">Endereço *</label>
            <input type="text" id="address" name="address" v-model="address" placeholder="Endereço" required />
            <template v-if="stepErros.address">
                <p v-for="error in stepErros.address" :key="error">{{ error }}</p>
            </template>
        </div>
        <div class="form-group">
            <label for="number">Número *</label>
            <MaskInput type="text" id="number" name="number" v-model="number" :value="number" placeholder="Número" mask="######" required />
            <template v-if="stepErros.number">
                <p v-for="error in stepErros.number" :key="error">{{ error }}</p>
            </template>
        </div>
        <div class="form-group">
            <label for="complement">Complemento *</label>
            <input type="text" id="complement" name="complement" v-model="complement" placeholder="Complemento" />
            <template v-if="stepErros.complement">
                <p v-for="error in stepErros.complement" :key="error">{{ error }}</p>
            </template>
        </div>
        <div class="form-group">
            <label for="details">Detalhes</label>
            <textarea id="details" name="details" v-model="details" placeholder="Detalhes"></textarea>
        </div>
        <button type="submit" class="btn-text" :class="stepCompleted ? '' : 'disable'" :disabled="!stepCompleted" @click="nextStep">Próximo</button>
    </div>
</template>

<script setup>
import { fetchViaCep } from '@/services/viaCep';
import { ref, watch, onMounted } from 'vue';
import { MaskInput } from 'vue-3-mask';

const props = defineProps(['currentData']);
const emit = defineEmits(['nextStep']);

const cep = ref('');
const state = ref('');
const city = ref('');
const address = ref('');
const number = ref('');
const complement = ref('');
const details = ref('');
const neighborhood = ref('');

const stepCompleted = ref(false);

const stepErros = ref({
    cep: [],
    neighborhood: [],
    address: [],
    number: [],
    complement: [],
});

onMounted(() => {
    if (props.currentData && props.currentData.address) {
        cep.value = props.currentData.address.cep || '';
        state.value = props.currentData.address.state || '';
        city.value = props.currentData.address.city || '';
        address.value = props.currentData.address.address || '';
        number.value = props.currentData.address.number || '';
        neighborhood.value = props.currentData.address.neighborhood || '';
        complement.value = props.currentData.address.complement || '';
    }
});

watch([cep, state, city, address, number, complement, neighborhood], () => {
    stepErros.value.cep = cep.value.length === 9 ? false : ['** Digite um valor válido **'];
    stepErros.value.neighborhood = neighborhood.value ? false : ['** O campo bairro é obrigatório **'];
    stepErros.value.address = address.value ? false : ['** O campo endereço é obrigatório **'];
    stepErros.value.number = number.value ? false : ['** O campo número é obrigatório **'];
    stepErros.value.complement = complement.value ? false : ['** O campo complemento é obrigatório **'];
    stepCompleted.value = Object.values(stepErros.value).every((error) => !error);
});

watch(cep, async (value) => {
    if (value.length === 9) {
        let cep = value.replace('-', '');
        const data = await fetchViaCep(cep);
        if (data.erro) {
            stepErros.value.cep = ['** CEP não encontrado **'];
        } else {
            stepErros.value.cep = false;
            state.value = data.uf;
            city.value = data.localidade;
            address.value = data.logradouro;
            neighborhood.value = data.bairro;
        }
    }
});



function nextStep() {
    if (stepCompleted.value) {
        emit('nextStep', {
            address: {
                nameAddress: 'casa principal',
                cep: cep.value,
                typeResidence: 'casa',
                neighborhood: neighborhood.value,
                city: city.value,
                state: state.value,
                address: address.value,
                complement: complement.value,
                number: number.value,
                details: details.value
            }
        });
    }
}

</script>
