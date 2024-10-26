<template>
    <div class="w-[95%] h-[95%] m-auto border rounded flex items-center justify-center">
        <div class="w-[90%] h-[95%] flex flex-col justify-between">

            <!-- Cabeçalho da Tabela -->
            <div class="border-b grid grid-cols-3 gap-10 justify-between text-tertiary-subtle pb-2">
                <p>Qtd.</p>
                <p>Itens</p>
                <p>Preço</p>
            </div>

            <!-- Conteúdo da Tabela -->
            <div class="flex flex-col flex-grow py-4">
                <div class="grid grid-cols-3 gap-10 justify-between text-tertiary-subtle mb-2">
                    <p>1</p>
                    <p>Marmita M</p>
                    <p>R$ 35,00</p>
                </div>
                <!-- Adicione mais linhas conforme necessário -->
            </div>

            <!-- Totais e Resumo -->
            <div class="border-t pt-4 text-tertiary-subtle">
                <div class="flex flex-row justify-between mb-2">
                    <span>Subtotal</span>
                    <span>R$ 35,00</span>
                </div>
                <div class="flex flex-row justify-between mb-2">
                    <span>Entrega</span>
                    <span>R$ 8,00</span>
                </div>
                <div class="flex flex-row justify-between text-tertiary-subtle font-bold">
                    <span>Total</span>
                    <span>R$ 43,00</span>
                </div>
            </div>
        </div>
    </div>
</template>