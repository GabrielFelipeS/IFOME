<script setup>
import HeaderSteps from './HeaderSteps.vue';
</script>

<template>
    <div class="steps">
        <HeaderSteps />
        <div class="step">
            <h2>Endereço da loja</h2>
            <p>Preencha as informações de endereço da sua loja.</p>
            <div class="form-group">
                <label for="cep">CEP</label>
                <input type="text" id="cep" name="cep" placeholder="CEP" required />
            </div>
            <div class="mid">
                <div class="form-group">
                    <label for="state">Estado</label>
                    <input type="text" id="state" name="state" placeholder="Estado" required />
                </div>
                <div class="form-group">
                    <label for="city">Cidade</label>
                    <input type="text" id="city" name="city" placeholder="Cidade" required />
                </div>
            </div>
            <div class="form-group">
                <label for="address">Endereço</label>
                <input type="text" id="address" name="address" placeholder="Endereço" required />
            </div>
            <div class="form-group">
                <label for="number">Número</label>
                <input type="text" id="number" name="number" placeholder="Número" required />
            </div>
            <div class="form-group">
                <label for="complement">Complemento</label>
                <input type="text" id="complement" name="complement" placeholder="Complemento" />
            </div>
            <button type="submit" class="btn-primary">Próximo</button>

        </div>
    </div>
</template>

<style lang="scss" scoped>
.steps {
    @apply fixed top-0 left-0 w-full h-full bg-white;

    .step {
        @apply w-[90%] h-[calc(100vh-100px)] m-auto pt-10;

        h2 {
            @apply text-4xl font-semibold text-gray-800 mb-5;
        }

        p {
            @apply text-lg text-gray-500 mb-5;
        }

        .mid {
            @apply flex flex-row justify-between;

            .form-group {
                @apply w-[calc(50%-10px)];
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
        }

        button {
            @apply w-[90%] h-[50px] bg-primary text-white font-semibold rounded-lg fixed bottom-10 left-[50%] transform -translate-x-1/2;
        }

    }
}
</style>