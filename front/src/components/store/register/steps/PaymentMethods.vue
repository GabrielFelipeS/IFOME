<template>
    <div class="step">
        <h2>Informações de Pagamento</h2>
        <p>Preencha com os dados de pagamento</p>
        <h3>Metodos de pagamento aceito</h3>
        <div class="grid grid-cols-2 lg:grid-cols-3">
            <div class="checkform-payment">
                <input type="checkbox" id="money" name="money" value="dinner" v-model="paymentMethods" checked />
                <label for="money">Dinheiro</label>
                <img src="../../../../assets/img/store/money_icon.png" />
            </div>
            <div class="checkform-payment">
                <input type="checkbox" id="credit" name="credit" value="credit" v-model="paymentMethods" />
                <label for="credit">Debito/Crédito</label>
                <img src="../../../../assets/img/store/credit_card.png" />
            </div>
            <div class="checkform-payment">
                <input type="checkbox" id="debit" name="debit" value="pix" v-model="paymentMethods" />
                <label for="debit">Pix</label>
                <img src="../../../../assets/img/store/pix_icon.png" />
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
        <button type="submit" class="btn-text" :class="stepCompleted ? '' : 'disable'" :disabled="!stepCompleted"
            @click="nextStep">Próximo</button>

    </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { MaskInput } from 'vue-3-mask';

const props = defineProps(['currentData']);
const emit = defineEmits(['nextStep']);

const paymentMethods = ref([]);
const bank = ref('');
const agency = ref('');
const account = ref('');
const digit = ref('');

onMounted(() => {
    if (props.currentData) {
        paymentMethods.value = props.currentData.paymentMethods || [];
        bank.value = props.currentData.bank || '';
        agency.value = props.currentData.agency || '';
        account.value = props.currentData.account || '';
        digit.value = props.currentData.digit || '';
    }
});

const stepCompleted = ref(false);

watch([paymentMethods, bank, agency, account, digit], () => {
    if (paymentMethods.value.length > 0 && bank.value && agency.value && account.value && digit.value) {
        stepCompleted.value = true;
    } else {
        stepCompleted.value = false;
    }
});

function nextStep() {
    emit('nextStep', { paymentMethods: paymentMethods.value, bank: bank.value, agency: agency.value, account: account.value, digit: digit.value });
}

</script>