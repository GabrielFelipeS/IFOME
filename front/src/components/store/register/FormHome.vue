<script setup>
import { ref, watch } from 'vue';
import { MaskInput } from 'vue-3-mask';
import Alert from '../Page/Alert.vue';
import axios from 'axios';

const emit = defineEmits(['submit', 'responseApi']);

const phone = ref('');
const name = ref('');
const email = ref('');

const errors = ref({
    name: [],
    email: [],
    phone: []
});

// Função de validação
function validateForm() {
    errors.value.name = [];
    errors.value.email = [];
    errors.value.phone = [];

    if (!name.value) {
        errors.value.name = ["Campo obrigatório"];
    }

    if (!email.value) {
        if (!email.value) {
            errors.value.email = ["Campo obrigatório"];
        } else {
            const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
            if (!emailRegex.test(email.value)) {
                errors.value.email = ["E-mail inválido"];
            }
        }
    }

    if (!phone.value) {
        errors.value.phone = ["Campo obrigatório"];
    } else {
        const phoneRegex = /^\(\d{2}\)\s\d{5}-\d{4}$/;
        if (!phoneRegex.test(phone.value)) {
            errors.value.phone = ["Celular inválido"];
        }
    }

    return Object.values(errors.value).every((error) => error.length === 0);
}

async function submitForm() {

    let response = axios.post('http://146.235.31.246/api/auth/validation/restaurant/email', {
        email: email.value
    }).then(response => {
        if(response.status === 200) {
            if (validateForm()) {
                emit('submit', {
                    name: name.value,
                    email: email.value,
                    phone: phone.value
                });
            }
        }
    }).catch(error => {
        emit('responseApi', {type: 'error', errors: {email: ['E-mail já cadastrado']}});
    });
}
</script>

<template>
    <form class="form" @submit.prevent="submitForm" @responseApi="showAlerts">
        <h2>Cadastre seu negócio</h2>

        <div class="form-group">
            <label for="name">Nome Completo</label>
            <input type="text" v-model="name" id="name" name="name" placeholder="Seu Nome Completo" />
            <template v-if="errors.name.length">
                <p>{{ errors.name[0] }}</p>
            </template>
        </div>

        <div class="form-group">
            <label for="email">E-mail</label>
            <input type="email" v-model="email" id="email" name="email" placeholder="E-mail" />
            <template v-if="errors.email.length">
                <p>{{ errors.email[0] }}</p>
            </template>
        </div>

        <div class="form-group">
            <label for="phone">Celular</label>
            <MaskInput v-model="phone" mask="(##) #####-####" placeholder="Celular" />
            <template v-if="errors.phone.length">
                <p>{{ errors.phone[0] }}</p>
            </template>
        </div>

        <button type="submit">Cadastrar Agora</button>

        <p>Ao continuar, você concorda em receber comunicações do iFome. Confira nossa <a href="#">Declaração de
                Privacidade.</a></p>
    </form>
</template>

<style lang="scss" scoped>
.form {
    @apply w-full max-w-[800px] bg-white rounded-lg shadow-lg mb-[50px] flex flex-col justify-around p-5;

    h2 {
        @apply text-4xl font-semibold text-gray-800 mb-5;
    }

    .form-group {
        @apply mb-5 w-[95%] m-auto;

        label {
            @apply text-lg font-semibold text-gray-800;
        }

        input {
            @apply w-full h-[50px] border border-gray-300 rounded-lg px-3;
        }

        p {
            @apply text-red-500 text-sm mt-1;
        }
    }

    button {
        @apply w-[95%] h-[50px] m-auto bg-primary text-white font-semibold rounded-lg;
    }

    p {
        @apply text-sm text-gray-500;

        a {
            text-decoration: underline;
        }
    }

    @media (max-width: 768px) {
        @apply mr-0 mt-36 mb-8;
    }

    @media (min-width: 768px) {
        @apply w-full mr-[4.2%] max-w-[600px] mt-24;
    }
}
</style>
