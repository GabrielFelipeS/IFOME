<script setup>
</script>
<template>
    <form class="form">
        <h2>Cadastre sua loja</h2>
        <div class="form-group">
            <label for="name">Nome Completo</label>
            <input type="text" id="name" name="name" placeholder="Nome da loja" />
        </div>
        <div class="form-group">
            <label for="email">E-mail</label>
            <input type="email" id="email" name="email" placeholder="E-mail" />
        </div>

        <div class="form-group">
            <label for="phone">Celular</label>
            <input type="text" id="phone" name="phone" placeholder="Celular" />
        </div>

        <button type="submit">Cadastrar Agora</button>

        <p>Ao continuar, você concorda em receber comunicações do iFome. Confira nossa <a href="#">Declaração de
                Privacidade.</a></p>
    </form>
</template>
<style lang="scss" scoped>
.form {
    @apply w-[80%] h-[60%] bg-white rounded-lg shadow-lg mb-[50px] flex flex-col justify-around p-5;

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