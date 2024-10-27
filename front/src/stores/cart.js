import {defineStore} from "pinia";
import {computed, ref} from "vue";

export const useCart = defineStore("cart", () => {
	const totalPrice = ref(0);
	const totalItems = ref(0);

	function updateCart(order) {
		totalPrice.value = order.totalPrice;
		totalItems.value = order.totalQuantity;
	}

	return {totalPrice, totalItems, updateCart};
})