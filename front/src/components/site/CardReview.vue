<script setup>
import { defineProps } from 'vue';

const props = defineProps({
	review: Object
});

console.log(props.review);
</script>

<template>
	<main class="flex justify-center mt-10">
		<div class="max-w-sm w-full bg-white shadow-lg rounded-lg p-6 border border-gray-200">
			<!-- Comentário -->
			<div class="mb-4">
				<span class="text-gray-700 text-lg font-medium">{{ review.comment }}</span>
			</div>

			<!-- Estrelas e Avaliação -->
			<div class="flex items-center space-x-2">
				<v-icon name="fa-star" class="text-yellow-400" />
				<span class="text-gray-600 text-sm">{{ review.stars }}</span>
			</div>
		</div>
	</main>
</template>

<style scoped>
</style>
