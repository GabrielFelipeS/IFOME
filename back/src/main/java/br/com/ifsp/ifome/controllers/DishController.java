package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.DishRequest;
import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.services.DishService;
import br.com.ifsp.ifome.services.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/dish")
public class DishController {
    private final DishService dishService;
    private final FileStorageService fileStorageService;

    public DishController(DishService dishService, FileStorageService fileStorageService){
        this.dishService = dishService;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<DishResponse> create(
            @Valid @RequestPart("dish")DishRequest dishRequest, UriComponentsBuilder ucb)
        throws IOException, MethodArgumentNotValidException{
        DishResponse dishResponse = dishService.create(dishRequest);

        URI locationOfNewDish = ucb
                .path("dish/{id}")
                .buildAndExpand(dishResponse.id())
                .toUri();
        return ResponseEntity.created(locationOfNewDish).body(dishResponse);
    }
}
