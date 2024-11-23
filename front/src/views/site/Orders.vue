<script setup>
import Header from "@/components/site/Header.vue";
import FooterMobile from "@/components/site/FooterMobile.vue";
import CardOrder from "@/components/site/CardOrder.vue";
import api from "@/services/api.js";
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";

const orders = ref([]);

const verifyLogin = async () => {
	const response = await api.post("auth/token/client/");
	return response.status === 200;
}
const getOrders = async () => {
	const response = await api.get('order/customerOrders');
	if (response.status !== 200) {
		return false;
	}
	orders.value = response.data;
}

onMounted(async () => {
	if (await verifyLogin()) {
		const response = await getOrders();
	}
});

const router = useRouter();

</script>

<template>
	<div class="w-full flex flex-row justify-center items-center">
		<div class="main">
			<div class="flex flex-row h-[60px] justify-between items-center px-5
				md:justify-start md: ">
				<button class="md:hidden" @click="router.go(-1)">
					<v-icon name="fa-chevron-left" scale="1.8" class="text-primary md:hidden"/>
				</button>
				<span class="font-bold text-tertiary-light text-lg mr-6">MEUS PEDIDOS</span>
				<div class="md:hidden"></div>
			</div>
			<div class="order-list pt-8">
				<CardOrder v-for="order in orders" :order="order"/>
			</div>
			<div class="pt-12 flex flex-col ">
				<span class="font-semibold text-lg mx-5">Histórico</span>
				<div class="order-list">
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
	.main {
		@apply w-full h-full flex flex-col max-w-[1200px] justify-center self-center;

		@apply md:mt-[85px];
	}

	.order-list {
		@apply m-4 flex flex-col gap-6;

		@apply md:grid md:grid-cols-2 gap-4;
	}
</style>