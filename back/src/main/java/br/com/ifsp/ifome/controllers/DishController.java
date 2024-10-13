package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocCreateDish;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.DishRequest;
import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.services.DishService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

@MultipartConfig
@RestController
@RequestMapping("/api/dish")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService){
        this.dishService = dishService;
    }

    @DocCreateDish
    @Transactional
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> create(
        @RequestPart("file") MultipartFile multipartFile,
        @Valid @RequestPart("dish")DishRequest dishRequest,
        Principal principal, UriComponentsBuilder ucb)
        throws IOException, MethodArgumentNotValidException{
        DishResponse dishResponse = dishService.create(dishRequest, multipartFile, principal);

        URI locationOfNewDish = ucb
            .path("dish/{id}")
            .buildAndExpand(dishResponse.id())
            .toUri();

        ApiResponse apiResponse = new ApiResponse("success", dishResponse, "Prato cadastrado com sucesso!");
        return ResponseEntity.created(locationOfNewDish).body(apiResponse);
    }
}
