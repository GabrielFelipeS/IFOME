<template>
    <div class="steps" v-if="stepsActive">
        <HeaderSteps :currentStep="currentStep" @returnBack="returnSteps" />

        <component :is="components[currentStep]" :currentData="currentData" @nextStep="nextStep" @submitForm="submit" />
    </div>
</template>

<script setup>
import { ref, watch } from 'vue';

import axios from 'axios';
import Address from './steps/Address.vue';
import DeliveryManInformations from './steps/DeliveryManInformations.vue';
import PaymentInformations from './steps/PaymentInformations.vue';
import LoginInformations from './steps/LoginInformations.vue';
import Final from './steps/Final.vue';
import HeaderSteps from './HeaderSteps.vue';

const emit = defineEmits(['responseApi']);

const props = defineProps({
    data: Object,
});

const stepsActive = ref(false);
const currentStep = ref(1);

const currentData = ref({ ...props.data });

watch(() => props.data, (value) => {
    if (Object.keys(value).length > 0) {
        stepsActive.value = true;
        currentData.value = { ...value };
    }
});

const components = {
    1: Address,
    2: DeliveryManInformations,
    3: PaymentInformations,
    4: LoginInformations,
    5: Final
};

function returnSteps() {
    if (currentStep.value > 1) {
        currentStep.value--;
    }
}

function nextStep(data) {

    currentData.value = { ...currentData.value, ...data };

    if (currentStep.value < 6) {
        currentStep.value++;
    }
}

async function submit(data) {
    try {

        currentData.value = { ...currentData.value, ...data };
        let formatedAddress = [currentData.value.address];


        const bankAccount = {
            bank: currentData.value.bank,
            agency: currentData.value.agency,
            account: `${currentData.value.account}-${currentData.value.digit}`
        };


        const dataToSend = {
            name: currentData.value.name,
            cpf: currentData.value.cpf,
            cnhNumber: currentData.value.cnh,
            cnhValidity: currentData.value.expirationDate,
            typeOfVehicle: currentData.value.vehicle,
            plate: currentData.value.plate,
            vehicleDocument: currentData.value.renavam,
            address: formatedAddress,
            bankAccount: bankAccount,
            email: currentData.value.email,
            telephone: currentData.value.phone,
            dateOfBirth: currentData.value.dateOfBirth,
            password: currentData.value.password,
            confirmationPassword: currentData.value.confirmPassword
        };

        const response = await axios.post(`${import.meta.env.VITE_API_URL}auth/deliveryPerson`, JSON.stringify(dataToSend), {
            headers: {
                'Content-Type': 'application/json',
            },
        });


        if (response.status === 201) {
            emit('responseApi', { type: "success", message: "Cadastro realizado com sucesso" });
            nextStep();
        } else if (response.status === 400) {
            emit('responseApi', { type: "error", message: response.data.message, errors: response.data.errors });
            stepCompleted.value = true;
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
        } else {
            emit('responseApi', { type: "error", message: "Erro de rede ou problema inesperado" });
        }
    }
}




</script>

<style lang="scss">
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
                @apply text-primary-dark;
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
                @apply relative w-[80%] md:w-[50%] h-[50px] bg-primary text-white font-semibold rounded-lg mb-3 text-center flex items-center justify-center;
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
