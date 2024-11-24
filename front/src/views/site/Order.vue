<script setup>
import {useRoute, useRouter} from "vue-router";
import api from "@/services/api.js";
import {computed, onMounted, onUnmounted, ref} from "vue";
import {formatReal} from "@/services/formatReal.js";
import pusher from "@/services/pusherOrders.js";
import {useOrderStatusStore} from "@/stores/orderStatus.js";
import {useToast} from "vue-toast-notification";

// Variáveis estáticas
const router = useRouter();
const route = useRoute();
const orderId = route.params.id;
const pusherService = pusher;
const orderStatusStore = useOrderStatusStore();
const toast = useToast();

// Variáveis reativas
const order = ref({});
const orderItems = ref([]);
const orderInfo = ref([]);
const infoFirst = ref({
    orderStatus: '',
    status: '',
});
const windowWidth = ref(window.innerWidth);
const dropdown = ref(false);
const shouldShowDropdown = computed(() => dropdown.value && windowWidth.value < 1200);

const emit = defineEmits(['status_updated']);

// Métodos de API
const verifyLogin = async () => {
    const response = await api.post("auth/token/client/");
    return response.status === 200;
};
const getOrder = async () => {
    const login = await verifyLogin();
    if (!login) {
        return false;
    }
    const response = await api.get('order/customerOrders');
    if (response.status !== 200) {
        return false;
    }

    return response.data.find(order => order.orderId == orderId);
};

// Atualizar dados do pedido
getOrder().then((response) => {
    order.value = response;
    orderItems.value = order.value.orderItems;
    orderInfo.value = order.value.orderInfo;
    infoFirst.value = orderInfo.value.pop();
});

// Eventos de tela e dropdown
const toggleDropdown = () => {
    if (orderInfo.value.length > 0 && windowWidth.value < 1200) {
        dropdown.value = !dropdown.value;
    }
};
const updateWindowWidth = () => {
    windowWidth.value = window.innerWidth;
};

// Evento de montagem
onMounted(() => {
    getOrder().then((response) => {
        order.value = response;
        orderItems.value = order.value.orderItems;
        orderInfo.value = order.value.orderInfo;
        infoFirst.value = orderInfo.value.pop();
    });

    const channel = pusherService.subscribe('order-channel');
    channel.bind(`order-status-updated_${orderId}`, (data) => {
        console.log('DATA DO PUSHER', data);
        if (data.orderId == orderId) {
            orderStatusStore.setOrderStatus(data.orderId, data.status);
            emit('status_updated', data.status);

            // Atualiza o status mostrado
            orderInfo.value.push(infoFirst.value);
            infoFirst.value = data;
            infoFirst.value['orderStatus'] = data.status;
            infoFirst.value['localDateTime'] = data.time;
        }
    });
    window.addEventListener('resize', updateWindowWidth);
});

// Evento de desmontagem
onUnmounted(() => {
    pusherService.unsubscribe('order-channel');
    window.removeEventListener('resize', updateWindowWidth);
});

// Avaliação
const reviewComment = ref('');
const stars = ref([false, false, false, false, false]);
const setStars = (index) => {
    stars.value = stars.value.map((star, i) => i <= index);
};
setStars(4);

const sendReview = async () => {
    const review = {
        stars: stars.value.filter(star => star).length,
        comment: reviewComment.value,
    };
    try {
        const response = await api.post(`client/order/${orderId}/review`, review);
        if (response.status === 200) {
            toast.open({
                message: response?.data?.message || 'Avaliação enviada com sucesso!',
                type: 'success',
                position: 'top',
            });
        }
    } catch (e) {
        const errorMessage = e.response?.data?.message || 'Erro ao enviar avaliação';
        toast.open({
            message: errorMessage,
            type: e.status === 401 ? 'error' : 'warning',
            position: 'top',
        });
        console.error("Erro ao enviar avaliação", e.response);
    }
};
</script>

<template>
    <div class="w-full flex flex-col items-center">
        <div class="main">
            <!-- Cabeçalho -->
            <div class="flex flex-row h-[60px] justify-between items-center px-5 md:justify-start">
                <button @click="router.push('/orders')">
                    <v-icon name="fa-chevron-left" scale="1.8" class="text-primary md:hidden" />
                </button>
                <button class="font-bold text-primary mr-4 flex items-center gap-1 md:hidden">
                    <v-icon name="fa-regular-question-circle" scale="1.3" class="md:hidden" />
                    Ajuda
                </button>
            </div>

            <!-- Status do Pedido -->
            <div class="flex flex-col items-start justify-center px-5 gap-3">
                <span class="text-tertiary-light font-semibold">Previsão de entrega</span>
                <span class="text-lg font-bold -mt-4">Hoje, 19:12 - 19:27</span>
                <div class="bg-green-700 h-1 rounded-full transition-all duration-300 w-full"></div>
            </div>
            <div class="flex flex-col items-start justify-center px-5 gap-3 py-4">
                <button class="flex flex-row items-center w-full justify-between gap-4 md:pb-1" @click="toggleDropdown">
                    <v-icon name="fa-circle" scale="1" class="text-green-600" />
                    <span class="text-xl font-semibold w-full self-start text-start leading-none">{{ infoFirst.orderStatus.replaceAll('_', ' ') }}</span>
                    <v-icon name="fa-chevron-down" scale="1.5" class="text-tertiary-light" v-if="orderInfo.length > 0 && windowWidth < 1200" />
                </button>
                <transition name="dropdown" enter-active-class="transition ease-out duration-100" enter-from-class="transform opacity-0 -translate-y-5" enter-to-class="transform opacity-100 translate-y-0" leave-active-class="transition ease-in " leave-from-class="transform opacity-100 translate-y-0" leave-to-class="transform opacity-0 -translate-y-5">
                    <div class="dropdown" v-if="(shouldShowDropdown && orderInfo.length > 0) || (!dropdown && windowWidth >= 1200)">
                        <div class="flex flex-row justify-between" v-for="info in orderInfo">
                            <div class="flex flex-row items-center w-full justify-between gap-4 font-semibold">
                                <v-icon name="fa-circle" scale="0.75" class="text-green-600 ml-0.5" />
                                <span class="text-tertiary-light w-full self-start text-start leading-none">{{ info.orderStatus.replaceAll('_', ' ') }}</span>
                                <span class="text-tertiary-light text-sm pr-1">{{ (new Date(info.localDateTime).getHours()) - 3 }}:{{ new Date(info.localDateTime).getUTCMinutes().toString().padStart(2, '0') }}</span>
                            </div>
                        </div>
                    </div>
                </transition>
            </div>

            <!-- Resumo do Pedido -->
            <div class="px-3 flex flex-col gap-y-0 mt-6 md:max-h-[calc(100%-15px)]" :class="{ 'opacity-30': shouldShowDropdown }">
                <span class="font-semibold px-1">Resumo do pedido</span>
                <div class="order-items border-s border-e px-3">
                    <div class="product" v-for="item in orderItems">
                        <div class="flex flex-col items-start gap-1.5 justify-center">
                            <span class="uppercase">{{ item.dish.name }}</span>
                        </div>
                        <div class="mr-6 flex flex-col items-end">
                            <span>R$ {{ formatReal(item.unitPrice) }}</span>
                            <span>x {{ item.quantity }}</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Avaliação -->
            <template id="avaliacao" v-if="infoFirst.orderStatus.toLowerCase() === 'concluido'">
                <div class="px-5 flex flex-col gap-y-4 w-full max-w-[600px] self-center mt-12 mb-8">
                    <span class="font-semibold text-lg">Avalie o restaurante</span>
                    <div class="flex flex-row justify-between p-2">
                        <v-icon v-for="(star, index) in stars" :key="index" name="fa-star" scale="2" @click="setStars(index)" class="cursor-pointer" :class="{ 'text-yellow-400': star, 'text-tertiary-light': !star }" />
                    </div>
                    <textarea class="w-full h-24 border border-s rounded-md p-2" placeholder="Comentário opcional" v-model="reviewComment" />
                    <button class="bg-primary text-white self-center rounded-md w-fit p-2 px-4" @click="sendReview">
                        Enviar avaliação
                        <v-icon name="fa-chevron-right" class="ml-2" />
                    </button>
                </div>
            </template>
        </div>
    </div>
</template>

<style scoped>
.main {
    @apply relative w-full h-full flex flex-col max-w-[1200px] justify-start self-center gap-y-4 overflow-auto pt-4;
    @apply pb-[80px];

    @apply md:pt-[90px] md:max-w-[1200px] md:self-center;
}
.order-items {
    @apply flex flex-col overflow-y-scroll h-full max-h-44 mt-4;
    @apply divide-y-4;

    @apply md:overflow-y-auto md:h-fit md:max-h-full;
}
.product {
    @apply flex flex-row justify-between py-4 -mt-1;
}
.order-summary {
    @apply flex flex-col gap-4 px-6 font-semibold max-h-[20%];
}
.dropdown {
    @apply absolute top-0 right-0 flex flex-col-reverse gap-4 mt-60 px-5 w-full z-50;
    @apply bg-white border-b pb-4 shadow-xl;
    @apply transition duration-500 ease-in-out transform;

    @apply md:static md:mt-0 md:px-0 md:bg-transparent md:shadow-none;
}
</style>
