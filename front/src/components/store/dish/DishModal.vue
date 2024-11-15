<script setup>

import {ref} from "vue";
import { getImage } from "@/services/getImage.js";
import api from "@/services/api.js";
import {useCart} from "@/stores/cart.js";
import {useToast} from "vue-toast-notification";
import {formatReal} from "@/services/formatReal.js";

const emit = defineEmits(['close-dish-modal', 'add-to-cart']);
const props = defineProps({
	dish: {
		type: Object,
	},
	restaurant: {
		type: Object,
	}
})

const count = ref(1);
const details = ref('');
const plusCount = () => {if (count.value < 99)count.value++}
const minusCount = () => {if (count.value > 1)count.value--}

const cart = useCart();
const toast = useToast();

const addToCart = async () => {
	let payload = {
		dishId: props.dish.id,
		quantity: count.value,
		detail: details.value,
	};

	try {
		const response = await api.post('client/cart/dish/', JSON.stringify(payload));
		const order = response.data.data;
		cart.updateCart(order);
		const message = response.data.message;
		toast.success(message);
	} catch (error) {
		console.error('Error adding to cart:', error);
		if (error.status === 409) {
			toast.warning(error.response?.data?.message);
		} else {
			toast.error(error.response?.data?.message);
		}
	}
};

</script>

<template>
	<div class="main">
		<div class="img-container">
			<button class="close-btn" @click="emit('close-dish-modal')"><v-icon name="fa-times" scale="1.5" /></button>
			<img :src="getImage(dish.dishImage)" alt="Prato" class="image">
		</div>
		<div class="px-2 w-full
			md:flex md:flex-col md:max-h-[90%] md:justify-between md:py-3 md:pt-5 md:min-w-[120%] md:-translate-x-[17%]">
			<div class="dish-description">
				<span class="uppercase font-semibold">{{ dish.name }}</span>
				<span class="text-xs text-tertiary-light line-clamp-3 md:line-clamp-none">{{ dish.description }}</span>
				<span class="uppercase text-green-500 font-semibold">R$ {{ formatReal(dish.price) }}</span>
			</div>
			<div class="restaurant-description">
				<span class="font-semibold md:mb-2">{{ restaurant.nameRestaurant }} <v-icon name="fa-store"/></span>
				<hr>
				<span class="self-end md:w-full md:self-start md:mt-2">80 a 90 Minutos</span>
			</div>
			<div class="observations">
				<label>Observações:</label>
				<textarea class="border border-soft-border rounded-md h-24 resize-none text-xs px-1"
					v-model="details"
				></textarea>
			</div>
		</div>
		<div class="hidden md:flex"></div>
		<div class="button-container">
			<div class="btn-group">
				<button @click="plusCount"><v-icon name="fa-plus" /></button>
				<input class="mx-3 text-black text-center max-w-5 h-full" v-model="count" disabled>
				<button @click="minusCount"><v-icon name="fa-minus" /></button>
			</div>
			<button @click='addToCart' class="btn">Adicionar <v-icon name="fa-chevron-right"/></button>
		</div>
	</div>
</template>

<style scoped>
	.main {
		@apply flex flex-col justify-start mt-4 p-3 z-30;
		@apply min-h-screen w-full mt-10 border shadow rounded-xl bg-white;
		@apply fixed inset-0 mb-[91px] overflow-y-scroll;
		@apply scroll-m-0;

		@apply md:top-1/2 md:left-1/2 md:-translate-x-1/2 md:-translate-y-1/2;
		@apply md:min-h-fit md:max-h-[75%] md:max-w-[90%] md:h-fit;
		@apply md:grid md:grid-cols-2 md:shadow-2xl md:gap-y-0;
		@apply lg:max-w-[1000px] lg:overflow-hidden lg:max-h-fit;

		@apply animate-fade animate-duration-300 animate-ease-in-out;
		@apply no-scrollbar;
	}
	.img-container {
		@apply flex flex-col justify-start items-center align-baseline p-3 w-full min-h-[40%] max-h-[50%] aspect-square;
		@apply rounded-md;

		@apply md:flex-col md:justify-start md:w-[78%] md:max-h-[90%] md:pb-0;
	}
	.image {
		@apply rounded-md max-w-[80%] aspect-square h-auto object-fill min-w-full min-h-full;

		@apply md:mt-8;
	}
	.dish-description {
		@apply flex flex-col justify-between items-start align-baseline w-full gap-2;
	}
	.restaurant-description {
		@apply flex flex-row justify-between mt-4 p-4 rounded-md border border-soft-border;
		@apply text-tertiary-light text-xs;

		@apply md:flex-col md:p-3;
	}
	.observations {
		@apply flex flex-col mt-5 gap-2;
		@apply text-sm text-tertiary-light font-semibold;
	}
	.button-container {
		@apply flex flex-row justify-between items-end align-baseline w-full gap-2 px-[10%] h-full self-end mb-[112px];
		@apply mt-6;

		@apply md:m-0 md:items-center md:h-fit md:w-full md:justify-end md:gap-8;
	}
	.btn-group {
		@apply inline-flex border border-soft-border p-2 rounded-md text-primary;

		& button {
			@apply w-full h-full;
			@apply text-primary active:text-primary-dark;
			@apply transition duration-300 ease-in-out;
		}
	}
	.btn {
		@apply bg-primary p-2 px-4 rounded-lg text-white border border-primary;
		@apply active:bg-primary-dark active:border-primary-dark;
		@apply transition duration-300 ease-in-out;

		@apply md:px-10;
	}
	.close-btn {
		@apply self-end text-tertiary-light fixed bg-white rounded-3xl border border-tertiary-light p-0.5;
		@apply -translate-y-3 translate-x-3;

		@apply md:self-start md:flex md:bg-transparent md:border-none md:rounded-none md:translate-x-0 md:translate-y-0;
	}
</style>