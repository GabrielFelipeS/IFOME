<script setup>
import { ref } from 'vue';
import { MaskInput } from 'vue-3-mask';

const emit = defineEmits(['submit']);

const phone = ref('');
const name = ref('');
const email = ref('');

const errors = ref({
    name: '',
    email: '',
    phone: ''
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

    if(errors.value.name || errors.value.email || errors.value.phone){
        return;
    }

    emit('submit', { name: name.value, email: email.value, phone: phone.value });

    document.querySelector('form').reset();
}
</script>
<template>
    <form class="form" @submit.prevent="submitForm">
        <h2>Cadastre seu negócio</h2>
        <div class="form-group">
            <label for="name">Nome Completo</label>
            <input type="text" v-model="name" id="name" name="name" placeholder="Seu Nome Completo" required/>
            <p v-if="errors.name">{{ errors.name.value }}</p>
        </div>
        <div class="form-group">
            <label for="email">E-mail</label>
            <input type="email" v-model="email" id="email" name="email" placeholder="E-mail" required/>
            <p v-if="errors.email">{{ errors.email.value }}</p>
        </div>

        <div class="form-group">
            <label for="phone">Celular</label>
            <MaskInput v-model="phone" mask="(##) #####-####" placeholder="Celular" required/>
            <p v-if="errors.phone">{{ errors.phone.value }}</p>
        </div>

        <button type="submit">Cadastrar Agora</button>

        <p>Ao continuar, você concorda em receber comunicações do iFome. Confira nossa <a href="#">Declaração de
                Privacidade.</a></p>
    </form>
</template>
<style lang="scss" scoped>
.form {
    @apply w-[95%] h-[60%] bg-white rounded-lg shadow-lg mb-[50px] flex flex-col justify-around p-5;

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

        p{
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

    @media (min-width: 768px) {
        @apply w-[500px] mb-[100px] mr-[100px] min-w-[500px];
    }
}
</style>