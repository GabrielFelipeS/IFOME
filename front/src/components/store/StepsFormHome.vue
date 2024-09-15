<script setup>
import { ref, watch } from 'vue';
import HeaderSteps from './HeaderSteps.vue';
import { MaskInput } from 'vue-3-mask';
import { fetchViaCep } from '@/services/viaCep';

const currentStep = ref(5);

function nextStep() {
    if (currentStep.value < 7) {
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
const paymentMethods = ref([]);
const bank = ref('');
const agency = ref('');
const account = ref('');
const digit = ref('');

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
const step4Completed = ref(false);
const step5Completed = ref(false);

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

watch([cnpj, nameStore, phone, specialty, opening, closing, daysSelected, other], () => {

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

const selectedFiles = ref([]);

function handleFileChange(event) {
    const files = Array.from(event.target.files);
    selectedFiles.value = [...selectedFiles.value, ...files];
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
    selectedFiles.value = [...selectedFiles.value, ...files];
}

function removeFile(index) {
    selectedFiles.value = selectedFiles.value.filter((_, i) => i !== index);
}

const step4Erros = ref({
    photos: false,
});

watch(selectedFiles, () => {
    if (selectedFiles.value.length === 0) {
        step4Erros.value.photos = true;
    } else {
        step4Erros.value.photos = false;
    }

    if (step4Erros.value.photos) {
        step4Completed.value = false;
    } else {
        step4Completed.value = true;
    }
});

watch([paymentMethods, bank, agency, account, digit], () => {
    if (paymentMethods.value.length > 0 && bank.value && agency.value && account.value && digit.value) {
        step5Completed.value = true;
    } else {
        step5Completed.value = false;
    }
});

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
                <MaskInput type="text" id="cnpj" v-model="cnpj" name="cnpj" placeholder="CNPJ" mask="##.###.###/####-##"
                    required />
                <p v-if="step3Erros.cnpj">** Digite um valor válido **</p>
            </div>
            <div class="form-group">
                <label for="nameStore">Nome do Restaurante (como aparecerá no app)</label>
                <input type="text" id="nameStore" v-model="nameStore" name="nameStore" placeholder="Nome da loja"
                    required />
                <p v-if="step3Erros.nameStore">** Digite um nome valido ( Minimo 3 letras ) **</p>
            </div>
            <div class="form-group">
                <label for="phone">Telefone do Restaurante</label>
                <MaskInput type="text" id="phone" v-model="phone" name="phone" placeholder="Telefone"
                    mask="(##) #####-####" required />
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
                        <input type="checkbox" id="dayselect" name="dayselect" v-model="daysSelected"
                            :value="day.value" />
                        <label for="sunday">{{ day.name }}</label>
                    </div>
                </div>
                <p v-if="step3Erros.daysSelected">** Selecione pelo menos um dia **</p>
            </div>

            <button type="submit" class="btn-text" :class="step3Completed ? '' : 'disable'"
                @click="nextStep">Próximo</button>
        </div>
        <div class="step" v-if="currentStep === 4">
            <h2>Fotos da loja</h2>
            <p>Selecione as fotos do seu negócio</p>

            <!-- Área de upload de fotos -->
            <div class="border-2 border-dashed border-gray-300 rounded-lg p-5 text-center h-[300px] flex items-center justify-center"
                @dragover.prevent="dragOver" @dragleave.prevent="dragLeave" @drop.prevent="drop">
                <input type="file" id="photo" name="photo" multiple class="hidden" @change="handleFileChange" />
                <label for="photo" class="cursor-pointer flex flex-col items-center justify-center">
                    <svg data-name="Livello 1" id="Livello_1" viewBox="0 0 128 128" xmlns="http://www.w3.org/2000/svg"
                        width="30" height="30" class="mb-2">
                        <title />
                        <path
                            d="M37.09,32.91A3,3,0,0,0,39.21,32L61,10.24V91a3,3,0,0,0,6,0V10.24L88.79,32A3,3,0,0,0,93,27.79L66.12.88A3,3,0,0,0,65.66.5L65.43.38a3,3,0,0,0-.29-.15,3,3,0,0,0-.31-.1L64.59.06a3,3,0,0,0-1.18,0l-.25.08a2.93,2.93,0,0,0-.31.1,3,3,0,0,0-.29.15L62.34.5a3,3,0,0,0-.46.38L35,27.79a3,3,0,0,0,2.12,5.12Z" />
                        <path
                            d="M125,88a3,3,0,0,0-3,3v22a9,9,0,0,1-9,9H15a9,9,0,0,1-9-9V91a3,3,0,0,0-6,0v22a15,15,0,0,0,15,15h98a15,15,0,0,0,15-15V91A3,3,0,0,0,125,88Z" />
                    </svg>

                    <span>Arraste e solte suas fotos aqui</span>
                    <span class="text-sm text-gray-400">ou <span class="text-blue-500">clique aqui</span> e
                        selecione</span>
                </label>
            </div>
            <p v-if="step4Erros.photos">** Selecione pelo menos uma foto **</p>

            <!-- Lista de fotos selecionadas -->
            <div class="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
                <div v-for="(file, index) in selectedFiles" :key="index"
                    class="flex items-center justify-between border-gray-300 border-dashed border-2 rounded-3xl font-thin pl-5">
                    <span class="truncate">{{ file.name }}</span>
                    <button class="bg-red-500 text-white w-[15%] py-2 rounded-r-3xl" @click="removeFile(index)">
                        X
                    </button>
                </div>
            </div>

            <button type="submit" class="btn-primary" :class="step4Completed ? '' : 'disable'"
                @click="nextStep">Próximo</button>
        </div>

        <div class="step" v-if="currentStep === 5">
            <h2>Informações de Pagamento</h2>
            <p>Preencha com os dados de pagamento</p>
            <h3>Metodos de pagamento aceito</h3>
            <div class="grid grid-cols-2 lg:grid-cols-3">
                <div class="checkform-payment">
                    <input type="checkbox" id="money" name="money" value="dinner" v-model="paymentMethods" checked/>
                    <label for="money">Dinheiro</label>
                    <img src="../../assets/img/store/money_icon.png" />
                </div>
                <div class="checkform-payment">
                    <input type="checkbox" id="credit" name="credit" value="credit" v-model="paymentMethods" />
                    <label for="credit">Debito/Crédito</label>
                    <img src="../../assets/img/store/credit_card.png" />
                </div>
                <div class="checkform-payment">
                    <input type="checkbox" id="debit" name="debit" value="pix" v-model="paymentMethods"/>
                    <label for="debit">Pix</label>
                    <img src="../../assets/img/store/pix_icon.png" />
                </div>
            </div>
            <h3>Recebimentos de fundos</h3>
            <div class="form-group">
                <label for="bank">Banco</label>
                <input type="text" id="bank" v-model="bank" name="bank" placeholder="Banco" required />
            </div>
            <div class="form-group">
                <label for="agency">Agência</label>
                <MaskInput type="text" id="agency" v-model="agency" name="agency" placeholder="Agência" mask="####-#"
                    required />
            </div>
            <div class="mid-payment">
                <div class="form-group">
                    <label for="account">Conta</label>
                    <MaskInput type="text" id="account" v-model="account" name="account" placeholder="Conta"
                        mask="#####" required />
                </div>
                <div class="form-group dig">
                    <label for="digit">Dígito</label>
                    <MaskInput type="text" id="digit" v-model="digit" name="digit" placeholder="Dígito" mask="#" required />
                </div>
            </div>
            <button type="submit" class="btn-text" :class="step5Completed ? '' : 'disable'"
                @click="nextStep">Próximo</button>

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

        .checkform-payment{
            display: flex;
            align-items: center;
            gap: 5px;
            white-space: nowrap;
            font-weight: 300;
            @apply p-2;

            @apply font-thin text-xl;

            img{
                @apply w-[30px] h-[30px];
            }
        }

        .mid-payment {
            @apply flex flex-col md:flex-row justify-between;

            .form-group {
                @apply w-[calc(70%-10px)];

                &.dig {
                    @apply w-[30%];
                }

                @media (max-width: 768px) {
                    @apply w-full;

                    &.dig {
                        @apply w-full;
                    }
                }
            }

        }

        .mid-check {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
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

            p {
                @apply text-red-500;
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
