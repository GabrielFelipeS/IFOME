<template>
  <div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col md:p-5">
    <h2 class="text-2xl mt-2 ml-2 font-normal text-tertiary-light">Pratos</h2>

    <div class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:gap-5 mt-5">
      <template v-if="dishes.length > 0">
        <DishCard
            v-for="dish in limitedDishes" :key="dish.id"
            :dish="dish"
        />
      </template>

      <template v-else>
        <p class="col-span-full text-center">Nenhum restaurante encontrado</p>
      </template>
    </div>
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
    console.log(dishes.value);
  } catch (error) {
    console.error("Erro ao buscar os restaurantes:", error);
    dishes.value = [];
  }
});
</script>
