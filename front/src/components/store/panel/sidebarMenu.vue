<template>
    <aside class="w-[150px] h-[100vh] bg-white border-r border-soft-border flex justify-between py-10 flex-col">
        <div class="flex flex-col text-primary w-full h-[150px] items-center justify-center">
            <v-icon name="fa-concierge-bell" class="w-8 h-8 text-primary"></v-icon>
            <p>Pedidos</p>
        </div>
        <div class="flex flex-col justify-center items-center gap-3">
            <v-icon name="fa-cog" class="w-8 h-8 text-primary"></v-icon>
            <v-icon name="fa-sign-out-alt" scale="1.5" class="text-primary cursor-pointer" v-on:click="logout()" />
        </div>
    </aside>
</template>

<script setup>
	import {useRouter} from "vue-router";
	import {useToast} from "vue-toast-notification";

	const toast = useToast();
	const router = useRouter();
	function logout() {
		localStorage.removeItem("token");
		router.push({ name: "store-login" });
		toast.info("Logout feito com sucesso.", {position: "bottom-left"});
	}
</script>