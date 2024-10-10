<template>
    <div class="steps" v-if="stepsActive">
        <HeaderSteps :currentStep="currentStep" @returnBack="returnSteps" />

        <component :is="components[currentStep]" :currentData="currentData" @nextStep="nextStep" @submitForm="submit" />
    </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import Address from './steps/Address.vue';
import Final from './steps/Final.vue';
import LoginInformations from './steps/LoginInformations.vue';
import PaymentMethods from './steps/PaymentMethods.vue';
import StoreInformations from './steps/StoreInformations.vue';
import Storephoto from './steps/Storephoto.vue';
import StoreManager from './steps/StoreManager.vue';
import HeaderSteps from './HeaderSteps.vue';
import axios from 'axios';

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
    2: StoreManager,
    3: StoreInformations,
    4: Storephoto,
    5: PaymentMethods,
    6: LoginInformations,
    7: Final
};

function returnSteps() {
    if (currentStep.value > 1) {
        currentStep.value--;
    }
}

function nextStep(data) {

    currentData.value = { ...currentData.value, ...data };

    if (currentStep.value <= 6) {
        currentStep.value++;
    }
}

async function submit(data) {
    try {
        currentData.value = { ...currentData.value, ...data };

        const formData = new FormData();

        const bankAccount = {
            bank: currentData.value.bank,
            agency: currentData.value.agency,
            account: `${currentData.value.account}-${currentData.value.digit}`
        };

        const openingHours = currentData.value.daysSelected.map((day) => {
            return {
                dayOfTheWeek: day,
                opening: currentData.value.opening,
                closing: currentData.value.closing
            };
        });

        const restaurantData = {
            nameRestaurant: currentData.value.nameStore,
            email: currentData.value.email,
            password: currentData.value.password,
            confirmationPassword: currentData.value.confirmPassword,
            cnpj: currentData.value.cnpj,
            address: [
                {
                    nameAddress: "casa principal",
                    cep: currentData.value.address.cep,
                    typeResidence: "casa",
                    neighborhood: currentData.value.address.neighborhood,
                    city: currentData.value.address.city,
                    state: currentData.value.address.state,
                    address: currentData.value.address.address,
                    complement: currentData.value.address.complement,
                    number: currentData.value.address.number,
                    details: currentData.value.address.details
                }
            ],
            telephone: currentData.value.phone,
            foodCategory: currentData.value.specialty === 'Outro' ? currentData.value.other : currentData.value.specialty,
            paymentMethods: currentData.value.paymentMethods.toString(),
            openingHoursStart: currentData.value.opening,
            openingHoursEnd: currentData.value.closing,
            openingHours: openingHours,
            personResponsible: currentData.value.name,
            personResponsibleCPF: currentData.value.cpf,
            bankAccount: bankAccount
        };

        if (currentData.value.photos && currentData.value.photos.length > 0) {
            formData.append('file', currentData.value.photos[0]);
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
            currentStep.value++;
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
