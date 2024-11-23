<template>
	<div class="fixed bottom-[75px] w-full px-8 py-2 flex flex-row justify-between z-20 items-center md:hidden
			bg-white border-t border-b border-soft-border drop-shadow-2xl" v-if="!cartOpen && hasCart">
		<span class="text-sm font-semibold flex flex-row items-center gap-2">
			<img src="../../assets/img/logo_header_clean.png" alt="logo" class="max-h-8 object-contain"/>
			R$ {{ formatReal(cart.totalPrice) }}
		</span>
		<button class="bg-primary text-white p-2 rounded-md text-sm" @click="emit('open-cart')">
			Ver sacola
			<v-icon name="fa-chevron-right"/>
		</button>
	</div>
	<footer
		class="w-full h-[75px] bg-white flex flex-row items-center justify-around fixed bottom-0 left-0 shadow-black shadow-2xl md:hidden z-50">
		<router-link :to="{name: 'home-site'}">
			<v-icon name="fa-home" scale="1.5" class="text-primary cursor-pointer"/>
		</router-link>
		<router-link :to="{name: 'restaurants'}">
			<v-icon name="fa-store" scale="1.5" class="text-primary cursor-pointer"/>
		</router-link>
		<router-link :to="{name: 'dishs'}">
			<v-icon name="md-fastfood-round" scale="1.4" class="text-primary cursor-pointer"/>
		</router-link>
		<router-link :to="{name: 'orders'}" v-if="isLogged">
			<v-icon name="fa-receipt" scale="1.5" class="text-primary cursor-pointer"/>
		</router-link>
		<v-icon name="fa-sign-out-alt" scale="1.5" class="text-primary cursor-pointer" @click="logout" v-if="isLogged"/>
		<router-link :to="{name: 'user-login'}" v-if="!isLogged">
			<v-icon name="fa-user-circle" scale="1.5" class="text-primary cursor-pointer"/>
		</router-link>
	</footer>
</template>

<script setup>
import {useCart} from "@/stores/cart.js";
import {onMounted, ref} from "vue";
import router from "@/router";
import {formatReal} from "@/services/formatReal.js";
import {useToast} from "vue-toast-notification";
import api from "@/services/api.js";
import {useRouter} from "vue-router";

const cart = useCart();

const props = defineProps({
	cartOpen: {
		type: Boolean,
	}
});
const emit = defineEmits(['open-cart'])

const hasCart = (cart.order !== undefined && cart.order.orderItems && cart.order.orderItems.length > 0);
const toast = useToast();
const routerFooter = useRouter();

onMounted(() => {
	cart.updateCart();
});

function logout() {
	localStorage.removeItem('token');
	toast.info('Fazendo logout...', {position: 'top-left'});
	routerFooter.push({name: 'home-site'});
	window.location.reload();
}

async function verifyLogin() {
	if (!localStorage.getItem('token')) {
		return false;
	}
	const response = await api.post('/auth/token/client/', {}, {
		headers: {
			Authorization: `Bearer ${localStorage.getItem('token')}`
		}
	});
	return response.status === 200;
}

const isLogged = ref(null);
verifyLogin().then((response) => {
	isLogged.value = response;
});

</script>