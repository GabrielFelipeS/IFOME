<template>
  <div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col md:p-5 mb-[100px] md:mb-0">
    <h2 class="text-2xl mt-2 ml-2 font-normal text-tertiary-light">Pratos</h2>

    <div class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:gap-5 mt-5 md:mb-0">
      <template v-if="dishes.length > 0">
        <router-link :to="{name:'restaurant internal', params: {id: dish.restaurantId }, query: { dish: dish.id }}" v-for="dish in dishes">
          <DishCard
               :key="dish.id"
              :dish="dish"
          />
        </router-link>
      </template>
    </div>
  </div>
</template>

<script setup>
import DishCard from "@/components/store/dish/DishCard.vue";
import {onMounted, ref, watch} from "vue";
const dishes = ref([]);

const props = defineProps({
  dishes: {
    type: Array,
    required: true
  }
});

watch(() => props.dishes, (newVal) => {
  dishes.value = newVal;
});

onMounted(() => {
  if (props.dishes) {
    dishes.value = props.dishes;
  }
});

</script>
