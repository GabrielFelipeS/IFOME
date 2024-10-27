import {defineStore} from "pinia";
import {ref} from "vue";

export const useCart = defineStore("cart", () => {
	const totalPrice = ref(0);
	const totalItems = ref(0);
	const order = ref({});

	function updateCart(param) {
		totalPrice.value = param.totalPrice;
		totalItems.value = param.totalQuantity;
		order.value = param;
	}

	return {totalPrice, totalItems, order, updateCart};
})