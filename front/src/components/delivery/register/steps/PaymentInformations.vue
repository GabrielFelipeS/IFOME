<template>
    <div class="step">
        <h2>Informações de Pagamento</h2>
        <div class="form-group">
            <label for="bank">Banco</label>
            <select id="bank" v-model="bank" :value="bank"required>
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
            <template v-if="stepErros.bank">
                <p v-for="error in stepErros.bank">{{ error }}</p>
            </template>
        </div>
        <div class="form-group">
            <label for="agency">Agência</label>
            <MaskInput type="text" id="agency" v-model="agency" :value="agency" placeholder="Agência" mask="####" required />
            <template v-if="stepErros.agency">
                <p v-for="error in stepErros.agency">{{ error }}</p>
            </template>
        </div>
        <div class="mid-payment">
            <div class="form-group">
                <label for="account">Conta</label>
                <MaskInput type="text" id="account" v-model="account" :value="account" placeholder="Conta" mask="####" required />
                <template v-if="stepErros.account">
                    <p v-for="error in stepErros.account">{{ error }}</p>
                </template>
            </div>
            <div class="form-group dig">
                <label for="digit">Dígito</label>
                <MaskInput type="text" id="digit" v-model="digit" :value="digit" placeholder="Dígito" mask="#" required />
                <template v-if="stepErros.digit">
                    <p v-for="error in stepErros.digit">{{ error }}</p>
                </template>
            </div>
        </div>
        <button type="submit" class="btn-text" :class="stepCompleted ? '' : 'disable'" :disabled="!stepCompleted"
            @click="nextStep">Próximo</button>
    </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { MaskInput } from 'vue-3-mask';

const emit = defineEmits(['nextStep']);
const props = defineProps(['currentData']);

const bank = ref('');
const agency = ref('');
const account = ref('');
const digit = ref('');

onMounted(() => {
    if (props.currentData) {
        bank.value = props.currentData.bank || '';
        agency.value = props.currentData.agency || '';
        account.value = props.currentData.account || '';
        digit.value = props.currentData.digit || '';
    }
});

const stepCompleted = ref(false);
const stepErros = ref({
    bank: [],
    agency: [],
    account: [],
    digit: [],
});

watch([bank, agency, account, digit], () => {
    if (bank.value) {
        stepErros.value.bank = false;
    } else {
        stepErros.value.bank = ['** Selecione um banco **'];
    }

    if (agency.value.length === 4) {
        stepErros.value.agency = false;
    } else {
        stepErros.value.agency = ['** Insira uma agência válida **'];
    }

    if (account.value.length === 4) {
        stepErros.value.account = false;
    } else {
        stepErros.value.account = ['** Insira uma conta válida **'];
    }

    if (digit.value) {
        stepErros.value.digit = false;
    } else {
        stepErros.value.digit = ['** Insira um dígito **'];
    }


    if (!Object.values(stepErros.value).some((value) => value)) {
        stepCompleted.value = true;
    } else {
        stepCompleted.value = false;
    }
});

function nextStep() {
    emit('nextStep', {
        bank: bank.value,
        agency: agency.value,
        account: account.value,
        digit: digit.value,
    });
}

</script>