<script setup>
import {useCart} from "@/stores/cart.js";
import {ref} from "vue";

const cart = useCart();
const order = cart.order;
const deliveryFee = ref(0);

console.log(order)

</script>

<template>
	<div class="main">
		<div class="restaurant-description my-8">
			<div class="flex flex-col text-center gap-3">
				<span class="text-tertiary-light text-xs">Seu pedido em</span>
				<span class="font-semibold">Antojitos</span>
			</div>
			<button class="text-primary font-semibold">
				Ver cardápio
			</button>
		</div>
		<span class="font-semibold mt-8">Resumo do pedido:</span>
		<div class="order-items mb-8">
			<div class="product" v-for="item in order.orderItems">
				<div class="flex flex-col items-start gap-1.5">
					<span>{{ item.dish.name }}</span>
					<button class="text-primary text-xs font-semibold">Remover</button>
				</div>
				<div class="mr-6">
					R$ {{ item.totalPrice.toLocaleString('pt-BR', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2}) }}
				</div>
			</div>
			<div class="product" v-for="item in order.orderItems">
				<div class="flex flex-col items-start gap-1.5">
					<span>{{ item.dish.name }}</span>
					<button class="text-primary text-xs font-semibold">Remover</button>
				</div>
				<div class="mr-6">
					R$ {{ item.totalPrice.toLocaleString('pt-BR', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2}) }}
				</div>
			</div>
			<div class="product" v-for="item in order.orderItems">
				<div class="flex flex-col items-start gap-1.5">
					<span>{{ item.dish.name }}</span>
					<button class="text-primary text-xs font-semibold">Remover</button>
				</div>
				<div class="mr-6">
					R$ {{ item.totalPrice.toLocaleString('pt-BR', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2}) }}
				</div>
			</div>
		</div>
		<div class="order-summary my-8">
			<div class="flex flex-row justify-between text-xs text-tertiary-light">
				<span>Subtotal</span>
				<span>R$ {{ order.totalPrice.toLocaleString('pt-BR', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2}) }}</span>
			</div>
			<div class="flex flex-row justify-between text-xs text-tertiary-light">
				<span>Taxa de entrega</span>
				<span>R$ {{ deliveryFee.toLocaleString('pt-BR', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2}) }}</span>
			</div>
			<div class="flex flex-row justify-between font-semibold">
				<span>Total</span>
				<span>R$ {{ (order.totalPrice + deliveryFee).toLocaleString('pt-BR', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2}) }}</span>
			</div>
		</div>
		<div class="flex flex-row justify-center h-full my-8">
			<button class="p-2 bg-primary text-white/80 rounded-md h-fit self-end">
				Escolher formas de pagamento <v-icon name="fa-chevron-right" />
			</button>
		</div>
	</div>
</template>

<style scoped>
	.main {
		@apply bg-white fixed right-0 bottom-0 z-40 rounded-xl;
		@apply w-full max-w-[600px] h-full max-h-[95%] p-4 pb-[116px];
		@apply flex flex-col overflow-y-scroll no-scrollbar;
	}
	.restaurant-description {
		@apply flex flex-row justify-between mx-12 mt-6 max-h-[20%];
	}
	.order-items {
		@apply flex flex-col min-h-[40%] overflow-y-scroll;
	}
	.product {
		@apply flex flex-row justify-between mx-2 py-4 -mt-1;
		@apply border-t-4 border-b-4 border-soft-border;
	}
	.order-summary {
		@apply flex flex-col gap-4 mx-12 font-semibold max-h-[20%];
	}
</style>