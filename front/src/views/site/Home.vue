<template>
	<PromoCards/>
	<RestaurantRecommendations />
	<DishRecommendations />
	<Restaurants/>
	<Dish/>
</template>

<script setup>
import PromoCards from "@/components/site/Home/PromoCards.vue";
import Restaurants from "@/components/site/Home/Restaurants.vue";
import Dish from "@/components/site/Home/Dish.vue";
import RestaurantRecommendations from "@/components/site/Home/RestaurantRecommendations.vue";
import DishRecommendations from "@/components/site/Home/DishRecommendations.vue";
</script>