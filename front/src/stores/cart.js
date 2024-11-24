import {defineStore} from "pinia";
import {ref} from "vue";
import api from "@/services/api.js";

export const useCart = defineStore("cart", () => {
	const totalPrice = ref(0);
	const totalItems = ref(0);
	const order = ref({});

	async function updateCart(param = null) {
		if (localStorage.getItem('token') === null) { return; }
		try {
			const response = await api.post('/auth/token/client/', {}, {
				headers: {
					Authorization: `Bearer ${localStorage.getItem('token')}`
				}
			});
			if (response.status !== 200) { return; }
		} catch (e) {
			return;
		}

		if (param == null && localStorage.getItem('token') !== null) {
			try {
				const response = await api.get('client/cart', {
					headers: {
						Authorization: `Bearer ${localStorage.getItem('token')}`
					}
				});
				param = response.data.data;
			} catch (e) {
				console.error("Erro ao carregar carrinho no Pinia: ", e);
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