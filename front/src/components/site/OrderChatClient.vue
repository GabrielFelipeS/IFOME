<template>
    <div class="w-[95%] h-[95%] max-h-[60vh] m-auto border rounded-lg flex flex-col">
        <!-- Cabeçalho do Chat -->
        <div class="flex flex-row justify-between items-center border-b p-4">
            <div class="text-lg font-semibold text-gray-700">Chat</div>
            <div>
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

        <!-- Mensagens -->
        <div ref="messageContainer" class="flex-grow overflow-y-auto p-4 space-y-3">
            <div v-for="(message, index) in currentChat" :key="index" class="w-full flex" :class="{
                'justify-end': message.senderType === 'CLIENT',
                'justify-start': message.senderType === (activeChat === 'client-restaurant' ? 'RESTAURANT' : 'DELIVERY'),
            }">
                <div :class="{
                    'bg-blue-100': message.senderType === 'CLIENT',
                    'bg-gray-100': message.senderType !== 'CLIENT',
                }" class="flex flex-row gap-2 p-3 rounded-lg shadow-sm">
                    <p class="text-gray-800">{{ message.content }}</p>
                    <span class="text-xs text-gray-500 self-end">{{ formatTime(message.createdAt) }}</span>
                </div>
            </div>
        </div>

        <!-- Campo de Entrada -->
        <div class="border-t p-4 flex items-center">
            <input type="text" v-model="message" @keyup.enter="sendMessage" placeholder="Digite sua mensagem"
                   class="flex-grow border rounded-full p-3 px-4 text-gray-700 outline-none"
                   />
            <button @click="sendMessage" class="ml-3 text-blue-500 hover:text-blue-700">
                <v-icon name="fa-paper-plane" class="w-7 h-7 text-primary"/>
            </button>
        </div>
    </div>
</template>

<script setup>
import {ref, computed, onMounted, onUnmounted, watch, nextTick} from "vue";
import api from "@/services/api";
import pusher from "@/services/pusherOrders.js";
import {useOrderStatusStore} from "@/stores/orderStatus";

// Estados permitidos para envio de mensagens para o entregador
const states = ["PRONTO_PARA_ENTREGA", "SAIU_PARA_ENTREGA", "PRONTO"];
const orderStatusStore = useOrderStatusStore();

// Props recebidas do componente pai
const props = defineProps({
    chat: {type: Array, required: true},
    customerOrderId: {type: [Number, String], required: true},
    deliveryChat: {type: Array, required: true},
});

const currentStatus = computed(() => orderStatusStore.getOrderStatus(props.customerOrderId) || []);

// Controle de mensagens e chats ativos
const activeChat = ref("client-restaurant");
const chat = computed(() => props.chat);
const deliveryChat = computed(() => props.deliveryChat);
const currentChat = computed(() =>
    activeChat.value === "client-restaurant" ? chat.value : deliveryChat.value
);

const messageContainer = ref(null);
const message = ref("");

// Formata o horário das mensagens
const formatTime = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleTimeString([], {hour: "2-digit", minute: "2-digit"});
};

// Rola para o fim das mensagens ao carregar ou enviar
const scrollToBottom = () => {
    nextTick(() => {
        if (messageContainer.value) {
            messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
        }
    });
};

// Envia uma mensagem para o chat ativo
const sendMessage = async () => {
    if (!message.value.trim()) return; // Impede envio de mensagens vazias
    try {
        const endpoint =
            activeChat.value === "client-restaurant"
                ? `chat/client/restaurant/${props.customerOrderId}`
                : `chat/client/delivery/${props.customerOrderId}`;

        await api.post(endpoint, {content: message.value});
        message.value = ""; // Limpa o campo de mensagem
        scrollToBottom();
    } catch (error) {
        console.error("Erro ao enviar mensagem:", error);
    }
};

// Rola automaticamente para o fim quando o chat ou as mensagens mudam
onMounted(() => {
    scrollToBottom();
    watch(() => [chat.value, deliveryChat.value], scrollToBottom);
});

// Cancela inscrições e limpa estado no unmount
onUnmounted(() => {
    pusher.unsubscribe(`chat_${props.customerOrderId}`);
    message.value = "";
});
</script>

<style scoped>
.flex-grow {
    overflow-y: auto;
}
</style>
