<template>
	<div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col md:p-5 mt-12">
		<h2 class="text-2xl mt-2 ml-2 font-normal text-tertiary-light">Recomendação de restaurantes</h2>

		<div class="w-full md:gap-5 md:mb-0">
			<template v-if="restaurants.length > 0">
				<Swiper
					:modules="[Navigation, Pagination, FreeMode]"
					:slides-per-view="'auto'"
					:space-between="20"
					:effect="'fade'"
					:fade-effect="{ crossFade: true }"
					class="mySwiper w-full pr-8">
					<SwiperSlide v-for="(restaurant, index) in limitedRestaurants" :key="restaurant.id || index"
								 class="my-4 w-full max-w-80">
						<CardRestaurant
							:restaurant="restaurant"
						/>
					</SwiperSlide>
				</Swiper>
			</template>

			<template v-else>
				<p class="col-span-full text-center">Nenhum restaurante encontrado</p>
			</template>
		</div>
		<router-link v-if="restaurants.length > 0" :to="{name : 'restaurants'}"
					 class="w-full h-[50px] bg-white border border-tertiary-light
					 	text-center rounded-md text-primary flex justify-center items-center
					  	hover:rounded-md duration-150 hover:transition-all hover:-translate-y-1 hover:text-white
					  	hover:bg-primary hover:border-none">
			Ver todos
		</router-link>
	</div>
</template>

<script setup>
import CardRestaurant from '@/components/site/CardRestaurant.vue';
import {onMounted, ref, computed} from 'vue';
import { Swiper, SwiperSlide } from 'swiper/vue';
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import {Navigation, Pagination, FreeMode} from 'swiper/modules';
import api from "@/services/api.js";

const restaurants = ref([]);
const limitedRestaurants = computed(() => restaurants.value.slice(0, 9));

onMounted(async () => {
	try {
		const {data} = await api.get('client/recommendations/restaurants');
		restaurants.value = data.data || [];
	} catch (error) {
		console.error("Erro ao buscar os restaurantes:", error);
		restaurants.value = [];
	}
});

</script>