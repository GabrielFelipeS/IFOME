<script setup>
import {useRoute} from 'vue-router';
import {ref, onMounted, watch, computed} from 'vue';
import router from '@/router';
import api from '@/services/api';
import {getImage} from "@/services/getImage.js";
import DishCard from "@/components/store/dish/DishCard.vue";
import CardReview from "@/components/site/CardReview.vue";

const route = useRoute();
const restaurantId = route.params.id;
const imageUrl = ref('');

const restaurant = ref(null);
const error = ref(null);

const dishes = ref([]);
const selectedDish = ref(null);

const cartOpen = ref(false);

const fetchRestaurantData = async () => {
	try {
		const response = await api.get(`restaurant/${restaurantId}`);
		if (response.data.data === null) {
			router.push({name: 'NotFound'});
			return;
		}
		restaurant.value = response.data.data;
		dishes.value = restaurant.value.dish.filter(dish => dish.availability === 'Disponível');
		imageUrl.value = getImage(restaurant.value.restaurantImage);
	} catch (err) {
		router.push({name: 'NotFound'});
		console.error(err);
	}
};
fetchRestaurantData().then(() => {
	console.log(restaurant);
});

const fetchDishes = async () => {
	try {
		const response = await api.get(`dish/${route.query.dish}`);
		selectedDish.value = response.data.data;
	} catch (err) {
		console.error(err);
	}
};

if (route.query.dish) {
	fetchDishes();
}

onMounted(() => {
	fetchRestaurantData();
});


const filteredReviews = computed(() => {
	return restaurant.value?.restaurantReview?.filter(review => review.comment.length > 0);
});
</script>

<template>
	<main class="w-full min-h-[calc(100vh-75px)] bg-white md:mt-[75px] md:max-w-[1200px] mx-auto">
		<div v-if="!restaurant" class="w-full h-[250px] bg-gray-300 animate-pulse"></div>

		<div v-else>
			<div
				class="w-full h-[250px] bg-[url('../assets/img/default.jpg')] bg-center bg-cover md:mt-[100px] relative z-10">
				<div class="md:hidden">
					<v-icon name="fa-chevron-left" scale="1.8"
							class="text-white absolute top-5 left-5 md:relative md:hidden shadow-black z-10 md:z-0"
							@click="router.push('/restaurants')"/>
				</div>
				<div class="absolute top-0 left-0 w-full h-full bg-black opacity-30"></div>
			</div>
			<div class="w-full flex flex-row md:flex-row items-center p-4 rounded-t-lg bg-white mt-[-45px] md:mt-5 relative z-20">
				<img :src="imageUrl || '../../assets/img/logo_header_clean.png'" alt="Logo do Restaurante"
					 class="w-20 h-20 object-cover rounded-full"/>

				<div class="ml-4 flex-1">
					<h3 class="text-lg font-semibold text-gray-800">{{ restaurant.nameRestaurant }} <span
						v-if="!restaurant.isOpen">( Fechado )</span></h3>
					<div class="flex items-center text-gray-600 text-sm mt-1">
						<span class="">{{ restaurant.foodCategory || 'Categoria' }}</span>
						<span class="mx-2">·</span>
						<span>1.5km</span>
						<span class="mx-2">·</span>
						<svg class="w-4 h-4 text-red-500 mr-1" fill="currentColor" viewBox="0 0 20 20"
							 xmlns="http://www.w3.org/2000/svg">
							<path
								d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.071 3.297a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.033a1 1 0 00-.364 1.118l1.071 3.297c.3.921-.755 1.688-1.54 1.118l-2.8-2.033a1 1 0 00-1.175 0l-2.8 2.033c-.785.57-1.84-.197-1.54-1.118l1.071-3.297a1 1 0 00-.364-1.118L2.98 8.724c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.071-3.297z"/>
						</svg>
						<span class="mr-2">{{ restaurant.rating?.toFixed(1) || 0 }}</span>
						<span class="font-semibold"><router-link :to="{name: 'restaurant internal', params: { id: restaurantId }}">Voltar a lista de pratos</router-link></span>
					</div>
					<div class="flex items-center text-sm text-gray-500 mt-1">
						<span>50-60 min</span>
						<span class="mx-2">·</span>
						<span>R$ 10,00</span>
					</div>
				</div>
			</div>
			<div class="border-t border-gray-300 my-4"></div>
			<span class="text-2xl flex flex-row justify-center md:justify-start p-4 pb-0 ">Avaliações do Restaurante</span>
			<div class="px-5 lg:grid grid-cols-3 gap-4 gap-y-0.5 mb-[91px]">
				<CardReview v-for="review in filteredReviews" :review="review"/>
			</div>
		</div>
	</main>
</template>

<style scoped>

</style>