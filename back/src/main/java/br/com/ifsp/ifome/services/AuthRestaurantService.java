package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.aspect.SensitiveData;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.dto.response.RestaurantLoginResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import br.com.ifsp.ifome.validation.interfaces.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AuthRestaurantService {
    private final TokenService tokenService;
    private final LoginService loginService;
    private final RestaurantRepository restaurantRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidatorService<RestaurantRequest> validatorService;
    private final FileStorageService fileStorageService;
    private final AddressCoordinatesService addressCoordinatesService;

    public AuthRestaurantService(TokenService tokenService, RestaurantRepository restaurantRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                                 List<Validator<RestaurantRequest>> validators, LoginService loginService, FileStorageService fileStorageService, AddressCoordinatesService addressCoordinatesService) {
        this.tokenService = tokenService;
        this.restaurantRepository = restaurantRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validatorService = new ValidatorService<>(validators);
        this.loginService = loginService;
        this.fileStorageService = fileStorageService;
        this.addressCoordinatesService = addressCoordinatesService;
    }

    @SensitiveData
    public RestaurantResponse create(RestaurantRequest restaurantRequest, MultipartFile multipartFile) throws MethodArgumentNotValidException, IOException {
        validatorService.isValid(restaurantRequest);

        String imageUrl = fileStorageService.storeFile(restaurantRequest.cnpj(), multipartFile);

        Address address = addressCoordinatesService.createAddressWithCoordinates(restaurantRequest.address());

        Restaurant restaurant = new Restaurant(restaurantRequest, address, bCryptPasswordEncoder, imageUrl);

        restaurant = restaurantRepository.save(restaurant);
        return RestaurantResponse.from(restaurant);
    }


    @SensitiveData
    public RestaurantLoginResponse login(LoginRequest loginRequest) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByEmail(loginRequest.email());

        loginService.isLoginIncorrect(restaurantOptional, loginRequest.password(), bCryptPasswordEncoder);

        var restaurant = restaurantOptional.orElseThrow();
        var jwtValue = tokenService.generateToken(restaurant.getEmail(), restaurant.getAuthorities());

        RestaurantResponse restaurantResponse = RestaurantResponse.from(restaurant);

        return new RestaurantLoginResponse(restaurantResponse, jwtValue);
    }

    public void forgotPassword(HttpServletRequest request, String email) {
    }
}
