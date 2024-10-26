<template>
    <div class="w-[98%] h-[18vh] m-auto bg-white p-4 rounded-lg shadow-md border">
        <!-- Cabeçalho do Histórico -->
        <div class="text-gray-700 font-semibold mb-4">Histórico</div>

        <!-- Linha de Status -->
        <div class="flex justify-around items-center">

            <!-- Status Solicitado -->
            <div class="flex flex-col items-center text-center">
                <v-icon name="fa-check-circle" class="w-8 h-8 text-primary" />
                <p class="text-gray-700">Solicitado</p>
                <span class="text-xs text-gray-500">19:30</span>
            </div>

            <!-- Status Em Preparo -->
            <div class="flex flex-col items-center text-center">
                <v-icon name="fa-check-circle" class="w-8 h-8 text-primary" />
                <p class="text-gray-700">Em preparo</p>
                <span class="text-xs text-gray-500">19:31</span>
            </div>

            <!-- Status Pronto -->
            <div class="flex flex-col items-center text-center">
                <v-icon name="fa-check-circle" class="w-8 h-8 text-tertiary-light" />
                <p class="text-gray-700">Pronto</p>
                <span class="text-xs text-gray-500">--:--</span>
            </div>

            <!-- Status Sair para Entrega -->
            <div class="flex flex-col items-center text-center">
                <v-icon name="fa-check-circle" class="w-8 h-8 text-tertiary-light" />
                <p class="text-gray-700">Saiu Para Entrega</p>
                <span class="text-xs text-gray-500">--:--</span>
            </div>

            <!-- Status Entregue -->
            <div class="flex flex-col items-center text-center">
                <v-icon name="fa-check-circle" class="w-8 h-8 text-tertiary-light" />
                <p class="text-gray-700">Entregue</p>
                <span class="text-xs text-gray-500">--:--</span>
            </div>

        </div>
    </div>
</template>