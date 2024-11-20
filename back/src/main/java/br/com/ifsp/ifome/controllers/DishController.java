package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocCreateDish;
import br.com.ifsp.ifome.docs.DocsGetAllDish;
import br.com.ifsp.ifome.docs.DocsGetPaginationDish;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.DishRequest;
import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.services.DishService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

//@MultipartConfig
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
        DishResponse dishResponse = dishService.create(dishRequest, multipartFile, principal.getName());

        URI locationOfNewDish = ucb
            .path("dish/{id}")
            .buildAndExpand(dishResponse.id())
            .toUri();

        ApiResponse apiResponse = new ApiResponse("success", dishResponse, "Prato cadastrado com sucesso!");
        return ResponseEntity.created(locationOfNewDish).body(apiResponse);
    }

    @GetMapping(path = "/")
    @DocsGetPaginationDish
    public ResponseEntity<ApiResponse> getAllAvailableDishWithPagination(
        @Parameter(hidden = true) @PageableDefault(size = 15, page = 0) Pageable pageable) {
       var dishResponses = this.dishService.getAllAvailable(pageable);

        ApiResponse apiResponse = new ApiResponse("success", dishResponses, "Busca por prato concluida");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAllDishById(@PathVariable Long id) {
        var dishResponses = this.dishService.getAvailableDishById(id);

        ApiResponse apiResponse = new ApiResponse("success", dishResponses, "Busca por prato concluida");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<ApiResponse> getAllAvailableDishByIdRestaurant(@PathVariable Long id) {
        var dishResponses = this.dishService.getAllAvailableById(id);

        ApiResponse apiResponse = new ApiResponse("success", dishResponses, "Busca por prato concluida");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/all")
    @DocsGetAllDish
    public ResponseEntity<ApiResponse> getAllAvailableDish() {
        var dishResponses = this.dishService.getAllAvailable();

        ApiResponse apiResponse = new ApiResponse("success", dishResponses, "Busca por prato concluida");
        return ResponseEntity.ok(apiResponse);
    }
}
