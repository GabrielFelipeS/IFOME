<template>
	<div class="fixed bottom-[80px] w-full px-8 py-2 flex flex-row justify-between z-50 bg-white">
		<span>10,00</span>
		<button>Ver sacola <v-icon name="fa-chevron-right" /></button>
	</div>
	<footer
		class="w-full h-[75px] bg-white flex flex-row items-center justify-around fixed bottom-0 left-0 shadow-black shadow-2xl md:hidden z-50">
		<v-icon name="fa-home" scale="1.5" class="text-primary cursor-pointer" />
		<v-icon name="fa-shopping-bag" scale="1.5" class="text-primary cursor-pointer" @click="$emit('open-cart')"/>
		<v-icon name="fa-comment-alt" scale="1.5" class="text-primary cursor-pointer" />
		<v-icon name="fa-sign-out-alt" scale="1.5" class="text-primary cursor-pointer" />
	</footer>
</template>

