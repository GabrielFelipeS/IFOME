<template>
    <footer
        class="w-full h-[75px] bg-white flex flex-row items-center justify-around fixed bottom-0 left-0 shadow-black shadow-2xl md:hidden z-50">
        <v-icon name="fa-home" scale="1.5" class="text-primary cursor-pointer" />
        <v-icon name="fa-shopping-bag" scale="1.5" class="text-primary cursor-pointer" @click="$emit('open-cart')"/>
        <v-icon name="fa-comment-alt" scale="1.5" class="text-primary cursor-pointer" />
        <v-icon name="fa-sign-out-alt" scale="1.5" class="text-primary cursor-pointer" />
    </footer>
</template>