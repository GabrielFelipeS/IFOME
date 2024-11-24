<script setup>
import Header from "@/components/site/Header.vue";
import FooterMobile from "@/components/site/FooterMobile.vue";
import {ref} from "vue";
import CartDetails from "@/components/site/CartDetails.vue";

const cartOpen = ref(false);

</script>

<template>
	<Header class="hidden md:flex" @open-cart="cartOpen = !cartOpen"/>
	<router-view />
	<CartDetails
		@open-cart="cartOpen = !cartOpen"
		v-if="cartOpen"
	/>
	<FooterMobile
		@open-cart="cartOpen = !cartOpen"
		:cart-open="cartOpen"
	/>
</template>