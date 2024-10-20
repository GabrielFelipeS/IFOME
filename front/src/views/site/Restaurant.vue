<template>
    <Header class="hidden md:flex" />
    <main class="w-full min-h-[calc(100vh-75px)] bg-white md:mt-[75px] md:max-w-[1200px] mx-auto">
        <div v-if="!restaurant" class="w-full h-[250px] bg-gray-300 animate-pulse"></div>

        <div v-else>
            <div
                class="w-full h-[250px] bg-[url('../assets/img/default.jpg')] bg-center bg-cover md:mt-[100px] relative z-10">
                <div class="md:hidden">
                    <v-icon name="fa-chevron-left" scale="1.8"
                        class="text-white absolute top-5 left-5 md:relative md:hidden shadow-black z-10 md:z-0"
                        @click="window.history.back()" />
                </div>
                <div class="absolute top-0 left-0 w-full h-full bg-black opacity-30"></div>
            </div>

            <div
                class="w-full flex flex-row md:flex-row items-center p-4 rounded-t-lg bg-white mt-[-45px] md:mt-5 relative z-50">
                <img src='../../assets/img/logo_header_clean.png' alt="Logo do Restaurante"
                    class="w-20 h-20 object-cover rounded-full" />

                <div class="ml-4 flex-1">
                    <h3 class="text-lg font-semibold text-gray-800">{{ restaurant.nameRestaurant }} <span v-if="restaurant.isOpen">( Fechado )</span></h3>
                    <div class="flex items-center text-gray-600 text-sm mt-1">
                        <svg class="w-4 h-4 text-red-500 mr-1" fill="currentColor" viewBox="0 0 20 20"
                            xmlns="http://www.w3.org/2000/svg">
                            <path
                                d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.071 3.297a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.033a1 1 0 00-.364 1.118l1.071 3.297c.3.921-.755 1.688-1.54 1.118l-2.8-2.033a1 1 0 00-1.175 0l-2.8 2.033c-.785.57-1.84-.197-1.54-1.118l1.071-3.297a1 1 0 00-.364-1.118L2.98 8.724c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.071-3.297z" />
                        </svg>
                        <span class="mr-2">{{ restaurant.rating || '4.5' }}</span>
                        <span class="mx-2">·</span>
                        <span class="mr-2">{{ restaurant.foodCategory || 'Categoria' }}</span>
                        <span class="mx-2">·</span>
                        <span>1.5km</span>
                    </div>
                    <div class="flex items-center text-sm text-gray-500 mt-1">
                        <span>50-60 min</span>
                        <span class="mx-2">·</span>
                        <span>R$ 10,00</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="ml-4">
            <h2 class="text-2xl font-normal text-tertiary-light">Pratos</h2>
            <div
                class="w-[95%] h-[40px] md:full bg-background-inputs rounded-lg flex flex-row justify-between items-center mt-3">
                <v-icon name="fa-search" scale="1" class="text-primary mx-4 cursor-pointer" @click="searchForm" />
                <input type="text" class="w-[90%] h-full bg-transparent text-tertiary text-lg  outline-none"
                    placeholder="Busque nome" v-model="query" @keyup.enter="searchForm" />
            </div>
        </div>
    </main>
    <FooterMobile />
</template>

<script setup>
import Header from '@/components/site/Header.vue';
import FooterMobile from '@/components/site/FooterMobile.vue';
import { useRoute } from 'vue-router';
import { ref, onMounted } from 'vue';
import axios from 'axios';

const route = useRoute();
const restaurantId = route.params.id;

const restaurant = ref(null);
const error = ref(null);

const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhcGlfaWZvbWUiLCJzdWIiOiJlbWFpbDFAZW1haWwuY29tIiwiZXhwIjoxNzM0Njk4OTYyLCJpYXQiOjE3Mjk0MzkzNjIsImF1dGhvcml0aWVzIjpbIkNMSUVOVF9ERUxFVEUiLCJDTElFTlRfVVBEQVRFIiwiQ0xJRU5UX1JFQUQiLCJDTElFTlRfQ1JFQVRFIiwiUk9MRV9DTElFTlQiXX0.pYwaOGen8eFhZaNFAIbAz04UJAgziAh4Zf7CWD0gjTJ1Np1Agorv6jIUOY3IYIGUIE6VTDcs87Y1VGTQAaodNcLn10m6AffBXZAaDyHAEujIyn9PolIe4VlO9AMcctju_D3NRI437x-t_W4_tv3rjDhzb6EcoZQ0Fl6TpcYNKbX4f7qi0C5jlviFfOLHj3oh66f8sF1SGM65LgldN4-kHramMtLkXZB7xu_6yEkQi1JTvSr2nOjg2MS04JMKX5Jk9MKZBf6Y2_NV7T4hngj1A9bCeIolI4m4RP4BX17__C08WGLKuvUCNB2kKs8zyQ-ay1ZbiTp7y8flhJLV4SUXMg`
};

const fetchRestaurantData = async () => {
    try {
        const response = await axios.get(`${import.meta.env.VITE_API_URL}restaurant/${restaurantId}`, { headers });
        restaurant.value = response.data.data;
    } catch (err) {
        error.value = 'Erro ao buscar os dados do restaurante';
        console.error(err);
    }
};

onMounted(() => {
    fetchRestaurantData();
});

</script>
