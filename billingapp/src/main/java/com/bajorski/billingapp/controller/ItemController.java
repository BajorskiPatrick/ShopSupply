package com.bajorski.billingapp.controller;

import com.bajorski.billingapp.io.ItemRequest;
import com.bajorski.billingapp.io.ItemResponse;
import com.bajorski.billingapp.service.ItemService;
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
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/admin/items")
    public ResponseEntity<ItemResponse> addItem(@RequestPart("item") String itemString, @RequestPart("file") MultipartFile imgFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemRequest itemRequest = null;
        try {
            itemRequest = objectMapper.readValue(itemString, ItemRequest.class);

            ItemResponse newItem = itemService.add(itemRequest, imgFile);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newItem.getItemId())
                    .toUri();

            return ResponseEntity.created(location).body(newItem);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while processing item json request");
        }
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponse>> fetchItems() {
        return ResponseEntity.ok(itemService.read());
    }

    @DeleteMapping("/admin/items/{id}")
    public ResponseEntity<Void> removeItem(@PathVariable String id) {
        try {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
