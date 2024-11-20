<template>
    <header
        class="w-full h-[75px] bg-white flex flex-row items-center justify-around relative md:shadow-xl shadow-black md:fixed top-0 md:z-50">
        <img src="../../assets/img/logo_header_clean.png" class="h-full hidden md:block" alt="Logo">
        <nav class="hidden md:block">
            <ul class="flex flex-row text-tertiary-light gap-4">
                <li>Inicio</li>
                <router-link :to="{ name: 'restaurants' }">Restaurantes</router-link>
                <li>Pratos</li>
            </ul>
        </nav>
        <div
            class="w-[95%] h-[60%] md:w-[50%] bg-background-inputs rounded-2xl flex flex-row justify-between items-center">
            <v-icon name="fa-search" scale="1" class="text-primary mx-4 cursor-pointer" @click="searchForm" />
            <input type="text" class="w-[90%] h-full bg-transparent text-tertiary text-lg pl-2 outline-none"
                placeholder="Busque por item ou loja" v-model="query" @keyup.enter="searchForm" />
        </div>
        <div class="flex-row hidden md:flex">
            <v-icon name="fa-shopping-bag" scale="1.5" class="text-primary cursor-pointer" @click="emit('open-cart')"/>
            <div class="flex flex-col">
                <p class="text-xs text-tertiary-light">R$ {{ formatReal(cart.totalPrice) }}</p>
                <p class="text-xs text-tertiary-light">{{ cart.totalItems }} Itens</p>
            </div>
        </div>
		<div class="relative flex-row hidden md:flex">
			<!-- Ícone com evento de click -->
			<v-icon name="fa-user-circle" scale="1.8" class="text-primary cursor-pointer" @click="toggleDropdown" />
			<!-- Dropdown -->
			<transition
				name="dropdown"
				enter-active-class="transition ease-out duration-100"
				enter-from-class="transform opacity-0 -translate-y-5"
				enter-to-class="transform opacity-100 translate-y-0"
				leave-active-class="transition ease-in "
				leave-from-class="transform opacity-100 translate-y-0"
				leave-to-class="transform opacity-0 -translate-y-5"
			>
				<ul v-if="isDropdownOpen" class="absolute top-full -right-6 mt-2 w-48 text-lg text-tertiary-light
						bg-white border border-gray-300 rounded-md shadow-2xl" >
					<li class="px-4 py-2 hover:bg-gray-100 cursor-pointer flex items-center gap-4"
						@click="router.push('/orders')">
						<v-icon name="fa-receipt" scale="1.1"/>
						Pedidos
					</li>
					<li class="px-4 py-2 hover:bg-gray-100 cursor-pointer flex items-center gap-4">
						<v-icon name="fa-sign-out-alt" scale="1.1" class="cursor-pointer" />
						Sair
					</li>
				</ul>
			</transition>
		</div>
    </header>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import { useCart } from "@/stores/cart.js";
import api from "@/services/api.js";
import {useToast} from "vue-toast-notification";
import {formatReal} from "@/services/formatReal.js";
import router from "@/router/index.js";

const emit = defineEmits(['search', 'open-cart'])

const query = ref('')

function searchForm() {
    if (query.value) {
        emit('search', query.value)
    }
}

const cart = useCart();
const toat = useToast();

const updateCart = async () => {
	if (localStorage.getItem('token') !== null) {
		try {
			const response = await api.get('client/cart');
			const order = response.data.data;
			cart.updateCart(order);
		} catch (e) {
			console.log("Erro ao carregar carrinho: " + e);
			toast.error(e.data.message);
		}
	}
}

onMounted(() => {
	updateCart();
})

const isDropdownOpen = ref(false);
const toggleDropdown = () => {
	isDropdownOpen.value = !isDropdownOpen.value;
};
</script>
