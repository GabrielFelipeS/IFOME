<script setup>
import { computed, ref, watch } from 'vue';
import HeaderSteps from './HeaderSteps.vue';
import { MaskInput } from 'vue-3-mask';
import { fetchViaCep } from '@/services/viaCep';
import axios from 'axios';

const currentStep = ref(1);

const emit = defineEmits(['responseApi']);

const stepsActive = ref(false);

const props = defineProps({
    data: Object,
});

const CurrentData = computed(() => {
    return props.data;
});

watch(CurrentData, (value) => {
    if (Object.keys(value).length > 0) {
        stepsActive.value = true;
        name.value = value.name;
        phone.value = value.phone;
        email.value = value.email;
    }
});

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

const email = ref('');
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
const neighborhood = ref('');
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
const password = ref('');
const confirmPassword = ref('');
const details = ref('');


watch(specialty, (value) => {
    if (value === 'Outro') {
        document.querySelector('#other').closest('.form-group').classList.remove('hidden');
    } else {
        document.querySelector('#other').closest('.form-group').classList.add('hidden');
    }
});

watch(other, (value) => {
    if (value) {
        document.querySelector('#specialty').closest('.form-group').classList.remove('hidden');
    } else {
        document.querySelector('#specialty').closest('.form-group').classList.add('hidden');
    }
});

const stepCompleted = ref(false);
const step2Completed = ref(false);
const step3Completed = ref(false);
const step4Completed = ref(false);
const step5Completed = ref(false);
const step6Completed = ref(false);

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

    if (opening.value && opening.value.includes(':')) {
        let openingValue = opening.value.split(':');
        let hour = parseInt(openingValue[0], 10);
        let minute = parseInt(openingValue[1], 10);

        if (isNaN(hour) || isNaN(minute) || hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            step3Erros.value.opening = true;
        } else {
            step3Erros.value.opening = false;
        }
    } else {
        step3Erros.value.opening = true;
    }

    // Verificação de Fechamento
    if (closing.value && closing.value.includes(':')) {
        let closingValue = closing.value.split(':');
        let hour = parseInt(closingValue[0], 10);
        let minute = parseInt(closingValue[1], 10);

        // Verificar se os valores são válidos
        if (isNaN(hour) || isNaN(minute) || hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            step3Erros.value.closing = true;
        } else {
            step3Erros.value.closing = false;
        }
    } else {
        step3Erros.value.closing = true;
    }

    if (!step3Erros.value.opening && !step3Erros.value.closing) {
        let openingValue = opening.value.split(':');
        let closingValue = closing.value.split(':');
        let openingHour = parseInt(openingValue[0], 10);
        let openingMinute = parseInt(openingValue[1], 10);
        let closingHour = parseInt(closingValue[0], 10);
        let closingMinute = parseInt(closingValue[1], 10);

        if (closingHour < openingHour || (closingHour === openingHour && closingMinute <= openingMinute)) {
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
        neighborhood.value = data.bairro;
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

const selectedFiles = ref([]);
const errorPhotos = ref(false);

function handleFileChange(event) {
    const files = Array.from(event.target.files);
    const file = files[0];

    if (file.size > 2 * 1024 * 1024) {
        errorPhotos.value = ['O arquivo deve ter no máximo 2MB'];
        selectedFiles.value = []; // Garantir que nenhum arquivo seja selecionado se o tamanho for maior que 2MB
    } else {
        errorPhotos.value = false;
        selectedFiles.value = [file]; // Aqui, garantimos que estamos modificando a variável 'selectedFiles' corretamente
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
    selectedFiles.value = [files[0]];
}

function removeFile() {
    selectedFiles.value = [];
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

const passwordErrors = ref({
    password: false,
    confirmPassword: false,
});

watch([password, confirmPassword], () => {
    if (password.value && password.value.length >= 8 && password.value.match(/[a-z]/) && password.value.match(/[A-Z]/) &&
        password.value.match(/[0-9]/)) {
        passwordErrors.value.password = false;
    } else {
        passwordErrors.value.password = true;
    }

    if (password.value && confirmPassword.value && password.value === confirmPassword.value && !passwordErrors.value.password) {
        passwordErrors.value.confirmPassword = false;
    } else {
        passwordErrors.value.confirmPassword = true;
    }

    if (Object.values(passwordErrors.value).some((value) => value)) {
        step6Completed.value = false;
    } else {
        step6Completed.value = true;
    }
});

function showPassword() {
    let input = document.querySelector('#password');
    let span = document.querySelector('#password + span');
    if (input.type === 'password') {
        input.type = 'text';
        span.textContent = 'Ocultar senha';
    } else {
        input.type = 'password';
        span.textContent = 'Mostrar senha';
    }
}

function showConfirmation() {
    let input = document.querySelector('#confirmPassword');
    let span = document.querySelector('#confirmPassword + span');
    if (input.type === 'password') {
        input.type = 'text';
        span.textContent = 'Ocultar senha';
    } else {
        input.type = 'password';
        span.textContent = 'Mostrar senha';
    }
}

async function submitForm() {
    try {
        const formData = new FormData();

        const bankAccount = {
            bank: bank.value,
            agency: agency.value,
            account: `${account.value}-${digit.value}`
        };

        const openingHours = daysSelected.value.map((day) => {
            return {
                dayOfTheWeek: day,
                opening: opening.value,
                closing: closing.value
            };
        });

        const restaurantData = {
            nameRestaurant: nameStore.value,
            email: email.value,
            password: password.value,
            confirmationPassword: confirmPassword.value,
            cnpj: cnpj.value,
            address: [
                {
                    nameAddress: "casa principal",
                    cep: cep.value,
                    typeResidence: "casa",
                    neighborhood: neighborhood.value,
                    city: city.value,
                    state: state.value,
                    address: address.value,
                    complement: complement.value,
                    number: number.value,
                    details: details.value
                }
            ],
            telephone: phone.value,
            foodCategory: specialty.value === 'Outro' ? other.value : specialty.value,
            paymentMethods: paymentMethods.value.toString(),
            openingHoursStart: opening.value,
            openingHoursEnd: closing.value,
            openingHours: openingHours,
            personResponsible: name.value,
            personResponsibleCPF: cpf.value,
            bankAccount: bankAccount
        };

        if (selectedFiles.value.length > 0) {
            formData.append('file', selectedFiles.value[0]);
        } else {
            throw new Error("Nenhum arquivo selecionado.");
        }


        formData.append('restaurant', new Blob([JSON.stringify(restaurantData)], {
            type: 'application/json',
        }));

        const response = await axios.post(`${import.meta.env.VITE_API_URL}auth/restaurant`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        });

        if (response.status === 201) {
            emit('responseApi', response.data);
            nextStep();
        } else {
            emit('responseApi', response.data);
        }

    } catch (error) {
        console.error(error);
        if (error.response) {
            emit('responseApi', error.response.data);
        } else {
            emit('responseApi', { message: error.message });
        }
    }
}

const returnSteps = () => {
    prevStep();
};

</script>

<template>
    <div class="steps" v-if="stepsActive">
        <HeaderSteps :currentStep="currentStep" @returnBack="returnSteps" />

        <div class="step" v-if="currentStep === 1">
            <h2>Endereço da loja</h2>
            <p>Preencha as informações de endereço da sua loja.</p>
            <div class="form-group">
                <label for="cep">CEP</label>
                <MaskInput type="text" id="cep" name="cep" v-model="cep" :value="cep" placeholder="CEP" mask="#####-###"
                    required />
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
                <label for="neighborhood">Bairro</label>
                <input type="text" id="neighborhood" name="neighborhood" v-model="neighborhood" placeholder="Bairro"
                    required />
            </div>
            <div class="form-group">
                <label for="address">Endereço</label>
                <input type="text" id="address" name="address" v-model="address" placeholder="Endereço" required />
            </div>
            <div class="form-group">
                <label for="number">Número</label>
                <MaskInput type="text" id="number" name="number" v-model="number" :value="number" placeholder="Número"
                    mask="######" required />
            </div>
            <div class="form-group">
                <label for="complement">Complemento</label>
                <input type="text" id="complement" name="complement" v-model="complement" placeholder="Complemento" />
            </div>
            <div class="form-group">
                <label for="details">Detalhes</label>
                <textarea id="details" name="details" v-model="details" placeholder="Detalhes"></textarea>
            </div>
            <button type="submit" class="btn-text" :class="stepCompleted ? '' : 'disable'" :disabled="!stepCompleted"
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
                <MaskInput type="text" id="cpf" v-model="cpf" name="cpf" :value="cpf" placeholder="CPF"
                    mask="###.###.###-##" required />
            </div>
            <button type="submit" class="btn-primary" :class="step2Completed ? '' : 'disable'"
                :disabled="!step2Completed" @click="nextStep">Próximo</button>
        </div>

        <div class="step" v-if="currentStep === 3">
            <h2>Informações da loja</h2>
            <p>Preencha com os dados do seu negócio</p>
            <div class="form-group">
                <label for="cnpj">CNPJ</label>
                <MaskInput type="text" id="cnpj" v-model="cnpj" name="cnpj" :value="cnpj" placeholder="CNPJ"
                    mask="##.###.###/####-##" required />
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
                <MaskInput type="text" id="phone" v-model="phone" :value="phone" name="phone" placeholder="Telefone"
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
                <div class="form-group" :class="specialty === 'Outro' ? '' : 'hidden'">
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

            <button type="submit" class="btn-text" :class="step3Completed ? '' : 'disable'" :disabled="!step3Completed"
                @click="nextStep">Próximo</button>
        </div>
        <div class="step" v-if="currentStep === 4">
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

            <!-- Lista de fotos selecionadas -->
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

            <button type="submit" class="btn-primary" :class="step4Completed ? '' : 'disable'"
                :disabled="!step4Completed" @click="nextStep">Próximo</button>
        </div>

        <div class="step" v-if="currentStep === 5">
            <h2>Informações de Pagamento</h2>
            <p>Preencha com os dados de pagamento</p>
            <h3>Metodos de pagamento aceito</h3>
            <div class="grid grid-cols-2 lg:grid-cols-3">
                <div class="checkform-payment">
                    <input type="checkbox" id="money" name="money" value="dinner" v-model="paymentMethods" checked />
                    <label for="money">Dinheiro</label>
                    <img src="../../assets/img/store/money_icon.png" />
                </div>
                <div class="checkform-payment">
                    <input type="checkbox" id="credit" name="credit" value="credit" v-model="paymentMethods" />
                    <label for="credit">Debito/Crédito</label>
                    <img src="../../assets/img/store/credit_card.png" />
                </div>
                <div class="checkform-payment">
                    <input type="checkbox" id="debit" name="debit" value="pix" v-model="paymentMethods" />
                    <label for="debit">Pix</label>
                    <img src="../../assets/img/store/pix_icon.png" />
                </div>
            </div>
            <h3>Recebimentos de fundos</h3>
            <div class="form-group">
                <label for="bank">Banco</label>
                <select name="bank" id="bank" v-model="bank" required>
                    <option value="">Selecione o banco</option>
                    <option value="001">Banco do Brasil</option>
                    <option value="033">Santander</option>
                    <option value="104">Caixa Econômica Federal</option>
                    <option value="237">Bradesco</option>
                    <option value="341">Itaú</option>
                    <option value="356">Nubank</option>
                    <option value="260">Nu Pagamentos</option>
                    <option value="212">Banco Original</option>
                    <option value="077">Banco Inter</option>
                    <option value="422">Banco Safra</option>
                    <option value="633">Banco Rendimento</option>
                    <option value="745">Banco Citibank</option>
                    <option value="399">HSBC Bank Brasil</option>
                    <option value="409">Unibanco</option>
                    <option value="041">Banco do Estado do Rio Grande do Sul</option>
                </select>
            </div>
            <div class="form-group">
                <label for="agency">Agência</label>
                <MaskInput type="text" id="agency" v-model="agency" name="agency" :value="agency" placeholder="Agência"
                    mask="####" required />
            </div>
            <div class="mid-payment">
                <div class="form-group">
                    <label for="account">Conta</label>
                    <MaskInput type="text" id="account" v-model="account" :value="account" name="account"
                        placeholder="Conta" mask="####" required />
                </div>
                <div class="form-group dig">
                    <label for="digit">Dígito</label>
                    <MaskInput type="text" id="digit" v-model="digit" name="digit" :value="digit" placeholder="Dígito"
                        mask="#" required />
                </div>
            </div>
            <button type="submit" class="btn-text" :class="step5Completed ? '' : 'disable'" :disabled="!step5Completed"
                @click="nextStep">Próximo</button>

        </div>
        <div class="step" v-if="currentStep === 6">
            <h2>Informações de Login</h2>
            <div class="form-group">
                <label for="password">Senha</label>
                <input type="password" id="password" name="password" placeholder="Senha" v-model="password" required />
                <span @click="showPassword" v-if="password.length > 0">Mostrar senha</span>
                <p v-if="passwordErrors.password">** A senha deve conter no mínimo 8 caracteres, uma letra maiúscula,
                    uma letra minúscula e um número **</p>
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirmar Senha</label>
                <input type="password" id="confirmPassword" name="confirmPassword" v-model="confirmPassword"
                    placeholder="Confirmar Senha" required />
                <span @click="showConfirmation" v-if="confirmPassword.length > 0">Mostrar Confirmação</span>
                <p v-if="passwordErrors.confirmPassword">** As senhas não conferem **</p>
            </div>
            <button type="submit" class="btn-primary" :class="step6Completed ? '' : 'disable'"
                :disabled="!step6Completed" @click="submitForm">Concluir</button>
        </div>

        <div class="step" v-if="currentStep === 7">
            <div class="final">
                <img src="../../assets/img/store/shop.png" alt="Logo Ifome">
                <h2>Seja Bem Vindo, {{ name }}</h2>
                <img src="../../assets/img/store/sushi.png" alt="Icone Sushi" class="baloon">
                <img src="../../assets/img/store/hamburguer-de-queijo.png" alt="Icone Hamburguer" class="baloon1">
                <img src="../../assets/img/store/plait.png" alt="Icone Prato" class="baloon2">
                <button type="submit" class="btn-text">Começar a Vender</button>
            </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
@keyframes baloon {
    0% {
        transform: translateY(0);
    }

    50% {
        transform: translateY(-10px);
    }

    100% {
        transform: translateY(0);
    }
}

.steps {
    @apply fixed top-0 left-0 w-full h-full bg-white overflow-auto;

    .step {
        @apply w-[90%] max-w-[800px] h-[calc(100vh-100px)] m-auto pt-10;

        h2 {
            @apply text-4xl font-semibold text-gray-800 mb-5 text-center;
        }

        h3 {
            @apply text-2xl font-semibold text-gray-800 mb-2;
        }

        p {
            @apply text-lg text-gray-500 mb-5 font-semibold;

            &.alert {
                @apply text-red-500;
            }
        }

        .checkform-payment {
            display: flex;
            align-items: center;
            gap: 5px;
            white-space: nowrap;
            font-weight: 300;
            @apply p-2;

            @apply font-thin text-xl;

            img {
                @apply w-[30px] h-[30px];
            }
        }


        .final {
            @apply flex flex-col items-center justify-start mt-36 h-full lg:mt-0 lg:justify-center;

            img {
                @apply w-[80%] md:w-[50%] lg:w-[40%] mb-5;
            }

            h2 {
                @apply text-4xl font-thin text-gray-800 mb-5;
            }

            .btn-text {
                @apply relative w-[80%] md:w-[50%] h-[50px] bg-primary text-white font-semibold rounded-lg mb-3 text-center;
            }

            .baloon {
                @apply w-[20%] md:w-[10%] lg:w-[5%] absolute top-[45%] right-[5%] md:right-[30%];
                animation: baloon 2s infinite;
            }

            .baloon1 {
                @apply w-[20%] md:w-[10%] lg:w-[5%] absolute top-[20%] left-[10%] md:left-[30%];
                animation: baloon 2s infinite;
            }

            .baloon2 {
                @apply w-[20%] md:w-[10%] lg:w-[5%] absolute bottom-[40%] left-[10%] md:left-[30%];
                animation: baloon 2s infinite;
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
                @apply text-red-500 font-light mt-1;
            }

            span {
                @apply text-blue-500 cursor-pointer mt-3 cursor-pointer;
            }

            textarea {
                @apply w-full h-[100px] border border-gray-300 rounded-lg px-3;
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
