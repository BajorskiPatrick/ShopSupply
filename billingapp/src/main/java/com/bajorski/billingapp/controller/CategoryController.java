package com.bajorski.billingapp.controller;

import com.bajorski.billingapp.io.CategoryRequest;
import com.bajorski.billingapp.io.CategoryResponse;
import com.bajorski.billingapp.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryResponse> addCategory(
            @RequestPart("category") String categoryString, @RequestPart("file") MultipartFile file
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryRequest request = null;
        try {
            request = objectMapper.readValue(categoryString, CategoryRequest.class);

            if (request.getName() == null || request.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            CategoryResponse newCategory = categoryService.add(request, file);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newCategory.getCategoryId())
                    .toUri();

            return ResponseEntity.created(location).body(newCategory);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exception occured while passing the json: " + e.getMessage());
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> fetchCategories() {
        List<CategoryResponse> categories = categoryService.read();

        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> remove(@PathVariable String categoryId) {
        try {
            categoryService.delete(categoryId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
