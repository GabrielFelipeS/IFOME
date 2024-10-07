<template>
    <div class="step">
        <h2>Para a Plataforma</h2>
        <p>Informe Seus Dados e Do Veículo</p>
        <div class="mid">
            <div class="form-group">
                <label for="cpf">CPF</label>
                <MaskInput type="text" id="cpf" v-model="cpf" placeholder="CPF" :value="cpf" mask="###.###.###-##" required />
                <template v-if="stepErros.cpf">
                    <p v-for="error in stepErros.cpf">{{ error }}</p>
                </template>
            </div>
            <div class="form-group">
                <label for="dateOfBirth">Data de Nascimento</label>
                <input type="date" id="dateOfBirth" v-model="dateOfBirth" placeholder="Data de Nascimento" required />
                <template v-if="stepErros.dateOfBirth">
                    <p v-for="error in stepErros.dateOfBirth">{{ error }}</p>
                </template>
            </div>
        </div>
        <div class="mid">
            <div class="form-group">
                <label for="cnh">CNH</label>
                <MaskInput type="text" id="cnh" v-model="cnh" :value="cnh" placeholder="CNH" mask="###########" required />
                <template v-if="stepErros.cnh">
                    <p v-for="error in stepErros.cnh">{{ error }}</p>
                </template>
            </div>
            <div class="form-group">
                <label for="expirationDate">Validade</label>
                <input type="date" id="expirationDate" v-model="expirationDate" placeholder="Validade" required />
                <template v-if="stepErros.expirationDate">
                    <p v-for="error in stepErros.expirationDate">{{ error }}</p>
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
            <template v-if="stepErros.vehicle">
                <p v-for="error in stepErros.vehicle">{{ error }}</p>
            </template>
        </div>

        <div class="mid">
            <div class="form-group">
                <label for="plate">Placa</label>
                <MaskInput type="text" id="plate" v-model="plate" :value="plate" placeholder="Placa" mask="AAA-#X##" required />
                <template v-if="stepErros.plate">
                    <p v-for="error in stepErros.plate">{{ error }}</p>
                </template>
            </div>
            <div class="form-group">
                <label for="renavam">Renavam</label>
                <MaskInput type="text" id="renavam" v-model="renavam" :value="renavam" placeholder="Renavam" mask="###########"
                    required />
                <template v-if="stepErros.renavam">
                    <p v-for="error in stepErros.renavam">{{ error }}</p>
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

const stepCompleted = ref(false);
const stepErros = ref({
    cpf: false,
    cnh: false,
    expirationDate: false,
    vehicle: false,
    plate: false,
    renavam: false,
    dateOfBirth: false,
});

const cpf = ref('');
const cnh = ref('');
const expirationDate = ref('');
const vehicle = ref("Carro");
const plate = ref('');
const renavam = ref('');
const dateOfBirth = ref('');



if (props.currentData) {
    cpf.value = props.currentData.cpf || '';
    cnh.value = props.currentData.cnh || '';
    expirationDate.value = props.currentData.expirationDate || '';
    vehicle.value = props.currentData.vehicle || "Carro";
    plate.value = props.currentData.plate || '';
    renavam.value = props.currentData.renavam || '';
    dateOfBirth.value = props.currentData.dateOfBirth || '';
}

onMounted(()=>{
    if(!Object.values(stepErros.value).some((value) => value)){
        stepCompleted.value = true;
    } else {
        stepCompleted.value = false;
    }
})

watch([cpf, cnh, expirationDate, vehicle, plate, renavam, dateOfBirth], () => {
    if (expirationDate.value.length === 10) {
        let expirationDateFormated = new Date(expirationDate.value);
        let today = new Date();
        today.setMonth(today.getMonth() + 1);

        if (expirationDateFormated < today) {
            stepErros.value.expirationDate = ['** Insira uma data válida **'];
        } else {
            let expirationDatePlus10 = new Date(today);
            expirationDatePlus10.setFullYear(today.getFullYear() + 10);

            if (expirationDateFormated > expirationDatePlus10) {
                stepErros.value.expirationDate = ['** A validade da CNH deve ser de no máximo 10 anos **'];
            } else {
                stepErros.value.expirationDate = false;
            }
        }
    } else {
        stepErros.value.expirationDate = ['** Insira uma data válida **'];
    }

    if (dateOfBirth.value.length === 10) {
        let dateOfBirthFormated = new Date(dateOfBirth.value);
        let today = new Date();

        if (dateOfBirthFormated > today) {
            stepErros.value.dateOfBirth = ['** Insira uma data válida **'];
        } else {
            stepErros.value.dateOfBirth = false;
        }
        let dateOfBirthPlus18 = new Date(today);
        dateOfBirthPlus18.setFullYear(today.getFullYear() - 18);
        if (dateOfBirthFormated > dateOfBirthPlus18) {
            stepErros.value.dateOfBirth = ['** O entregador deve ter no mínimo 18 anos **'];
        } else {
            stepErros.value.dateOfBirth = false;
        }
    } else {
        stepErros.value.dateOfBirth = ['** Insira uma data válida **'];
    }

    if (cnh.value.length >= 9 && cnh.value.length <= 11) {
        stepErros.value.cnh = false;
    } else {
        stepErros.value.cnh = ['** Insira um CNH válido (9 a 11 dígitos) **'];
    }

    async function validateCPF(cpf) {
        try {
            const response = await axios.post(
                `${import.meta.env.VITE_API_URL}auth/validation/delivery/cpf`,
                {
                    cpf: String(cpf),
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
        stepErros.value.cpf = false;
        if (!validateCPF(cpf.value)) {
            stepErros.value.cpf = ['** CPF já cadastrado **'];
        }
    } else {
        stepErros.value.cpf = ['** Insira um CPF válido **'];
    }

    if (plate.value.length === 8) {
        stepErros.value.plate = false;
    } else {
        stepErros.value.plate = ['** Insira uma placa válida **'];
    }

    if (renavam.value.length === 9) {
        stepErros.value.renavam = false;
    } else {
        stepErros.value.renavam = ['** Insira um Renavam válido **'];
    }

    if (vehicle.value === 'none') {
        stepErros.value.vehicle = ['** Selecione um veículo **'];
    } else {
        stepErros.value.vehicle = false;
    }

    if(!Object.values(stepErros.value).some((value) => value)){
        stepCompleted.value = true;
    } else {
        stepCompleted.value = false;
    }
});

onMounted(() =>{
    console.log(stepErros);
})

function nextStep() {
    emit('nextStep', {
        cpf: cpf.value,
        cnh: cnh.value,
        expirationDate: expirationDate.value,
        vehicle: vehicle.value,
        plate: plate.value,
        renavam: renavam.value,
        dateOfBirth: dateOfBirth.value,
    });
}
</script>
