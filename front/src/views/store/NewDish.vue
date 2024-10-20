<script setup>

import Form from "@/components/store/dish/new/Form.vue";
import Header from "@/components/user/Header.vue";

document.querySelector('#app').setAttribute('style', 'overflow-x: hidden');
</script>

<template>
	<div class="content">
		<header class="header">
			<Header :home-page="'user-login'"/>
		</header>
		<div class="main">
			<Form class="component"/>
		</div>
	</div>
</template>

<style scoped>
.content {
	@apply w-screen min-h-screen flex flex-col align-middle;
	background-color: #fff;
	background-position: center;
	background-size: cover;
}
.main {
	@apply flex flex-row flex-grow h-full w-full justify-center items-center;
}
.header {
	@apply flex flex-row
}

.component {
	@apply max-w-[600px];
}
</style>