<template>
  <div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col md:p-5 mb-[100px] md:mb-0">
    <h2 class="text-2xl mt-2 ml-2 font-normal text-tertiary-light">Pratos</h2>

    <div class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:gap-5 mt-5 md:mb-0">
      <template v-if="dishes.length > 0">
        <router-link :to="{name:'restaurant internal', params: {id: dish.restaurantId }, query: { dish: dish.id }}" v-for="dish in limitedDishes" :key="$route.fullPath">
          <DishCard
               :key="dish.id"
              :dish="dish"
          />
        </router-link>
      </template>

      <template v-else>
        <p class="col-span-full text-center">Nenhum restaurante encontrado</p>
      </template>
    </div>
    <router-link v-if="dishes.length > 0" :to="{name : 'dishs'}"
                 class="w-full h-[50px] bg-white border border-tertiary-light text-center rounded-md text-primary flex justify-center items-center mt-5 md:mt-10 hover:rounded-md duration-150 hover:transition-all hover:-translate-y-1 hover:text-white hover:bg-primary hover:border-none">
      Ver todos
    </router-link>
  </div>
</template>

<script setup>
import CardRestaurant from '@/components/site/CardRestaurant.vue';
import {computed, onMounted, ref} from 'vue';
import axios from 'axios';
import DishCard from "@/components/store/dish/DishCard.vue";

const dishes = ref([]);

const limitedDishes = computed(() => dishes.value.slice(0, 9));

onMounted(async () => {
  try {
    const {data} = await axios.get(`${import.meta.env.VITE_API_URL}dish/all`);
    dishes.value = data.data || [];
  } catch (error) {
    console.error("Erro ao buscar os restaurantes:", error);
    dishes.value = [];
  }
});
</script>
