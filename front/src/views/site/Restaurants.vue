<template>
    <main class="w-full min-h-[calc(100vh-75px)] bg-white md:mt-[75px]">
        <div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col md:p-5">
            <h2 class="text-2xl mt-2 ml-2 font-normal text-tertiary-light">Restaurantes</h2>

            <div class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:gap-5 mt-5 mb-[100px]">
                <template v-if="restaurants.length > 0">

                    <CardRestaurant
                        v-for="(restaurant, index) in restaurants"
                        :key="restaurant.id || index"
                        :restaurant="restaurant"
                    />
                </template>

                <template v-else>
                    <p class="col-span-full text-center">Nenhum restaurante encontrado</p>
                </template>
            </div>
        </div>
    </main>
</template>

<script setup>
import CardRestaurant from '@/components/site/CardRestaurant.vue';
import { onMounted, ref } from 'vue';
import axios from 'axios';

const restaurants = ref([]);

onMounted(async () => {
    try {
        const { data } = await axios.get(`${import.meta.env.VITE_API_URL}restaurant/`);
        restaurants.value = data.data.content || []; 
    } catch (error) {
        console.error("Erro ao buscar os restaurantes:", error);
        restaurants.value = []; 
    }
});
</script>
