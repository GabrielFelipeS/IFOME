<template>
  <div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col md:p-5">
    <h2 class="text-2xl mt-2 ml-2 font-normal text-tertiary-light">Restaurantes</h2>

    <div class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:gap-5 mt-5 ">
      <template v-if="restaurants.length > 0">

        <CardRestaurant
            v-for="(restaurant, index) in limitedRestaurants"
            :key="restaurant.id || index"
            :restaurant="restaurant"
        />

      </template>

      <template v-else>
        <p class="col-span-full text-center">Nenhum restaurante encontrado</p>
      </template>
    </div>
    <router-link v-if="restaurants.length > 0" :to="{name : 'restaurants'}"
                 class="w-full h-[50px] bg-white border border-tertiary-light text-center rounded-md text-primary flex justify-center items-center mt-10 hover:rounded-md duration-150 hover:transition-all hover:-translate-y-1 hover:text-white hover:bg-primary hover:border-none">
      Ver todos
    </router-link>
  </div>
</template>

<script setup>
import CardRestaurant from '@/components/site/CardRestaurant.vue';
import {onMounted, ref, computed} from 'vue';
import axios from 'axios';

const restaurants = ref([]);
const limitedRestaurants = computed(() => restaurants.value.slice(0, 9));

onMounted(async () => {
  try {
    const {data} = await axios.get(`${import.meta.env.VITE_API_URL}restaurant/?size=9`);
    restaurants.value = data.data.content || [];
  } catch (error) {
    console.error("Erro ao buscar os restaurantes:", error);
    restaurants.value = [];
  }
});
</script>
