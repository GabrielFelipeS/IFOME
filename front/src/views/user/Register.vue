<script setup>
import '@/components/user/Header.vue'
import Header from "@/components/user/Header.vue";
import ModalRegister from "@/components/user/register/ModalRegister.vue";
</script>

<template>
	<div class="content">
		<Header />
		<div class="main">
			<ModalRegister />
		</div>
	</div>
</template>

<style lang="scss" scoped>

	.content {
		@apply w-screen h-screen flex flex-col align-middle;
		background-image: url("../../assets/img/user/bg-cadastro.jpg");
		background-color: #fff;
		background-position: center;
		background-size: cover;
	}

	.main {
		@apply mt-[50%];
	}

</style>