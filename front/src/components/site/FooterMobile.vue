<template>
	<div class="fixed bottom-[75px] w-full px-8 py-2 flex flex-row justify-between z-20 items-center md:hidden
			bg-white border-t border-b border-soft-border drop-shadow-2xl" v-if="!cartOpen && hasCart">
		<span class="text-sm font-semibold flex flex-row items-center gap-2">
			<img src="../../assets/img/logo_header_clean.png" alt="logo" class="max-h-8 object-contain" />
			R$ {{ formatReal(cart.totalPrice) }}
		</span>
		<button class="bg-primary text-white p-2 rounded-md text-sm" @click="emit('open-cart')">
			Ver sacola <v-icon name="fa-chevron-right" />
		</button>
	</div>
	<footer
		class="w-full h-[75px] bg-white flex flex-row items-center justify-around fixed bottom-0 left-0 shadow-black shadow-2xl md:hidden z-50">
		<router-link :to="{name: 'home-site'}"><v-icon name="fa-home" scale="1.5" class="text-primary cursor-pointer" /></router-link>
		<v-icon name="fa-shopping-bag" scale="1.5" class="text-primary cursor-pointer" @click="emit('open-cart')"/>
		<v-icon name="fa-receipt" scale="1.5" class="text-primary cursor-pointer" @click="rugoToOrders"/>
		<v-icon name="fa-sign-out-alt" scale="1.5" class="text-primary cursor-pointer" />
	</footer>
</template>

<script setup>
import {useCart} from "@/stores/cart.js";
import {onMounted} from "vue";
import router from "@/router";
import {formatReal} from "@/services/formatReal.js";

const cart = useCart();

const props = defineProps({
	cartOpen: {
		type: Boolean,
	}
});
const emit = defineEmits(['open-cart'])

const hasCart = (cart.order !== undefined && cart.order.orderItems && cart.order.orderItems.length > 0);

onMounted(() => {
	cart.updateCart();
})

const goToOrders = () => {
	router.push({name: 'orders'});
}
</script>