import '@/assets/tailwind.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import { OhVueIcon, addIcons } from "oh-vue-icons";
import * as FaIcons from "oh-vue-icons/icons/fa";
import * as MdIcons from "oh-vue-icons/icons/md";


import ToastPlugin from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-bootstrap.css';

import Autocomplete from '@trevoreyre/autocomplete-vue';

const Fa = Object.values({ ...FaIcons });
addIcons(...Fa);
const Md = Object.values({ ...MdIcons });
addIcons(...Md);


const app = createApp(App)

app.use(createPinia());
app.use(router);
app.use(ToastPlugin);

app.component('autocomplete', Autocomplete);
app.component("v-icon", OhVueIcon);

app.mount('#app')
