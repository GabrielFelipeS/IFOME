<template>
    <div class="w-[95%] h-[95%] max-h-[60vh] m-auto border rounded-lg flex flex-col">

        <div class="flex flex-row justify-between">
            <div class="border-b p-4 text-lg font-semibold text-gray-700">
                Chat
            </div>
            <div class="p-4">
                <label class="flex items-center cursor-pointer">
                    <span class="mr-2 text-gray-700">Cliente</span>
                    <input type="checkbox" id="switch-role" class="hidden"
                           @change="activeChat = $event.target.checked ? 'client-delivery' : 'client-restaurant'"/>
                    <div class="w-10 h-5 rounded-full shadow-inner relative transition-colors duration-300" :class="{
                        'bg-primary': activeChat === 'client-delivery',
                        'bg-gray-300': activeChat === 'client-restaurant',
                    }">
                        <div class="dot w-5 h-5 bg-white rounded-full shadow absolute transition-transform duration-300"
                             :style="{ transform: activeChat === 'client-delivery' ? 'translateX(100%)' : 'translateX(0)' }">
                        </div>
                    </div>
                    <span class="ml-2 text-gray-700">Entregador</span>
                </label>

            </div>
        </div>

        <div ref="messageContainer" class="flex-grow overflow-y-auto p-4 space-y-3">
            <div v-for="(message, index) in currentChat" :key="index" class="w-full flex" :class="{
                'justify-end': message.senderType === 'CLIENT',
                'justify-start': message.senderType === (activeChat === 'client-restaurant' ? 'RESTAURANT' : 'DELIVERY'),
            }">
                <div :class="{
                    'bg-blue-100': message.senderType === 'CLIENT',
                    'bg-gray-100': message.senderType === 'DELIVERY' || message.senderType === 'RESTAURANT',
                }" class="flex flex-row gap-2 p-3 rounded-lg shadow-sm">
                    <p class="text-gray-800">{{ message.content }}</p>
                    <span class="text-xs text-gray-500 self-end">{{ formatTime(message.createdAt) }}</span>
                </div>
            </div>
        </div>

        <!-- Campo de Entrada de Mensagem -->
        <div class="border-t p-4 flex items-center">
            <input type="text" v-model="message" @keyup.enter="sendMessage" placeholder="Digite sua mensagem"
                   class="flex-grow border rounded-full p-3 px-4 text-gray-700 outline-none"
                   :disabled="activeChat === 'client-delivery' && !states.includes(currentStatus)"/>
            <button @click="sendMessage" class="ml-3 text-blue-500 hover:text-blue-700"
                    :disabled="activeChat === 'client-delivery' && !states.includes(currentStatus)" :class="{
                    'opacity-50 cursor-not-allowed': activeChat === 'client-delivery' && !states.includes(currentStatus),
                }">
                <!-- Ícone de Envio -->
                <v-icon name="fa-paper-plane" class="w-7 h-7 text-primary"/>
            </button>
        </div>
    </div>
</template>

<script setup>
import {ref, computed, onMounted, onUnmounted, nextTick, watch, onBeforeMount} from "vue";
import api from "@/services/api";
import pusher from "@/services/pusherOrders.js";
import {useOrderStatusStore} from '@/stores/orderStatus';
import {useRoute} from "vue-router";

const states = [
    'PRONTO_PARA_ENTREGA',
    'SAIU_PARA_ENTREGA',
];

const route = useRoute();

const orderStatusStore = useOrderStatusStore();

const currentStatus = computed(() => orderStatusStore.getOrderStatus(props.customerOrderId.orderId) || []);

const props = defineProps({
    chat: {
        type: [Array, null, String, Object],
        required: true,
    },
    customerOrderId: {
        type: [Number, String, Object],
        required: true,
    },
    deliveryChat: {
        type: [Array, null, String, Object],
        required: true,
    },
});

const activeChat = ref("client-restaurant");

const chat = computed(() => props.chat);
const deliveryChat = computed(() => props.deliveryChat);

const messageContainer = ref(null);
const message = ref("");

const currentChat = computed(() => {
    return activeChat.value === "client-restaurant" ? chat.value : deliveryChat.value;
});

const formatTime = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleTimeString([], {hour: "2-digit", minute: "2-digit"});
};

const scrollToBottom = () => {
    nextTick(() => {
        if (messageContainer.value) {
            messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
        }
    });
};

const sendMessage = async () => {
    if (!message.value.trim()) return;
    try {
        const endpoint =
            activeChat.value === "client-restaurant"
                ? `chat/client/restaurant/${props.customerOrderId.orderId}`
                : `chat/client/delivery/${props.customerOrderId.orderId}`;

        const response = await api.post(endpoint, {
            content: message.value,
        });

        console.log(response.data);

        message.value = "";
        scrollToBottom();
    } catch (error) {
        console.error("Erro ao enviar mensagem:", error);
    }
};

watch(() => props.customerOrderId.orderId, (newOrder, oldOrder) => {

    if (!props.customerOrderId.orderId) {
        chat.value = null;
        return;
    }
    pusher.unsubscribe(`chat_${oldOrder}`);
    const channel = pusher.subscribe(`chat_${newOrder}`);

    if (!channel) {
        console.error('Erro ao se inscrever no canal');
    }

    channel.bind("client-delivery-chat", (data) => {
        console.log('client-delivery-chat', data);
        chat.value.push(data);
        scrollToBottom();
    });
    channel.bind("client-restaurant-chat", (data) => {
        console.log('client-restaurant-chat', data);
        chat.value.push(data);
        scrollToBottom();
    });

    channel.bind("restaurant-delivery-chat", (data) => {
        console.log('restaurant-delivery-chat', data);
        deliveryChat.value.push(data);
        scrollToBottom();
    });

    message.value = "";
});

onUnmounted(() => {
    pusher.unsubscribe(`chat_${props.customerOrderId.orderId}`);
    message.value = '';
});

watch(() => props.customerOrderId.orderId, (newOrder, oldOrder) => {
    pusher.unsubscribe(`chat_${oldOrder}`);
    pusher.subscribe(`chat_${newOrder}`);

    message.value = '';
});
</script>

<style scoped>
.flex-grow {
    overflow-y: auto;
}
</style>
