<template>
	<div class="fixed bottom-[75px] w-full px-8 py-2 flex flex-row justify-between z-20 items-center md:hidden
			bg-white border-t border-b border-soft-border drop-shadow-2xl" v-if="!cartOpen && hasCart">
		<span class="text-sm font-semibold flex flex-row items-center gap-2">
			<img src="../../assets/img/logo_header_clean.png" alt="logo" class="max-h-8 object-contain" />
			R$ {{ cart.totalPrice.toLocaleString('pt-BR', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2}) }}
		</span>
		<button class="bg-primary text-white p-2 rounded-md text-sm" @click="emit('open-cart')">
			Ver sacola <v-icon name="fa-chevron-right" />
		</button>
	</div>
	<footer
		class="w-full h-[75px] bg-white flex flex-row items-center justify-around fixed bottom-0 left-0 shadow-black shadow-2xl md:hidden z-50">
		<v-icon name="fa-home" scale="1.5" class="text-primary cursor-pointer" />
		<v-icon name="fa-shopping-bag" scale="1.5" class="text-primary cursor-pointer" @click="emit('open-cart')"/>
		<v-icon name="fa-comment-alt" scale="1.5" class="text-primary cursor-pointer" />
		<v-icon name="fa-sign-out-alt" scale="1.5" class="text-primary cursor-pointer" />
	</footer>
</template>

<script setup>
import {useCart} from "@/stores/cart.js";
import {onMounted, ref} from "vue";

const cart = useCart();
const orderItems = ref(null);

orderItems.value = cart.order.orderItems;

const props = defineProps({
	cartOpen: {
		type: Boolean,
	}
});
const emit = defineEmits(['open-cart'])

const hasCart = (orderItems.value !== undefined);

onMounted(() => {
	cart.updateCart();
})
</script>