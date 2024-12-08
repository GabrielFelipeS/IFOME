<script setup>
import CardOrder from "@/components/site/CardOrder.vue";
import api from "@/services/api.js";
import { onMounted, ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useToast } from "vue-toast-notification";

const router = useRouter();
const route = useRoute();

const orders = ref([]);

const verifyLogin = async () => {
	const response = await api.post("auth/token/client/");
	return response.status === 200;
};

const getOrders = async () => {
	try {
		const response = await api.get("order/customerOrders");
		if (response.status === 200) {
			orders.value = response.data;
		} else {
			console.error("Erro ao carregar pedidos");
			return false;
		}
	} catch (error) {
		console.error("Erro de requisição:", error);
		return false;
	}
};

onMounted(async () => {
	if (await verifyLogin()) {
		await getOrders();
	}

	if (route.query.failed === "true") {
		useToast().error(
			"Ocorreu uma falha ao registrar o pagamento! Tente novamente ou contate o banco",
			{ position: "top-right" }
		);

		const { failed, ...restQuery } = route.query;
		router.replace({ query: restQuery });
	}
});
</script>

<template>
	<div class="w-full flex flex-row justify-center items-center">
		<div class="main">
			<div class="flex flex-row h-[60px] justify-between items-center px-5">
				<button class="md:hidden" @click="router.go(-1)">
					<v-icon name="fa-chevron-left" scale="1.8" class="text-primary" />
				</button>
				<span class="font-bold text-tertiary-light text-lg mr-6">MEUS PEDIDOS</span>
			</div>

			<div class="order-list pt-8">
				<CardOrder v-for="order in orders" :key="order.id" :order="order" />
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
