<template>
    <div class="w-full h-[50px] grid grid-cols-3 gap-1 md:grid-cols-6 items-center">
        <div class="relative w-full">
            <select
                class="filter-select">
                <option value="1">Opção 1</option>
                <option value="2">Opção 2</option>
                <option value="3">Opção 3</option>
            </select>
            <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                <svg class="w-5 h-5 text-tertiary-light" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                    xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                </svg>
            </div>
        </div>
        <div class="relative w-full mx-1 my-3">
            <select
                class="filter-select">
                <option value="1">Opção 1</option>
                <option value="2">Opção 2</option>
                <option value="3">Opção 3</option>
            </select>
            <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                <svg class="w-5 h-5 text-tertiary-light" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                    xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                </svg>
            </div>
        </div>
        <div class="relative w-full mx-1 my-3">
            <select
                class="filter-select">
                <option value="1">Opção 1</option>
                <option value="2">Opção 2</option>
                <option value="3">Opção 3</option>
            </select>
            <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                <svg class="w-5 h-5 text-tertiary-light" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                    xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                </svg>
            </div>
        </div>
    </div>
</template>

<style scoped>
.filter-select{
    @apply w-full appearance-none rounded-full px-3 py-1 text-tertiary-light border-2 bg-white border-tertiary-light pl-4 pr-8;
}
</style>