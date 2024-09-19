package br.com.ifsp.ifome.dto.response;

public record RestaurantLoginResponse(RestaurantResponse restaurant, String token) {
}
