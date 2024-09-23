package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.dto.response.RestaurantLoginResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import br.com.ifsp.ifome.validation.interfaces.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private final TokenService tokenService;
    private final LoginService loginService;
    private final RestaurantRepository restaurantRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidatorService<RestaurantRequest> validatorService;

    public RestaurantService(TokenService tokenService, RestaurantRepository restaurantRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                             List<Validator<RestaurantRequest>> validators, LoginService loginService) {
        this.tokenService = tokenService;
        this.restaurantRepository = restaurantRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validatorService = new ValidatorService<>(validators);
        this.loginService = loginService;
    }
    public RestaurantResponse create(RestaurantRequest restaurantRequest) throws MethodArgumentNotValidException {
        validatorService.isValid(restaurantRequest);
        Restaurant restaurant = new Restaurant(restaurantRequest, bCryptPasswordEncoder);
        restaurant = restaurantRepository.save(restaurant);
        return new RestaurantResponse(restaurant);
    }

    public RestaurantLoginResponse login(LoginRequest loginRequest) {
        Optional<Restaurant> restaurant = restaurantRepository.findByEmail(loginRequest.email());

        loginService.isLoginIncorrect(restaurant, loginRequest.password(), bCryptPasswordEncoder);

        var jwtValue = tokenService.generateToken(restaurant.orElseThrow().getEmail());

        RestaurantResponse restaurantResponse = new RestaurantResponse(restaurant.orElseThrow());

        return new RestaurantLoginResponse(restaurantResponse, jwtValue);
    }

    public void forgotPassword(HttpServletRequest request, String email) {
    }
}
