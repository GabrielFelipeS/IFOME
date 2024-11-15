<script setup>
import api from "@/services/api.js";
import {onMounted, ref} from "vue";
import {getImage} from "@/services/getImage.js";
import {useRouter} from "vue-router";

const props = defineProps({
	order: {
		type: Object,
		required: true,
	},
});

const restaurantId = props.order.orderItems[0].restaurantId;
const restaurant = ref({});

const getRestaurantData = async () => {
	const response = await api.get('restaurant/' + restaurantId);
	if (response.status === 200) {
		restaurant.value = response.data.data;
	}
}
getRestaurantData();

console.log(props.order);

const router = useRouter();
</script>

<template>
	<div class="border border-soft-border shadow-lg px-6 py-2 rounded-lg">
		<div class="flex flex-row justify-between">
			<div class="flex flex-col">
				<span class="font-semibold text-tertiary-light text-sm -mb-1">Previsão de entrega</span>
				<span class="font-semibold">Hoje, 16:00 - 16:15</span>
			</div>
			<div>
				<v-icon name="fa-circle" class="text-green-500" scale="0.7"/>
			</div>
		</div>
		<div class="flex flex-row w-full gap-6">
			<img :src="getImage( restaurant.restaurantImage )" alt="logo" class="object-left max-h-16 max-w-16 rounded-full" />
			<div class="flex flex-col w-full justify-center">
				<span class="w-full font-semibold -mb-1">{{ restaurant.nameRestaurant }}</span>
				<span class="w-full font-semibold text-tertiary-light text-sm">Entrega parceira</span>
			</div>
		</div>
		<div class="flex flex-row w-full justify-between px-8 py-2">
			<button class="text-primary font-semibold">Ajuda</button>
			<button class="text-primary font-semibold" @click="router.push('/order/' + props.order.orderId)">Acompanhar</button>
		</div>
	</div>
</template>

<style lang="scss" scoped>

</style>