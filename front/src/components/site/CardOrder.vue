<script setup>

</script>

<template>
	<div class="border border-soft-border shadow-lg px-6 py-2 rounded-lg">
		<div class="flex flex-row justify-between">
			<div class="flex flex-col">
				<span class="font-semibold text-tertiary-light text-sm -mb-1">Previsão de entrega</span>
				<span class="font-semibold">Hoje, 16:00 - 16:15</span>
			</div>
			<div>
				<v-icon name="fa-circle" class="text-green-500" scale="0.7"/>
			</div>
		</div>
		<div class="flex flex-row w-full gap-6">
			<img src="@/assets/img/logo_header_clean.png" alt="logo" class="object-left max-h-16 max-w-16"/>
			<div class="flex flex-col w-full justify-center">
				<span class="w-full font-semibold -mb-1">Antojitos</span>
				<span class="w-full font-semibold text-tertiary-light text-sm">Entrega parceira</span>
			</div>
		</div>
		<div class="flex flex-row w-full justify-between px-8 py-2">
			<button class="text-primary font-semibold">Ajuda</button>
			<button class="text-primary font-semibold">Acompanhar</button>
		</div>
	</div>
</template>

<style lang="scss" scoped>

</style>