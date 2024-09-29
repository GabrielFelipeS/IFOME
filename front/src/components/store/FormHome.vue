<script setup>
import { ref, watch } from 'vue';
import { MaskInput } from 'vue-3-mask';
import Alert from '../Page/Alert.vue';

const emit = defineEmits(['submit']);

const phone = ref('');
const name = ref('');
const email = ref('');

const errors = ref({
    name: '',
    email: '',
    phone: ''
});

const errosApi = ref([]);

function showAlerts(response) {
    errosApi.value = response;
}

watch(() => errosApi.value, (newValue) => {
    if (newValue.length) {
        setTimeout(() => {
            errosApi.value = [];
        }, 5000);
    }
});

function submitForm() {
    if (!name.value) {
        errors.value.name = { value: 'Campo obrigatório' };
    } else {
        errors.value.name = '';
    }

    if (!email.value) {
        errors.value.email = { value: 'Campo obrigatório' };
    } else {
        errors.value.email = '';
    }

    if (!phone.value) {
        errors.value.phone = { value: 'Campo obrigatório' };
    } else {
        errors.value.phone = '';
    }

    if (errors.value.name || errors.value.email || errors.value.phone) {
        return;
    }

    emit('submit', { name: name.value, email: email.value, phone: phone.value });

    document.querySelector('form').reset();
}
</script>
<template>
    <div class="fixed top-[120px] right-5 flex flex-col z-50">
        <template v-for="error in errosApi">
            <Alert id="1" :type="error.type" :message="error.message" />
            <template v-if="error.erros">
                <template v-for="erro in error.erros">
                    <Alert id="1" :type="error.type" :message="erro.message" />
                </template>
            </template>
        </template>
    </div>
    <form class="form" @submit.prevent="submitForm" @responseApi="showAlerts">
        <h2>Cadastre seu negócio</h2>
        <div class="form-group">
            <label for="name">Nome Completo</label>
            <input type="text" v-model="name" id="name" name="name" placeholder="Seu Nome Completo" required />
            <p v-if="errors.name">{{ errors.name.value }}</p>
        </div>
        <div class="form-group">
            <label for="email">E-mail</label>
            <input type="email" v-model="email" id="email" name="email" placeholder="E-mail" required />
            <p v-if="errors.email">{{ errors.email.value }}</p>
        </div>

        <div class="form-group">
            <label for="phone">Celular</label>
            <MaskInput v-model="phone" mask="(##) #####-####" placeholder="Celular" required />
            <p v-if="errors.phone">{{ errors.phone.value }}</p>
        </div>

        <button type="submit">Cadastrar Agora</button>

        <p>Ao continuar, você concorda em receber comunicações do iFome. Confira nossa <a href="#">Declaração de
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