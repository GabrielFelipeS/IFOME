<template>
  <swiper
      :modules="[Navigation, Pagination, Autoplay]"
      :loop="true"
      :autoplay="{
        delay: 2500
      }"
      :breakpoints="{
        '640':{
          slidesPerView: 1,
          spaceBetween: 20
        },
        '768':{
          slidesPerView: 2,
          spaceBetween: 40
        },
        '1024':{
          slidesPerView: 3,
          spaceBetween: 50
        }
      }"
      :slides-per-view="1"
      :space-between="20"
      class="mySwiper md:mt-[75px] md:max-w-[1200px]"
  >
    <swiper-slide v-for="(image, index) in images" :key="index">
      <div class="w-full h-[200px] mx-auto px-5 rounded-2xl md:mt-10">
        <img :src="image" alt="Imagem" class="w-full h-full object-cover rounded-2xl"/>
      </div>
    </swiper-slide>
  </swiper>
</template>

<script setup>
import {ref} from "vue";
import { Swiper, SwiperSlide } from 'swiper/vue';
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import {Navigation, Pagination, Autoplay} from 'swiper/modules';

const images = ref([
  "https://via.placeholder.com/600x300?text=Imagem+1",
  "https://via.placeholder.com/600x300?text=Imagem+2",
  "https://via.placeholder.com/600x300?text=Imagem+3",
  "https://via.placeholder.com/600x300?text=Imagem+3",
  "https://via.placeholder.com/600x300?text=Imagem+3",
  "https://via.placeholder.com/600x300?text=Imagem+3",
  "https://via.placeholder.com/600x300?text=Imagem+3",
  "https://via.placeholder.com/600x300?text=Imagem+3",
  "https://via.placeholder.com/600x300?text=Imagem+3",
  "https://via.placeholder.com/600x300?text=Imagem+3",
  "https://via.placeholder.com/600x300?text=Imagem+3",

]);
</script>
