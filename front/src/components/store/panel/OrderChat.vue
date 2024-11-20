<template>
    <div class="w-[95%] h-[95%] m-auto border rounded-lg flex flex-col">

        <!-- Cabeçalho do Chat -->
        <div class="border-b p-4 text-lg font-semibold text-gray-700">
            Chat
        </div>

        <!-- Área de Mensagens com Rolagem -->
        <div class="flex-grow overflow-y-auto p-4 space-y-3">

            <!-- Mensagem Enviada -->
            <div class="w-full flex justify-end flex-row-reverse">
                <div class="flex flex-row gap-2 bg-blue-100 p-3 rounded-lg shadow-sm">
                    <p class="text-gray-800">Poderia mandar talher?</p>
                    <span class="text-xs text-gray-500 self-end">19:00</span>
                </div>
            </div>

            <!-- Mensagem Recebida -->
            <div class="w-full flex justify-end">
                <div class="flex flex-row gap-2 bg-gray-100 p-3 rounded-lg shadow-sm">
                    <p class="text-gray-800">Claro que sim!</p>
                    <span class="text-xs text-gray-500 self-end">19:00</span>
                </div>
            </div>


        </div>

        <!-- Campo de Entrada de Mensagem -->
        <div class="border-t p-4 flex items-center">
            <input type="text" placeholder="Message"
                class="flex-grow border rounded-full p-3 px-4 text-gray-700 outline-none" />
            <button class="ml-3 text-red-500 hover:text-red-700">
                <!-- Ícone de Envio -->
                <v-icon name="fa-paper-plane" class="w-7 h-7 text-primary" />
            </button>
        </div>
    </div>
</template>