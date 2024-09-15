<script setup>
import { ref, watch } from 'vue';
import HeaderSteps from './HeaderSteps.vue';
import { MaskInput } from 'vue-3-mask';
import { fetchViaCep } from '@/services/viaCep';

const currentStep = ref(3);

function nextStep() {
    if (currentStep.value < 4) {
        currentStep.value++;
    }
}

function prevStep() {
    if (currentStep.value > 1) {
        currentStep.value--;
    }
}

const cep = ref('');
const state = ref('');
const city = ref('');
const address = ref('');
const number = ref('');
const complement = ref('');
const name = ref('');
const cpf = ref('');
const cnpj = ref('');
const nameStore = ref('');
const phone = ref('');
const specialty = ref('');
const other = ref('');
const opening = ref('');
const closing = ref('');
const daysSelected = ref([]);

watch(specialty, (value) => {
    if (value === 'Outro') {
        document.querySelector('#other').closest('.form-group').classList.remove('hidden');
    } else {
        document.querySelector('#other').closest('.form-group').classList.add('hidden');
    }
});

const stepCompleted = ref(false);
const step2Completed = ref(false);
const step3Completed = ref(false);

const step3Erros = ref({
    cnpj: false,
    nameStore: false,
    phone: false,
    specialty: false,
    opening: false,
    closing: false,
    daysSelected: false,
    other: false,
});

watch([cnpj,nameStore, phone, specialty, opening, closing, daysSelected, other], () => {

    if (cnpj.value) {
        let cnpjValue = cnpj.value.replace(/\D/g, '');
        if (cnpjValue.length !== 14) {
            step3Erros.value.cnpj = true;
        } else {
            step3Erros.value.cnpj = false;
        }
    }

    if (nameStore.value) {
        if (nameStore.value.length < 3) {
            step3Erros.value.nameStore = true;
        } else {
            step3Erros.value.nameStore = false;
        }
    }

    if (phone.value) {
        let phoneValue = phone.value.replace(/\D/g, '');
        if (phoneValue.length !== 10 && phoneValue.length !== 11) {
            step3Erros.value.phone = true;
        } else {
            step3Erros.value.phone = false;
        }
    }

    if (specialty.value === 'Outro' && !other.value) {
        step3Erros.value.other = true;
    } else {
        step3Erros.value.other = false;
    }

    if (specialty.value === 'none') {
        step3Erros.value.specialty = true;
    } else {
        step3Erros.value.specialty = false;
    }

    if (opening.value) {
        let openingValue = opening.value.split(':');
        if (openingValue[0] < 0 || openingValue[0] > 23 || openingValue[1] < 0 || openingValue[1] > 59) {
            step3Erros.value.opening = true;
        } else {
            step3Erros.value.opening = false;
        }
    }

    if (closing.value) {
        let closingValue = closing.value.split(':');
        if (closingValue[0] < 0 || closingValue[0] > 23 || closingValue[1] < 0 || closingValue[1] > 59) {
            step3Erros.value.closing = true;
        } else {
            step3Erros.value.closing = false;
        }
    }

    if (opening.value && closing.value) {
        let openingValue = opening.value.split(':');
        let closingValue = closing.value.split(':');
        if (closingValue[0] < openingValue[0] || (closingValue[0] === openingValue[0] && closingValue[1] <= openingValue[1])) {
            step3Erros.value.closing = true;
        } else {
            step3Erros.value.closing = false;
        }
    }

    if (daysSelected.value.length === 0) {
        step3Erros.value.daysSelected = true;
    } else {
        step3Erros.value.daysSelected = false;
    }  
    
    //verifica se tem algum erro

    if (Object.values(step3Erros.value).some((value) => value)) {
        step3Completed.value = false;
    } else {
        step3Completed.value = true;
    }
});

watch([name, cpf], () => {
    let cpfValue = cpf.value.replace(/\D/g, '');
    if (name.value && cpfValue.length === 11) {
        step2Completed.value = true;
    } else {
        step2Completed.value = false;
    }
});

watch([cep, state, city, address, number], () => {
    if (cep.value && state.value && city.value && address.value && number.value) {
        stepCompleted.value = true;
    } else {
        stepCompleted.value = false;
    }
});

watch(cep, async (value) => {
    if (value.length === 9) {
        let cep = value.replace('-', '');
        const data = await fetchViaCep(cep);
        state.value = data.uf;
        city.value = data.localidade;
        address.value = data.logradouro;
    }
});

const days = [
    { name: 'Domingo', value: 'sunday' },
    { name: 'Segunda-feira', value: 'monday' },
    { name: 'Terça-feira', value: 'tuesday' },
    { name: 'Quarta-feira', value: 'wednesday' },
    { name: 'Quinta-feira', value: 'thursday' },
    { name: 'Sexta-feira', value: 'friday' },
    { name: 'Sábado', value: 'saturday' },
];


</script>

<template>
    <div class="steps">
        <!-- Passar a etapa atual para o componente filho -->
        <HeaderSteps :currentStep="currentStep" />

        <div class="step" v-if="currentStep === 1">
            <h2>Endereço da loja</h2>
            <p>Preencha as informações de endereço da sua loja.</p>
            <div class="form-group">
                <label for="cep">CEP</label>
                <MaskInput type="text" id="cep" name="cep" v-model="cep" placeholder="CEP" mask="#####-###" required />
            </div>
            <div class="mid">
                <div class="form-group">
                    <label for="state">Estado</label>
                    <input type="text" id="state" name="state" v-model="state" placeholder="Estado" required disabled />
                </div>
                <div class="form-group">
                    <label for="city">Cidade</label>
                    <input type="text" id="city" name="city" v-model="city" placeholder="Cidade" required disabled />
                </div>
            </div>
            <div class="form-group">
                <label for="address">Endereço</label>
                <input type="text" id="address" name="address" v-model="address" placeholder="Endereço" required />
            </div>
            <div class="form-group">
                <label for="number">Número</label>
                <MaskInput type="text" id="number" name="number" v-model="number" placeholder="Número" mask="######"
                    required />
            </div>
            <div class="form-group">
                <label for="complement">Complemento</label>
                <input type="text" id="complement" name="complement" v-model="complement" placeholder="Complemento" />
            </div>
            <button type="submit" class="btn-primary" :class="stepCompleted ? '' : 'disable'"
                @click="nextStep">Próximo</button>
        </div>

        <div class="step" v-if="currentStep === 2">
            <h2>Responsável da loja</h2>
            <p>Informe os dados da pessoa que tem o nome no contrato social da empresa, seja como dona, sócia ou sócia
                administrativa.</p>
            <div class="form-group">
                <label for="name">Nome</label>
                <input type="text" id="name" name="name" v-model="name" placeholder="Nome" required />
            </div>
            <div class="form-group">
                <label for="cpf">CPF</label>
                <MaskInput type="text" id="cpf" v-model="cpf" name="cpf" placeholder="CPF" mask="###.###.###-##"
                    required />
            </div>
            <button type="submit" class="btn-primary" :class="step2Completed ? '' : 'disable'"
                @click="nextStep">Próximo</button>
        </div>

        <div class="step" v-if="currentStep === 3">
            <h2>Informações da loja</h2>
            <p>Preencha com os dados do seu negócio</p>
            <div class="form-group">
                <label for="cnpj">CNPJ</label>
                <MaskInput type="text" id="cnpj" v-model="cnpj" name="cnpj" placeholder="CNPJ" mask="##.###.###/####-##" required />
                <p v-if="step3Erros.cnpj">** Digite um valor válido **</p>
            </div>
            <div class="form-group">
                <label for="nameStore">Nome do Restaurante (como aparecerá no app)</label>
                <input type="text" id="nameStore" v-model="nameStore" name="nameStore" placeholder="Nome da loja" required />
                <p v-if="step3Erros.nameStore">** Digite um nome valido ( Minimo 3 letras ) **</p>
            </div>
            <div class="form-group">
                <label for="phone">Telefone do Restaurante</label>
                <MaskInput type="text" id="phone" v-model="phone" name="phone" placeholder="Telefone" mask="(##) #####-####" required />
                <p v-if="step3Erros.phone">** Digite um valor válido **</p>
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
                    <p v-if="step3Erros.specialty">** Selecione uma opção valida **</p>
                </div>
                <div class="form-group hidden">
                    <label for="other">Outro</label>
                    <input type="text" id="other" v-model="other" name="other" placeholder="Outro" />
                    <p v-if="step3Erros.other">** Digite um valor valido **</p>
                </div>
            </div>
            <h3>Funcionamento</h3>
            <div class="mid">
                <div class="form-group">
                    <label for="opening">Abertura</label>
                    <input type="time" id="opening" v-model="opening" name="opening" required />
                    <p v-if="step3Erros.opening">** Digite um horario valido **</p>
                </div>
                <div class="form-group ">
                    <label for="closing">Fechamento</label>
                    <input type="time" id="closing" v-model="closing" name="closing" required />
                    <p v-if="step3Erros.closing">** Digite um horario valido, e maior que a abertura**</p>
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
                <p v-if="step3Erros.daysSelected">** Selecione pelo menos um dia **</p>
            </div>

            <button type="submit" class="btn-text" @click="nextStep">Próximo</button>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.steps {
    @apply fixed top-0 left-0 w-full h-full bg-white overflow-auto;

    .step {
        @apply w-[90%] h-[calc(100vh-100px)] m-auto pt-10;

        h2 {
            @apply text-4xl font-semibold text-gray-800 mb-5;
        }

        h3 {
            @apply text-2xl font-semibold text-gray-800 mb-2;
        }

        p {
            @apply text-lg text-gray-500 mb-5 font-bold;
        }

        .mid-check {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-around;
            
            gap: 5px;

            .checkform {
                display: flex;
                align-items: center;
                gap: 5px;
                white-space: nowrap;
                font-weight: 300;

                input[type="checkbox"] {
                    @apply bg-black text-white;
                }
            }
        }

        .mid {
            @apply flex flex-row justify-between;

            .form-group {
                @apply w-[calc(50%-10px)];
            }

            input[type="checkbox"] {
                @apply hidden;
            }
        }

        .form-group {
            @apply mb-5 w-full;

            label {
                @apply text-lg font-semibold text-gray-800;
            }

            input {
                @apply w-full h-[50px] border border-gray-300 rounded-lg px-3;
            }

            select {
                @apply w-full h-[50px] border border-gray-300 rounded-lg px-3;
            }

        }

        .btn-text {
            @apply relative w-full h-[50px] bg-primary text-white font-semibold rounded-lg mb-3;
        }

        .btn-primary {
            @apply w-[90%] h-[50px] bg-primary text-white font-semibold rounded-lg fixed bottom-10 left-[50%] transform -translate-x-1/2;
        }

        @media (min-width: 768px) {
            @apply w-[60%] h-[calc(100vh-100px)] m-auto pt-10;

            .btn-primary {
                @apply w-[60%] h-[50px] bg-primary text-white font-semibold rounded-lg fixed bottom-10 left-[50%] transform -translate-x-1/2;
            }
        }

        .disable {
            @apply bg-gray-300 cursor-not-allowed;
        }

    }
}
</style>
