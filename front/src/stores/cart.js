import {defineStore} from "pinia";
import {ref} from "vue";
import api from "@/services/api.js";

export const useCart = defineStore("cart", () => {
	const totalPrice = ref(0);
	const totalItems = ref(0);
	const order = ref({});

	async function updateCart(param = null) {
		if (param == null && localStorage.getItem('token') !== null) {
			try {
				const response = await api.get('client/cart');
				param = response.data.data;
			} catch (e) {
				console.log("Erro ao carregar carrinho no Pinia: " + e);
			}
		}
		if (param === null) {
			throw new Error("Erro no carregamento do carrinho por api (Pinia)");
		}
		totalPrice.value = param.totalPrice;
		totalItems.value = param.totalQuantity;
		order.value = param;
	}

	return {totalPrice, totalItems, order, updateCart};
})