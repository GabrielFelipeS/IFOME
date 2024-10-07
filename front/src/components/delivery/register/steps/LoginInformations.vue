<template>
    <div class="step">
        <h2>Informações de Login</h2>
        <div class="form-group">
            <label for="password">Senha</label>
            <input type="password" id="password" name="password" placeholder="Senha" v-model="password" required />
            <span @click="showPassword" v-if="password.length > 0">Mostrar senha</span>
            <p v-if="passwordErrors.password">** A senha deve conter no mínimo 8 caracteres, uma letra maiúscula,
                uma letra minúscula e um número **</p>
        </div>
        <div class="form-group">
            <label for="confirmPassword">Confirmar Senha</label>
            <input type="password" id="confirmPassword" name="confirmPassword" v-model="confirmPassword"
                placeholder="Confirmar Senha" required />
            <span @click="showConfirmation" v-if="confirmPassword.length > 0">Mostrar Confirmação</span>
            <p v-if="passwordErrors.confirmPassword">** As senhas não conferem **</p>
        </div>
        <button type="submit" class="btn-text" :class="stepCompleted ? '' : 'disable'" :disabled="!stepCompleted"
            @click="submitForm">Concluir</button>
    </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';

const emit = defineEmits(['submitForm']);
const props = defineProps(['currentData']);

const password = ref('');
const confirmPassword = ref('');

onMounted(() => {
    if (props.currentData) {
        password.value = props.currentData.password || '';
        confirmPassword.value = props.currentData.confirmPassword || '';
    }
});

const passwordErrors = ref({
    password: false,
    confirmPassword: false,
});

const stepCompleted = ref(false);

watch([password, confirmPassword], () => {
    if (password.value && password.value.length >= 8 && password.value.match(/[a-z]/) && password.value.match(/[A-Z]/) &&
        password.value.match(/[0-9]/)) {
        passwordErrors.value.password = false;
    } else {
        passwordErrors.value.password = true;
    }

    if (password.value && confirmPassword.value && password.value === confirmPassword.value && !passwordErrors.value.password) {
        passwordErrors.value.confirmPassword = false;
    } else {
        passwordErrors.value.confirmPassword = true;
    }

    if (Object.values(passwordErrors.value).some((value) => value)) {
        stepCompleted.value = false;
    } else {
        stepCompleted.value = true;
    }
});

function showPassword() {
    let input = document.querySelector('#password');
    let span = document.querySelector('#password + span');
    if (input.type === 'password') {
        input.type = 'text';
        span.textContent = 'Ocultar senha';
    } else {
        input.type = 'password';
        span.textContent = 'Mostrar senha';
    }
}

function showConfirmation() {
    let input = document.querySelector('#confirmPassword');
    let span = document.querySelector('#confirmPassword + span');
    if (input.type === 'password') {
        input.type = 'text';
        span.textContent = 'Ocultar senha';
    } else {
        input.type = 'password';
        span.textContent = 'Mostrar senha';
    }
}

function submitForm() {
    emit('submitForm', { password: password.value, confirmPassword: confirmPassword.value });
}
</script>