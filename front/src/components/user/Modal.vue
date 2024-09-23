<template>
	<div class="body">
		<slot></slot>
	</div>
</template>

<style scoped>
.body {
	@apply bg-white flex flex-col w-full my-[2%] mx-5 py-5 justify-center;

	@apply md:max-w-[1200px];
}
</style>