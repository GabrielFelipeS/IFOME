<script setup>

import {ref} from "vue";
import {getImage} from "@/services/getImage.js";

const props = defineProps({
	dish: {
		type: Object,
		required: true
	},
	footerLead: {
		type: String,
		default: 'A partir de R$ '
	},
});

const imgUrl = ref('');
imgUrl.value = getImage(props.dish.dishImage);
</script>

<template>
	<div class="card">
		<div class="card-image">
			<img :src="imgUrl" alt="Prato" class="rounded-md max-w-[100px] min-w-[100px] min-h-[100px] max-h-[100px]">
		</div>
		<div class="card-body">
			<div class="title">
				{{ dish.name }}
			</div>
			<div class="content">
				{{ dish.description }}
			</div>
			<div class="footer">
				<div class="inline-flex">
					{{ footerLead }}{{ dish.price.toLocaleString('pt-BR', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2}) }}
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
	.card {
		@apply flex flex-row justify-between mt-4 p-3 min-h-[120px];
		@apply rounded-md border shadow;
		@apply hover:scale-105 transition-transform duration-300;
	}
	.card-image {
		@apply col-span-1 rounded-md;
	}
	.card-body {
		@apply w-full ml-3;
		@apply flex flex-col justify-between;
		@apply text-tertiary-light;
	}
	.title {
		@apply text-lg font-semibold;
	}
	.content {
		@apply text-xs max-h-[48px] line-clamp-3 h-full;
	}
	.footer {
		@apply flex flex-row justify-between;
		@apply text-xs font-semibold;
	}
</style>