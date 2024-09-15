<template>
	<div class="body">
		<slot></slot>
	</div>
</template>

<style scoped>
.body {
	@apply bg-white flex flex-col w-full my-[2%] mx-5 py-5 justify-center;
	@apply rounded-2xl border-2 border-b-stone-400;

}
</style>