<template>
    <div id="map" class="w-full h-[calc(100vh_-_100px)]"></div>
</template>

<script setup>
import { Loader } from '@googlemaps/js-api-loader';
import { onMounted, ref, defineProps, watch, computed } from 'vue';
import deliveryBikeIcon from '@/assets/delivery-bike.png';
import restaurantIcon from '@/assets/restaurant.png';
import clientIcon from '@/assets/house.png';
import { usePedidoStore } from '@/stores/usePedidoStore';

const pedidoStore = usePedidoStore();

const restaurantLocation = computed(() => pedidoStore.restaurantLocation);
const clientLocation = computed(() => pedidoStore.clientLocation);

watch(restaurantLocation, () => updateMapMarkers());
watch(clientLocation, () => updateMapMarkers());

const loader = new Loader({
    apiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY,
    version: 'weekly',
    libraries: ['places']
});

const currentLocation = ref({ lat: -23.439250, lng: -46.536766 });
let map;
let markers = []; // Armazena os marcadores ativos para facilitar a limpeza
let bounds;

// Função para adicionar um marcador e salvar na lista de marcadores
const addMarker = (position, icon) => {
    const latLngPosition = {
        lat: parseFloat(position.lat),
        lng: parseFloat(position.lng)
    };
    const marker = new google.maps.Marker({
        position: latLngPosition,
        map,
        icon: {
            url: icon,
            scaledSize: new google.maps.Size(50, 50),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(25, 50),
        }
    });
    markers.push(marker);
    bounds.extend(latLngPosition);
};

// Função para limpar todos os marcadores do mapa
const clearMarkers = () => {
    markers.forEach(marker => marker.setMap(null));
    markers = [];
};

// Função para atualizar os marcadores com base no status
const updateMapMarkers = () => {
    clearMarkers(); // Limpa marcadores antigos
    bounds = new google.maps.LatLngBounds();

    if (currentStatus.value === 'buscando') {
        // Apenas centraliza no entregador ou na posição padrão
        addMarker(currentLocation.value, deliveryBikeIcon); // Marcador de entregador
        map.setCenter(currentLocation.value);
        map.setZoom(15);
    } else if (currentStatus.value === 'NOVO') {
        // Mostrar restaurante e cliente
        if (restaurantLocation.value) addMarker(restaurantLocation.value, restaurantIcon);
        if (clientLocation.value) addMarker(clientLocation.value, clientIcon);
    } else if (['ACEITO', 'EM_PREPARO', 'NO_LOCAL', 'PRONTO'].includes(currentStatus.value)) {
        // Mostrar restaurante e entregador
        if (restaurantLocation.value) addMarker(restaurantLocation.value, restaurantIcon);
        addMarker(currentLocation.value, deliveryBikeIcon); // Entregador
    } else if (currentStatus.value === 'A_CAMINHO') {
        // Mostrar cliente e entregador
        if (clientLocation.value) addMarker(clientLocation.value, clientIcon);
        addMarker(currentLocation.value, deliveryBikeIcon); // Entregador
    } else if (currentStatus.value === 'CONCLUIDO') {
        // Mostrar apenas a localização do entregador
        map.setCenter(currentLocation.value);
        map.setZoom(15);
        addMarker(currentLocation.value, deliveryBikeIcon); // Entregador
    }

    // Ajusta os limites do mapa para mostrar os marcadores
    if (markers.length > 1) {
        map.fitBounds(bounds);
    } else if (markers.length === 1) {
        map.setCenter(markers[0].getPosition());
        map.setZoom(15);
    }
};

// Obter a localização atual do entregador
const getCurrentLocation = () => {
    return new Promise((resolve) => {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                resolve({
                    lat: position.coords.latitude,
                    lng: position.coords.longitude
                });
            },
            (error) => {
                console.error("Erro ao obter localização:", error);
                resolve(currentLocation.value); // Resolve com a localização padrão se falhar
            }
        );
    });
};

onMounted(async () => {
    // Aguarda o carregamento completo da biblioteca do Google Maps
    await loader.load();

    currentLocation.value = await getCurrentLocation();

    // Inicializa o mapa após garantir que `google` está disponível
    map = new google.maps.Map(document.getElementById('map'), {
        center: currentLocation.value,
        zoom: 15,
        disableDefaultUI: true,
        draggable: false,
        scrollwheel: false,
        disableDoubleClickZoom: true,
        keyboardShortcuts: false,
    });

    // Inicializa os marcadores com base no status atual
    updateMapMarkers();
});

// Computed para obter o status atual
const currentStatus = computed(() => {
    if (Array.isArray(pedidoStore.status) && pedidoStore.status.length > 0) {
        let status = pedidoStore.status.length > 0 ? pedidoStore.status[pedidoStore.status.length - 1].orderDeliveryStatus : 'buscando'
        if (status === 'CANCELADO' || status === 'CONCLUIDO') {
            pedidoStore.resetOrderData();
            status = 'buscando';
        }
        return status;
    }
    return 'buscando';
});

// Watch para monitorar mudanças na localização do entregador
watch(currentLocation, () => {
    if (['ACEITO', 'EM_PREPARO', 'NO_LOCAL', 'PRONTO', 'A_CAMINHO'].includes(currentStatus.value)) {
        updateMapMarkers();
    }
});
</script>
