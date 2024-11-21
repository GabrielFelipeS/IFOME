<template>
  <div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col md:p-5 md:mt-[75px]">
    <h2 class="text-2xl mt-2 ml-2 font-normal text-tertiary-light">Restaurantes</h2>

    <div class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:gap-5 mt-5 ">
      <template v-if="restaurants.length > 0">

        <CardRestaurant
            v-for="(restaurant, index) in restaurants"
            :key="restaurant.id || index"
            :restaurant="restaurant"
        />

      </template>
    </div>
  </div>
</template>

<script setup>
import CardRestaurant from '@/components/site/CardRestaurant.vue';
import {onMounted, ref, computed, watch} from 'vue';

const restaurants = ref([]);

const props = defineProps({
  restaurants: {
    type: Array,
    required: true
  }
});

watch(() => props.restaurants, (newVal) => {
  restaurants.value = newVal;
});

onMounted(() => {
  if (props.restaurants) {
    restaurants.value = props.restaurants;
  }
});
</script>
