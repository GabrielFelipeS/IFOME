<template>
    <div class="w-full  max-h-[60vh] h-[60vh] m-auto border rounded-lg flex flex-col absolute bottom-0 z-50 bg-white"
         v-if="isActive">

        <div class="flex flex-row justify-between">
            <div class="flex flex-row">
                <buttton
                    class="uppercase bg-primary text-white flex justify-center items-center px-5 w-[70%] cursor-pointer"
                    @click="closeChat">fechar
                </buttton>
                <div class="border-b p-4 text-lg font-semibold text-gray-700">
                    Chat
                </div>
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
                    <span class="ml-2 text-gray-700">Restaurante</span>
                </label>

            </div>
        </div>

        <div ref="messageContainer" class="flex-grow overflow-y-auto p-4 space-y-3">
            <div v-for="(message, index) in currentChat" :key="index" class="w-full flex" :class="{
                'justify-end': message.senderType === 'DELIVERY',
                'justify-start': message.senderType === (activeChat === 'client-restaurant' ? 'CLIENT' : 'RESTAURANT'),
            }">
                <div :class="{
                    'bg-blue-100': message.senderType === 'DELIVERY',
                    'bg-gray-100': message.senderType === 'RESTAURANT' || message.senderType === 'CLIENT',
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
                   />
            <button @click="sendMessage" class="ml-3 text-blue-500 hover:text-blue-700"
                     :class="{
                    'opacity-50 cursor-not-allowed': !message.trim(),
                }">
                <!-- Ícone de Envio -->
                <v-icon name="fa-paper-plane" class="w-7 h-7 text-primary"/>
            </button>
        </div>
    </div>
</template>

<script setup>
import {ref, computed, onMounted, onUnmounted, nextTick, watch} from "vue";
import api from "@/services/api";
import pusher from "@/services/pusherOrders.js";
import {useOrderStatusStore} from '@/stores/orderStatus';
import {useRoute} from "vue-router";

const states = [
    'PRONTO_PARA_ENTREGA',
    'SAIU_PARA_ENTREGA',
    'NO_LOCAL',
    'EM_PREPARO',
    'PRONTO',
    'A_CAMINHO',
];

const route = useRoute();

const orderStatusStore = useOrderStatusStore();

const currentStatus = computed(() => orderStatusStore.getOrderStatus(props.customerOrderId) || []);

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
    isActive: {
        type: Boolean,
        default: false,
    },
});

const emit = defineEmits(["closeChat"]);

const isActive = ref(false);

watch(() => props.isActive, (newIsActive) => {
    console.log('isActive', newIsActive);
    isActive.value = newIsActive;
});

function closeChat() {
    isActive.value = false;
    emit("closeChat");
}

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
                ? `chat/client/delivery/${props.customerOrderId}`
                : `chat/restaurant/delivery/${props.customerOrderId}`;

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

watch(() => props.customerOrderId, (newOrder, oldOrder) => {
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
    pusher.unsubscribe(`chat_${props.customerOrderId}`);
    message.value = '';
});

watch(() => props.customerOrderId, (newOrder, oldOrder) => {
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
