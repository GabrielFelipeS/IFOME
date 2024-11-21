<template>
  <Header />
  <div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col md:p-5 mt-0 md:mt-[75px]">
    <h2 class="text-2xl mt-2 ml-2 font-normal text-tertiary-light">Pratos</h2>

    <div v-if="Object.keys(dishesByCategory).length > 0">
      <div v-for="(categoryDishes, category) in dishesByCategory" :key="category" class="mt-8">
        <h3 class="text-xl font-semibold text-primary">{{ category }}</h3>
        <div class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:gap-5 mt-4">

          <router-link :to="{name:'restaurant internal', params: {id: dish.restaurantId }, query: { dish: dish.id }}" v-for="dish in categoryDishes" :key="$route.fullPath">
            <DishCard
                :key="dish.id"
                :dish="dish"
            />
          </router-link>
        </div>
      </div>
    </div>

    <div v-else>
      <p class="text-center">Nenhum prato encontrado</p>
    </div>
  </div>
  <FooterMobile />
</template>


<script setup>
import { computed, onMounted, ref } from 'vue';
import axios from 'axios';
import DishCard from "@/components/store/dish/DishCard.vue";
import Header from "@/components/site/Header.vue";
import FooterMobile from "@/components/site/FooterMobile.vue";

const dishes = ref([]);

const dishesByCategory = computed(() => {
  return dishes.value.reduce((acc, dish) => {
    const category = dish.dishCategory || 'Outros';
    if (!acc[category]) {
      acc[category] = [];
    }
    acc[category].push(dish);
    return acc;
  }, {});
});

onMounted(async () => {
  try {
    const { data } = await axios.get(`${import.meta.env.VITE_API_URL}dish/all`);
    dishes.value = data.data || [];
  } catch (error) {
    console.error("Erro ao buscar os pratos:", error);
    dishes.value = [];
  }
});
</script>

