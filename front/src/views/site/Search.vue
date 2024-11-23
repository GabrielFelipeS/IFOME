<template>
  <Restaurants v-if="restaurants" :restaurants="restaurants" />
  <Dishes v-if="dishes" :dishes="dishes" />

  <div v-if="!restaurants && !dishes" class="flex w-full h-screen items-center justify-center flex-col">
    <div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col items-center md:p-5 md:mt-[100px] text-center">
      <img :src="SearchPhoto" alt="Nenhum resultado encontrado" class="w-full max-w-[40%] mb-6" />
      <h2 class="text-2xl font-semibold text-tertiary-light mb-2">
        Nenhum resultado encontrado
      </h2>
      <p class="text-lg text-gray-500">
        Não encontramos nenhum item ou restaurante correspondente à sua busca.
      </p>
      <p class="text-lg text-gray-500">
        Tente pesquisar algo diferente ou explorar outras opções em nosso site.
      </p>
    </div>
  </div>
</template>

<script setup>
import Header from "@/components/site/Header.vue";
import { useRoute } from "vue-router";
import { onMounted, ref, watch } from "vue";
import api from "@/services/api";
import Restaurants from "@/components/site/Search/Restaurants.vue";
import Dishes from "@/components/site/Search/Dish.vue";
import SearchPhoto from "@/assets/img/search.webp";

const route = useRoute();
const dishes = ref(null);
const restaurants = ref(null);

watch(() => route.query.q, async (newVal) => {
  if (newVal) {
    try {
      const response = await api.get(`client/search?query=${newVal}`);
      if (response.data.data) {
        if (response.data.data.restaurants != null) {
          restaurants.value = response.data.data.restaurants;
        }
        if (response.data.data.dishes != null) {
          dishes.value = response.data.data.dishes;
        }
      }
    } catch (err) {
      console.error(err);
    }
  }
});

onMounted(async () => {
  if (route.query.q) {
    try {
      const response = await api.get(`client/search?query=${route.query.q}`);
      if (response.data.data) {
        if (response.data.data.restaurants != null) {
          restaurants.value = response.data.data.restaurants;
        }
        if (response.data.data.dishes != null) {
          dishes.value = response.data.data.dishes;
        }
      }
    } catch (err) {
      console.error(err);
    }
  }
});
</script>
