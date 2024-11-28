<template>
    <div class="w-[95%] h-[95%] m-auto border rounded flex items-center justify-center">
        <div class="w-[90%] h-[95%] max-h-[60vh] flex flex-col justify-between ">

            <div class="border-b grid grid-cols-3 gap-10 justify-between text-tertiary-subtle pb-2">
                <p>Qtd.</p>
                <p>Itens</p>
                <p>Preço</p>
            </div>

            <div class="flex flex-col py-4 max-h-[400px] overflow-auto">

                <template v-for="items in props.order.orderItems">
                    <div class="grid grid-cols-3 gap-10 justify-between text-tertiary-subtle mb-2 text-sm font-bold">
                        <p>{{ items.quantity }}</p>
                        <div>
                            <p>{{ items.dish.name }}</p>
                            <p class="font-thin"><span class="font-bold">Observação:</span> {{ items.detail }} </p>
                        </div>
                        <p>R$ {{ items.totalPrice }}</p>
                    </div>
                </template>
            </div>

            <div class="border-t pt-4 text-tertiary-subtle">
                <div class="flex flex-row justify-between mb-2">
                    <span>Subtotal</span>
                    <span>R$ {{ props.order.orderPrice }}</span>
                </div>
                <div class="flex flex-row justify-between mb-2">
                    <!-- TODO: Realizar calculo do valor da entrega e soma ao valor final -->
                    <span>Entrega</span>
                    <span>R$ --:--</span>
                </div>
                <div class="flex flex-row justify-between text-tertiary-subtle font-bold">
                    <span>Total</span>
                    <span>R$ {{ props.order.orderPrice }}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
const props = defineProps(['order']);
</script>