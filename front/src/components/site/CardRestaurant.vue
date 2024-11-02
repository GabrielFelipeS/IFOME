<template>
    <router-link :to="`/restaurant/${props.restaurant.id}`"
        class="w-full flex flex-row md:flex-row items-center p-4 rounded-lg shadow-sm shadow-[#B3B3B3] bg-white mt-5 md:shadow-none md:hover:shadow-lg md:hover:scale-105 md:transition-all md:duration-150">
        <img :src="imageUrl  || '../../assets/img/logo_header_clean.png'"
            alt="Logo do Restaurante" class="w-20 h-20 object-cover rounded-full" />

        <div class="ml-4 flex-1">
            <h3 class="text-lg font-semibold text-gray-800">
                {{ props.restaurant.nameRestaurant }}
                <span v-if="isClosed">(Fechado)</span>
            </h3>
            <div class="flex items-center text-gray-600 text-sm mt-1">
                <svg class="w-4 h-4 text-red-500 mr-1" fill="currentColor" viewBox="0 0 20 20"
                    xmlns="http://www.w3.org/2000/svg">
                    <path
                        d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.071 3.297a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.033a1 1 0 00-.364 1.118l1.071 3.297c.3.921-.755 1.688-1.54 1.118l-2.8-2.033a1 1 0 00-1.175 0l-2.8 2.033c-.785.57-1.84-.197-1.54-1.118l1.071-3.297a1 1 0 00-.364-1.118L2.98 8.724c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.071-3.297z" />
                </svg>
                <span class="mr-2">4.5</span>
                <span class="mx-2">·</span>
                <span class="mr-2">{{ props.restaurant.foodCategory }}</span>
                <span class="mx-2">·</span>
                <span>1.5km</span>
            </div>
            <div class="flex items-center text-sm text-gray-500 mt-1">
                <span>50-60 min</span>
                <span class="mx-2">·</span>
                <span>R$ 10,00</span>
            </div>
        </div>
    </router-link>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const props = defineProps({
    restaurant: Object
});

const currentDay = ref('');
const currentTime = ref('');
const imageUrl = `${import.meta.env.VITE_API_URL}image/${props.restaurant.restaurantImage}`;

onMounted(() => {
    const now = new Date();
    const daysOfWeek = [
        'Domingo', 'Segunda-feira', 'Terça-feira', 'Quarta-feira', 'Quinta-feira', 'Sexta-feira', 'Sábado'
    ];

    currentDay.value = daysOfWeek[now.getDay()];
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    currentTime.value = `${hours}:${minutes}`;
});


const isClosed = props.restaurant.isOpen
</script>
