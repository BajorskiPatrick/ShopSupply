package com.bajorski.billingapp.service.impl;

import com.bajorski.billingapp.entity.CategoryEntity;
import com.bajorski.billingapp.entity.ItemEntity;
import com.bajorski.billingapp.io.ItemRequest;
import com.bajorski.billingapp.io.ItemResponse;
import com.bajorski.billingapp.repository.CategoryRepository;
import com.bajorski.billingapp.repository.ItemRepository;
import com.bajorski.billingapp.service.FileUploadService;
import com.bajorski.billingapp.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final FileUploadService fileUploadService;

    private final CategoryRepository categoryRepository;

    private final ItemRepository itemRepository;

    @Override
    public ItemResponse add(ItemRequest itemRequest, MultipartFile imgFile) {
        String imgUrl = fileUploadService.uploadFile(imgFile);
        ItemEntity newItem = convertToEntity(itemRequest);
        CategoryEntity existingCategory = categoryRepository.findByCategoryId(itemRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + itemRequest.getCategoryId()));
        newItem.setCategory(existingCategory);
        newItem.setImgUrl(imgUrl);

        newItem = itemRepository.save(newItem);

        return convertToResponse(newItem);
    }

    @Override
    public List<ItemResponse> read() {
        return itemRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public void deleteItem(String itemId) {
        ItemEntity existingItem = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found: " + itemId));

        boolean isFileDeleted = fileUploadService.deleteFile(existingItem.getImgUrl());
        if (!isFileDeleted) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete item image file");
        }

        itemRepository.delete(existingItem);
    }

    private ItemEntity convertToEntity(ItemRequest itemRequest) {
        return ItemEntity.builder()
                .itemId(UUID.randomUUID().toString())
                .name(itemRequest.getName())
                .price(itemRequest.getPrice())
                .description(itemRequest.getDescription())
                .build();
    }

    private ItemResponse convertToResponse(ItemEntity newItem) {
        return ItemResponse.builder()
                .itemId(newItem.getItemId())
                .name(newItem.getName())
                .price(newItem.getPrice())
                .description(newItem.getDescription())
                .imgUrl(newItem.getImgUrl())
                .categoryId(newItem.getCategory().getCategoryId())
                .categoryName(newItem.getCategory().getName())
                .createdAt(newItem.getCreatedAt())
                .updatedAt(newItem.getUpdatedAt())
                .build();
    }
}
