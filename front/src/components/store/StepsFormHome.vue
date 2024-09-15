<script setup>
import { ref, watch } from 'vue';
import HeaderSteps from './HeaderSteps.vue';
import { MaskInput } from 'vue-3-mask';
import { fetchViaCep } from '@/services/viaCep';

const currentStep = ref(2);

function nextStep() {
    currentStep.value++;
}

function prevStep() {
    currentStep.value--;
}

const cep = ref('');
const state = ref('');
const city = ref('');
const address = ref('');
const number = ref('');
const complement = ref('');
const name = ref('');
const cpf = ref('');

const stepCompleted = ref(false);
const step2Completed = ref(false);

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

</script>

<template>
    <div class="steps">
        <HeaderSteps />
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
                <MaskInput type="text" id="cpf" v-model="cpf" name="cpf" placeholder="CPF" mask="###.###.###-##" required />
            </div>
            <button type="submit" class="btn-primary" :class="step2Completed ? '' : 'disable'"
                @click="nextStep">Próximo</button>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.steps {
    @apply fixed top-0 left-0 w-full h-full bg-white;

    .step {
        @apply w-[90%] h-[calc(100vh-100px)] m-auto pt-10;

        h2 {
            @apply text-4xl font-semibold text-gray-800 mb-5;
        }

        p {
            @apply text-lg text-gray-500 mb-5;
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

            input {
                @apply w-full h-[50px] border border-gray-300 rounded-lg px-3;
            }
        }

        button {
            @apply w-[90%] h-[50px] bg-primary text-white font-semibold rounded-lg fixed bottom-10 left-[50%] transform -translate-x-1/2;
        }

        @media (min-width: 768px) {
            @apply w-[60%] h-[calc(100vh-100px)] m-auto pt-10;

            button {
                @apply w-[60%] h-[50px] bg-primary text-white font-semibold rounded-lg fixed bottom-10 left-[50%] transform -translate-x-1/2;
            }
        }

        .disable {
            @apply bg-gray-300 cursor-not-allowed;
        }

    }
}
</style>