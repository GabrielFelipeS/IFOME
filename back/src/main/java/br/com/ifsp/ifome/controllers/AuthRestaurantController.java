package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocsCreateRestaurant;
import br.com.ifsp.ifome.docs.DocsRestaurantLogin;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.dto.response.RestaurantLoginResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.services.FileStorageService;
import br.com.ifsp.ifome.services.RestaurantService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController

@MultipartConfig
@RequestMapping("/api/auth/restaurant")
public class AuthRestaurantController {
    private final RestaurantService restaurantService;
    private final FileStorageService fileStorageService;

    public AuthRestaurantController(RestaurantService restaurantService, FileStorageService fileStorageService){
        this.restaurantService = restaurantService;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    @DocsCreateRestaurant
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> create(
        @RequestPart("file")  MultipartFile multipartFile,
        @Parameter(description = "Metadados em formato JSON", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantRequest.class)))
        @Valid @RequestPart("restaurant") RestaurantRequest restaurantRequest,
        UriComponentsBuilder ucb)
        throws IOException, MethodArgumentNotValidException {

        RestaurantResponse restaurantResponse = restaurantService.create(restaurantRequest, multipartFile);

        URI locationOfNewRestaurant = ucb
            .path("restaurant/{id}")
            .buildAndExpand(restaurantResponse.id())
            .toUri();
        ApiResponse apiResponse = new ApiResponse("success", restaurantResponse, "Restaurante cadastrado com sucesso");
        return ResponseEntity.created(locationOfNewRestaurant).body(apiResponse);
    }

    @PostMapping("/login")
    @DocsRestaurantLogin
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest restaurantLogin) {
        RestaurantLoginResponse restaurantLoginResponse = restaurantService.login(restaurantLogin);
        ApiResponse apiResponse = new ApiResponse("success", restaurantLoginResponse, "Restaurante logado com sucesso");
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("forgot-password")
    public void forgotPassword(HttpServletRequest request, @RequestBody @Valid @Email String email) throws Exception{
        System.err.println(request.getServerName());
        restaurantService.forgotPassword(request, email);
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody @Valid @Email String email) throws Exception{

    }

}
