<template>
	<div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col md:p-5 mb-[100px] md:mb-0" v-if="dishes.length > 0">
		<h2 class="text-2xl mt-2 ml-2 font-normal text-tertiary-light">Recomendação de pratos</h2>

		<div class="w-full md:mb-0">
			<Swiper
				:modules="[Navigation, Pagination, FreeMode]"
				:slides-per-view="'auto'"
				:space-between="20"
				:effect="'fade'"
				:fade-effect="{ crossFade: true }"
				class="mySwiper pr-8 w-full">
				<SwiperSlide v-for="dish in limitedDishes" :key="$route.fullPath" class="my-4 w-full max-w-96">
					<router-link :to="{name:'restaurant internal', params: {id: dish.restaurantId }, query: { dish: dish.id }}">
						<DishCard
							:key="dish.id"
							:dish="dish"
						/>
					</router-link>
				</SwiperSlide>
			</Swiper>
		</div>
	</div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import DishCard from "@/components/store/dish/DishCard.vue";
import { Swiper, SwiperSlide } from 'swiper/vue';
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import {FreeMode, Navigation, Pagination} from 'swiper/modules';
import api from "@/services/api.js";

const dishes = ref([]);

const limitedDishes = computed(() => dishes.value.slice(0, 9));

onMounted(async () => {
	try {
		const {data} = await api.get('client/recommendations/dishes');
		dishes.value = data.data || [];
	} catch (error) {
		console.error("Erro ao buscar os restaurantes:", error);
		dishes.value = [];
	}
});
</script>
