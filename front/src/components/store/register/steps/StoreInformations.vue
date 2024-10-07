<template>
    <div class="step">
        <h2>Informações da loja</h2>
        <p>Preencha com os dados do seu negócio</p>
        <div class="form-group">
            <label for="cnpj">CNPJ</label>
            <MaskInput type="text" id="cnpj" v-model="cnpj" name="cnpj" :value="cnpj" placeholder="CNPJ"
                mask="##.###.###/####-##" required />
            <template v-if="stepErros.cnpj">
                <p v-for="error in stepErros.cnpj">{{ error }}</p>
            </template>
        </div>
        <div class="form-group">
            <label for="nameStore">Nome do Restaurante (como aparecerá no app)</label>
            <input type="text" id="nameStore" v-model="nameStore" name="nameStore" placeholder="Nome da loja"
                required />
            <template v-if="stepErros.nameStore">
                <p v-for="error in stepErros.nameStore">{{ error }}</p>
            </template>
        </div>
        <div class="form-group">
            <label for="phone">Telefone do Restaurante</label>
            <MaskInput type="text" id="phone" v-model="phone" :value="phone" name="phone" placeholder="Telefone"
                mask="(##) #####-####" required />
            <template v-if="stepErros.phone">
                <p v-for="error in stepErros.phone">{{ error }}</p>
            </template>
        </div>
        <div class="mid">
            <div class="form-group">
                <label for="specialty">Especialidade</label>
                <select name="specialty" id="specialty" v-model="specialty" required>
                    <option value="none">Selecione a especialidade</option>
                    <option value="Pizza">Pizza</option>
                    <option value="Hamburguer">Hamburguer</option>
                    <option value="Comida Japonesa">Comida Japonesa</option>
                    <option value="Comida Saudável">Comida Saudável</option>
                    <option value="Outro">Outro</option>
                </select>
                <template v-if="stepErros.specialty">
                    <p v-for="error in stepErros.specialty">{{ error }}</p>
                </template>
            </div>
            <div class="form-group" :class="specialty === 'Outro' ? '' : 'hidden'">
                <label for="other">Outro</label>
                <input type="text" id="other" v-model="other" name="other" placeholder="Outro" />
                <template v-if="stepErros.other">
                    <p v-for="error in stepErros.other">{{ error }}</p>
                </template>
            </div>
        </div>
        <h3>Funcionamento</h3>
        <div class="mid">
            <div class="form-group">
                <label for="opening">Abertura</label>
                <input type="time" id="opening" v-model="opening" name="opening" required />
                <template v-if="stepErros.opening">
                    <p v-for="error in stepErros.opening">{{ error }}</p>
                </template>
            </div>
            <div class="form-group ">
                <label for="closing">Fechamento</label>
                <input type="time" id="closing" v-model="closing" name="closing" required />
                <template v-if="stepErros.closing">
                    <p v-for="error in stepErros.closing">{{ error }}</p>
                </template>
            </div>
        </div>
        <div class="form-group">
            <label for="days">Dias de funcionamento</label>
            <div class="mid-check">
                <div class="checkform" v-for="day in days">
                    <input type="checkbox" id="dayselect" name="dayselect" v-model="daysSelected" :value="day.value" />
                    <label for="sunday">{{ day.name }}</label>
                </div>
            </div>
            <template v-if="stepErros.daysSelected">
                <p v-for="error in stepErros.daysSelected">{{ error }}</p>
            </template>
        </div>

        <button type="submit" class="btn-text" :class="stepCompleted ? '' : 'disable'" :disabled="!stepCompleted"
            @click="nextStep">Próximo</button>
    </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref, watch } from 'vue';
import { MaskInput } from 'vue-3-mask';

const emit = defineEmits(['nextStep']);
const props = defineProps(['currentData']);



const cnpj = ref('');
const nameStore = ref('');
const phone = ref('');
const specialty = ref('');
const other = ref('');
const opening = ref('');
const closing = ref('');
const daysSelected = ref([]);

onMounted(() => {
    if (props.currentData) {
        cnpj.value = props.currentData.cnpj || '';
        nameStore.value = props.currentData.nameStore || '';
        phone.value = props.currentData.phone || '';
        specialty.value = props.currentData.specialty || '';
        other.value = props.currentData.other || '';
        opening.value = props.currentData.opening || '';
        closing.value = props.currentData.closing || '';
        daysSelected.value = props.currentData.daysSelected || [];
    }
});

const days = [
    { name: 'Domingo', value: 'Segunda-feira' },
    { name: 'Segunda-feira', value: 'Terça-feira' },
    { name: 'Terça-feira', value: 'Quarta-feira' },
    { name: 'Quarta-feira', value: 'Quinta-feira' },
    { name: 'Quinta-feira', value: 'Sexta-feira' },
    { name: 'Sexta-feira', value: 'Sábado' },
    { name: 'Sábado', value: 'Domingo' },
];

const stepCompleted = ref(false);

const stepErros = ref({
    cnpj: [],
    nameStore: [],
    phone: [],
    specialty: [],
    opening: [],
    closing: [],
    daysSelected: [],
    other: [],
});


watch([cnpj, nameStore, phone, specialty, opening, closing, daysSelected, other], async () => {

    if (cnpj.value) {
        let cnpjValue = cnpj.value.replace(/\D/g, '');
        if (cnpjValue.length !== 14) {
            stepErros.value.cnpj = ['** Digite um valor válido **'];
        } else {
            try {
                const response = await axios.post(`${import.meta.env.VITE_API_URL}auth/validation/restaurant/cnpj`, {
                    cnpj: cnpjValue,
                });

                if (response.status === 200) {
                    stepErros.value.cnpj = false;
                }
            } catch (error) {
                stepErros.value.cnpj = [`** ${error.response.data.errors.cnpj[0]} **`];
            }
        }
    }


    if (nameStore.value) {
        if (nameStore.value.length < 3) {
            stepErros.value.nameStore = ['** Digite um nome valido, minimo de 3 letras **'];
        } else {
            stepErros.value.nameStore = false;
        }
    }

    if (phone.value) {
        let phoneValue = phone.value.replace(/\D/g, '');
        if (phoneValue.length !== 10 && phoneValue.length !== 11) {
            stepErros.value.phone = ['** Digite um valor válido **'];
        } else {
            stepErros.value.phone = false;
        }
    }

    if (specialty.value === 'Outro' && !other.value) {
        stepErros.value.other = ['** Digite um valor valido **'];
    } else {
        stepErros.value.other = false;
    }

    if (specialty.value === 'none') {
        stepErros.value.specialty = ['** Selecione uma opção valida **'];
    } else {
        stepErros.value.specialty = false;
    }

    if (opening.value && opening.value.includes(':')) {
        let openingValue = opening.value.split(':');
        if (isNaN(parseInt(openingValue[0], 10)) || isNaN(parseInt(openingValue[1], 10))) {
            stepErros.value.opening = ['** Digite um horario valido **'];
        } else {
            stepErros.value.opening = false;
        }
    } else {
        stepErros.value.opening = ['** Digite um horario valido **'];
    }

    if (closing.value && closing.value.includes(':')) {
        let closingValue = closing.value.split(':');
        if (isNaN(parseInt(closingValue[0], 10)) || isNaN(parseInt(closingValue[1], 10))) {
            stepErros.value.closing = ['** Digite um horario valido **'];
        } else {
            stepErros.value.closing = false;
        }
    } else {
        stepErros.value.closing = ['** Digite um horario valido **'];
    }

    if (!stepErros.value.opening && !stepErros.value.closing) {
        let openingValue = opening.value.split(':');
        let closingValue = closing.value.split(':');
        if (parseInt(openingValue[0], 10) > parseInt(closingValue[0], 10)) {
            stepErros.value.opening = ['** O horario de abertura deve ser menor que o de fechamento **'];
            stepErros.value.closing = ['** O horario de fechamento deve ser maior que o de abertura **'];
        } else if (parseInt(openingValue[0], 10) === parseInt(closingValue[0], 10) && parseInt(openingValue[1], 10) >= parseInt(closingValue[1], 10)) {
            stepErros.value.opening = ['** O horario de abertura deve ser menor que o de fechamento **'];
            stepErros.value.closing = ['** O horario de fechamento deve ser maior que o de abertura **'];
        } else {
            stepErros.value.opening = false;
            stepErros.value.closing = false;
        }
    }

    if (daysSelected.value.length === 0) {
        stepErros.value.daysSelected = ['** Selecione pelo menos um dia **'];
    } else {
        stepErros.value.daysSelected = false;
    }

    if (Object.values(stepErros.value).some((value) => value)) {
        stepCompleted.value = false;
    } else {
        stepCompleted.value = true;
    }
});

function nextStep() {
    emit('nextStep', {
        cnpj: cnpj.value,
        nameStore: nameStore.value,
        phone: phone.value,
        specialty: specialty.value,
        other: other.value,
        opening: opening.value,
        closing: closing.value,
        daysSelected: daysSelected.value,
    });
}
</script>
