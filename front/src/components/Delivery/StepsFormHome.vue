<script setup>
import { computed, ref, watch } from 'vue';
import HeaderSteps from './HeaderSteps.vue';
import { MaskInput } from 'vue-3-mask';
import { fetchViaCep } from '@/services/viaCep';
import axios from 'axios';

const currentStep = ref(1);
const stepsActive = ref(false);

const emit = defineEmits(['responseApi']);

const name = ref('');
const phone = ref('');
const email = ref('');

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

const cep = ref('');
const state = ref('');
const city = ref('');
const address = ref('');
const number = ref('');
const complement = ref('');
const neighborhood = ref('');
const details = ref('');

const buttonFinal = ref('Concluir');

const cpf = ref('');
const cnh = ref('');
const expirationDate = ref('');
const vehicle = ref('none');
const plate = ref('');
const renavam = ref('');

const bank = ref('');
const agency = ref('');
const account = ref('');
const digit = ref('');

const password = ref('');
const confirmPassword = ref('');

const dateOfBirth = ref('');

//Step 1

const stepCompleted = ref(false);
watch([cep, state, city, address, number, complement], () => {
    if (cep.value && state.value && city.value && address.value && number.value && complement.value) {
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

        document.querySelector('#number').focus();
    }
});

//Step 2

const step2Completed = ref(false);
const step2Erros = ref({
    cpf: [],
    cnh: [],
    expirationDate: [],
    vehicle: [],
    plate: [],
    renavam: [],
    dateOfBirth: [],
});

watch([cpf, cnh, expirationDate, vehicle, plate, renavam, dateOfBirth], () => {
    if (expirationDate.value.length === 10) {
        let expirationDateFormated = new Date(expirationDate.value);
        let today = new Date();
        today.setMonth(today.getMonth() + 1);

        if (expirationDateFormated < today) {
            step2Erros.value.expirationDate = ['** Insira uma data válida **'];
        } else {
            let expirationDatePlus10 = new Date(today);
            expirationDatePlus10.setFullYear(today.getFullYear() + 10);

            if (expirationDateFormated > expirationDatePlus10) {
                step2Erros.value.expirationDate = ['** A validade da CNH deve ser de no máximo 10 anos **'];
            } else {
                step2Erros.value.expirationDate = false;
            }
        }
    } else {
        step2Erros.value.expirationDate = ['** Insira uma data válida **'];
    }

    if (dateOfBirth.value.length === 10) {
        let dateOfBirthFormated = new Date(dateOfBirth.value);
        let today = new Date();

        if (dateOfBirthFormated > today) {
            step2Erros.value.dateOfBirth = ['** Insira uma data válida **'];
        } else {
            step2Erros.value.dateOfBirth = false;
        }
        let dateOfBirthPlus18 = new Date(today);
        dateOfBirthPlus18.setFullYear(today.getFullYear() - 18);
        if (dateOfBirthFormated > dateOfBirthPlus18) {
            step2Erros.value.dateOfBirth = ['** O entregador deve ter no mínimo 18 anos **'];
        } else {
            step2Erros.value.dateOfBirth = false;
        }
    } else {
        step2Erros.value.dateOfBirth = ['** Insira uma data válida **'];
    }

    if (cnh.value.length >= 9 && cnh.value.length <= 11) {
        step2Erros.value.cnh = false;
    } else {
        step2Erros.value.cnh = ['** Insira um CNH válido (9 a 11 dígitos) **'];
    }

    async function validateCPF(cpf) {
        try {
            const response = await axios.post(
                'http://146.235.31.246/api/auth/validation/delivery/cpf',
                {
                    cpf: String(cpf),  // Certifica-se de que o CPF seja enviado como string
                },
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            );
            return true; 
        } catch (error) {
            return false;  
        }
    }

    if (cpf.value.length === 14) {
        step2Erros.value.cpf = false;
        if (!validateCPF(cpf.value)) {
            step2Erros.value.cpf = ['** CPF já cadastrado **'];
        }
    } else {
        step2Erros.value.cpf = ['** Insira um CPF válido **'];
    }

    if (plate.value.length === 8) {
        step2Erros.value.plate = false;
    } else {
        step2Erros.value.plate = ['** Insira uma placa válida **'];
    }

    if (renavam.value.length === 9) {
        step2Erros.value.renavam = false;
    } else {
        step2Erros.value.renavam = ['** Insira um Renavam válido **'];
    }

    if (vehicle.value === 'none') {
        step2Erros.value.vehicle = ['** Selecione um veículo **'];
    } else {
        step2Erros.value.vehicle = false;
    }

    if (
        cpf.value &&
        cnh.value &&
        expirationDate.value &&
        vehicle.value &&
        plate.value &&
        renavam.value &&
        !Object.values(step2Erros.value).some((value) => value)
    ) {
        step2Completed.value = true;
    } else {
        step2Completed.value = false;
    }
});

//Step 3

const step3Completed = ref(false);
const step3Erros = ref({
    bank: [],
    agency: [],
    account: [],
    digit: [],
});

watch([bank, agency, account, digit], () => {
    if (bank.value) {
        step3Erros.value.bank = false;
    } else {
        step3Erros.value.bank = ['** Selecione um banco **'];
    }

    if (agency.value.length === 4) {
        step3Erros.value.agency = false;
    } else {
        step3Erros.value.agency = ['** Insira uma agência válida **'];
    }

    if (account.value.length === 4) {
        step3Erros.value.account = false;
    } else {
        step3Erros.value.account = ['** Insira uma conta válida **'];
    }

    if (digit.value) {
        step3Erros.value.digit = false;
    } else {
        step3Erros.value.digit = ['** Insira um dígito **'];
    }


    if (!Object.values(step3Erros.value).some((value) => value)) {
        step3Completed.value = true;
    } else {
        step3Completed.value = false;
    }
});

//Step 4

const step4Completed = ref(false);
const passwordErrors = ref({
    password: [],
    confirmPassword: [],
});


watch([password, confirmPassword], () => {
    if (password.value && password.value.length >= 8 && password.value.match(/[a-z]/) && password.value.match(/[A-Z]/) && password.value.match(/[0-9]/)) {
        passwordErrors.value.password = false;
    } else {
        passwordErrors.value.password = ['** A senha deve conter no mínimo 8 caracteres, uma letra maiúscula, uma letra minúscula e um número **'];
    }

    if (password.value && confirmPassword.value && password.value === confirmPassword.value && !passwordErrors.value.password) {
        passwordErrors.value.confirmPassword = false;
    } else {
        passwordErrors.value.confirmPassword = ['** As senhas não conferem **'];
    }

    if (Object.values(passwordErrors.value).some((value) => value)) {
        step4Completed.value = false;
    } else {
        step4Completed.value = true;
    }
});


function showPassword() {
    let input = document.querySelector('#password');
    let span = document.querySelector('#password + span');
    if (input.type === 'password') {
        input.type = 'text';
        span.textContent = 'Ocultar Senha';
    } else {
        input.type = 'password';
        span.textContent = 'Mostrar Senha';
    }
}

function showConfirmation() {
    let input = document.querySelector('#confirmPassword');
    let span = document.querySelector('#confirmPassword + span');
    if (input.type === 'password') {
        input.type = 'text';
        span.textContent = 'Ocultar Confirmação';
    } else {
        input.type = 'password';
        span.textContent = 'Mostrar Confirmação';
    }
}

async function submitForm() {

    step4Completed.value = false;

    buttonFinal.value = 'Aguarde...';

    let formatedAddress = [{
        nameAddress: "residência",
        cep: cep.value,
        neighborhood: neighborhood.value,
        city: city.value,
        state: state.value,
        address: address.value,
        complement: complement.value,
        number: number.value,
        details: details.value,
        typeResidence: "casa"
    }];

    const bankAccount = {
        bank: bank.value,
        agency: agency.value,
        account: `${account.value}-${digit.value}`
    };

    const data = {
        name: name.value,
        cpf: cpf.value,
        cnhNumber: cnh.value,
        cnhValidity: expirationDate.value,
        typeOfVehicle: vehicle.value,
        plate: plate.value,
        vehicleDocument: renavam.value,
        address: formatedAddress,
        bankAccount: bankAccount,
        email: email.value,
        telephone: phone.value,
        dateOfBirth: dateOfBirth.value,
        password: password.value,
        confirmationPassword: confirmPassword.value
    };

    try {
        const response = await axios.post('http://146.235.31.246/api/auth/deliveryPerson', JSON.stringify(data), {
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.status === 201) {
            emit('responseApi', { type: "success", message: "Cadastro realizado com sucesso" });
            nextStep();
        } else if (response.status === 400) {
            emit('responseApi', { type: "error", message: response.data.message, errors: response.data.errors });
            step4Completed.value = true;
            buttonFinal.value = 'Concluir';
        }
    } catch (error) {
        if (error.response) {
            if (error.response.data.errors) {
                Object.keys(error.response.data.errors).forEach((key) => {
                    error.response.data.errors[key].forEach((message) => {
                        console.error(`${key}: ${message}`);
                    });
                });
            }
            emit('responseApi', {
                type: "error",
                message: error.response.data.message || "Erro ao cadastrar",
                errors: error.response.data.errors || {}
            });
            step4Completed.value = true;
            buttonFinal.value = 'Concluir';
        } else {
            emit('responseApi', { type: "error", message: "Erro de rede ou problema inesperado" });
            step4Completed.value = true;
            buttonFinal.value = 'Concluir';
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
            <h2>Endereço do Entregador</h2>
            <p>Preencha as informações de endereço</p>
            <div class="form-group">
                <label for="cep">CEP *</label>
                <MaskInput type="text" id="cep" v-model="cep" placeholder="CEP" mask="#####-###" required />
            </div>
            <div class="mid">
                <div class="form-group">
                    <label for="state">Estado *</label>
                    <input type="text" id="state" v-model="state" placeholder="Estado" required disabled />
                </div>
                <div class="form-group">
                    <label for="city">Cidade *</label>
                    <input type="text" id="city" v-model="city" placeholder="Cidade" required disabled />
                </div>
            </div>
            <div class="form-group">
                <label for="neighborhood">Bairro *</label>
                <input type="text" id="neighborhood" v-model="neighborhood" placeholder="Bairro" required />
            </div>
            <div class="form-group">
                <label for="address">Endereço *</label>
                <input type="text" id="address" v-model="address" placeholder="Endereço" required />
            </div>
            <div class="form-group">
                <label for="number">Número *</label>
                <MaskInput type="text" id="number" v-model="number" placeholder="Número" mask="######" required />
            </div>
            <div class="form-group">
                <label for="complement">Complemento *</label>
                <input type="text" id="complement" v-model="complement" placeholder="Complemento" />
            </div>
            <div class="form-group">
                <label for="details">Detalhes</label>
                <textarea id="details" v-model="details" placeholder="Detalhes"></textarea>
            </div>
            <button type="submit" class="btn-text" :class="stepCompleted ? '' : 'disable'" :disabled="!stepCompleted"
                @click="nextStep">Próximo</button>
        </div>

        <div class="step" v-if="currentStep === 2">
            <h2>Para a Plataforma</h2>
            <p>Informe Seus Dados e Do Veículo</p>
            <div class="mid">
                <div class="form-group">
                    <label for="cpf">CPF</label>
                    <MaskInput type="text" id="cpf" v-model="cpf" placeholder="CPF" mask="###.###.###-##" required />
                    <template v-if="step2Erros.cpf">
                        <p v-for="error in step2Erros.cpf">{{ error }}</p>
                    </template>
                </div>
                <div class="form-group">
                    <label for="dateOfBirth">Data de Nascimento</label>
                    <input type="date" id="dateOfBirth" v-model="dateOfBirth" placeholder="Data de Nascimento"
                        required />
                    <template v-if="step2Erros.dateOfBirth">
                        <p v-for="error in step2Erros.dateOfBirth">{{ error }}</p>
                    </template>
                </div>
            </div>
            <div class="mid">
                <div class="form-group">
                    <label for="cnh">CNH</label>
                    <MaskInput type="text" id="cnh" v-model="cnh" placeholder="CNH" mask="###########" required />
                    <template v-if="step2Erros.cnh">
                        <p v-for="error in step2Erros.cnh">{{ error }}</p>
                    </template>
                </div>
                <div class="form-group">
                    <label for="expirationDate">Validade</label>
                    <input type="date" id="expirationDate" v-model="expirationDate" placeholder="Validade" required />
                    <template v-if="step2Erros.expirationDate">
                        <p v-for="error in step2Erros.expirationDate">{{ error }}</p>
                    </template>
                </div>
            </div>
            <div class="form-group">
                <label for="vehicle">Veículo</label>
                <select id="vehicle" v-model="vehicle" required>
                    <option value="none" disabled selected>Selecione o veículo</option>
                    <option value="Carro">Carro</option>
                    <option value="Moto">Moto</option>
                </select>
                <template v-if="step2Erros.vehicle">
                    <p v-for="error in step2Erros.vehicle">{{ error }}</p>
                </template>
            </div>

            <div class="mid">
                <div class="form-group">
                    <label for="plate">Placa</label>
                    <MaskInput type="text" id="plate" v-model="plate" placeholder="Placa" mask="AAA-#X##" required />
                    <template v-if="step2Erros.plate">
                        <p v-for="error in step2Erros.plate">{{ error }}</p>
                    </template>
                </div>
                <div class="form-group">
                    <label for="renavam">Renavam</label>
                    <MaskInput type="text" id="renavam" v-model="renavam" placeholder="Renavam" mask="###########"
                        required />
                    <template v-if="step2Erros.renavam">
                        <p v-for="error in step2Erros.renavam">{{ error }}</p>
                    </template>
                </div>
            </div>

            <button type="submit" class="btn-primary" :class="step2Completed ? '' : 'disable'"
                :disabled="!step2Completed" @click="nextStep">Próximo</button>
        </div>

        <div class="step" v-if="currentStep === 3">
            <h2>Informações de Pagamento</h2>
            <div class="form-group">
                <label for="bank">Banco</label>
                <select id="bank" v-model="bank" required>
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
                <template v-if="step3Erros.bank">
                    <p v-for="error in step3Erros.bank">{{ error }}</p>
                </template>
            </div>
            <div class="form-group">
                <label for="agency">Agência</label>
                <MaskInput type="text" id="agency" v-model="agency" placeholder="Agência" mask="####" required />
                <template v-if="step3Erros.agency">
                    <p v-for="error in step3Erros.agency">{{ error }}</p>
                </template>
            </div>
            <div class="mid-payment">
                <div class="form-group">
                    <label for="account">Conta</label>
                    <MaskInput type="text" id="account" v-model="account" placeholder="Conta" mask="####" required />
                    <template v-if="step3Erros.account">
                        <p v-for="error in step3Erros.account">{{ error }}</p>
                    </template>
                </div>
                <div class="form-group dig">
                    <label for="digit">Dígito</label>
                    <MaskInput type="text" id="digit" v-model="digit" placeholder="Dígito" mask="#" required />
                    <template v-if="step3Erros.digit">
                        <p v-for="error in step3Erros.digit">{{ error }}</p>
                    </template>
                </div>
            </div>
            <button type="submit" class="btn-text" :class="step3Completed ? '' : 'disable'" :disabled="!step3Completed"
                @click="nextStep">Próximo</button>
        </div>

        <div class="step" v-if="currentStep === 4">
            <h2>Informações de Login</h2>
            <div class="form-group">
                <label for="password">Senha</label>
                <input type="password" id="password" v-model="password" placeholder="Senha" required />
                <span @click="showPassword" v-if="password.length > 0">Mostrar senha</span>
                <template v-if="passwordErrors.password">
                    <p v-for="error in passwordErrors.password">{{ error }}</p>
                </template>
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirmar Senha</label>
                <input type="password" id="confirmPassword" v-model="confirmPassword" placeholder="Confirmar Senha"
                    required />
                <span @click="showConfirmation" v-if="confirmPassword.length > 0">Mostrar Confirmação</span>
                <template v-if="passwordErrors.confirmPassword">
                    <p v-for="error in passwordErrors.confirmPassword">{{ error }}</p>
                </template>
            </div>
            <button type="submit" class="btn-primary" :class="step4Completed ? '' : 'disable'"
                :disabled="!step4Completed" @click="submitForm">{{ buttonFinal }}</button>
        </div>

        <div class="step" v-if="currentStep === 5">
            <div class="final">
                <img src="../../assets/img/delivery/delivery_man.png" alt="Logo Ifome">
                <h2>Seja Bem-Vindo, {{ name }}</h2>
                <button type="submit" class="btn-text">Começar a Entregar</button>
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
        @apply w-[90%] h-[calc(100vh-100px)] m-auto pt-10;

        h2 {
            @apply text-4xl font-semibold text-gray-800 mb-5 text-center;
        }

        h3 {
            @apply text-2xl font-semibold text-gray-800 mb-2;
        }

        p {
            @apply text-lg text-gray-500 mb-5 font-semibold
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
        }

        .mid {
            @apply flex flex-row justify-between;

            .form-group {
                @apply w-[calc(50%-10px)];
            }
        }

        .form-group {
            @apply mb-5 w-full;

            label {
                @apply text-lg font-semibold text-gray-800;
            }

            input,
            select {
                @apply w-full h-[50px] border border-gray-300 rounded-lg px-3;
            }

            p {
                @apply text-red-500 font-light mt-1;
            }

            span {
                @apply text-blue-500 cursor-pointer mt-3;
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
