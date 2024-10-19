<template>
    <Header />
    <main class="w-full min-h-[calc(100vh-75px)] bg-white md:mt-[75px]">
        <div class="w-[90%] md:max-w-[1200px] mx-auto flex flex-col md:p-5">
            <DropFilterArea />
            <h2 class="text-2xl mt-2 ml-2 font-normal text-tertiary-light">Restaurantes</h2>

            <div class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:gap-5 mt-5 mb-[100px]">
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
                <CardRestaurant />
            </div>
        </div>
    </main>
    <FooterMobile />
</template>

<script setup>
import Header from '@/components/site/Header.vue'
import FooterMobile from '@/components/site/FooterMobile.vue'
import DropFilterArea from '@/components/site/DropFilterArea.vue'
import CardRestaurant from '@/components/site/CardRestaurant.vue'
</script>
