<script setup>
import { defineProps } from 'vue';

const props = defineProps({
	review: Object
});

console.log(props.review);
</script>

<template>
	<main class="flex justify-center mt-10">
		<div class="max-w-sm w-full bg-white shadow-lg rounded-lg p-6 border border-gray-200 flex flex-col justify-between">
			<!-- Comentário -->
			<div>
				<span class="text-gray-700">{{ review.comment }}</span>
			</div>

			<!-- Estrelas e Avaliação -->
			<div class="flex items-center space-x-2 mt-4 self-start">
				<v-icon name="fa-star" class="text-yellow-400" />
				<span class="text-gray-600 text-sm font-bold">{{ review.stars.toFixed(0) }}</span>
			</div>
		</div>
	</main>
</template>

<style scoped>
</style>
