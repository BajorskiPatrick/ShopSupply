package com.bajorski.billingapp.service.impl;

import com.bajorski.billingapp.entity.CategoryEntity;
import com.bajorski.billingapp.io.CategoryRequest;
import com.bajorski.billingapp.io.CategoryResponse;
import com.bajorski.billingapp.repository.CategoryRepository;
import com.bajorski.billingapp.repository.ItemRepository;
import com.bajorski.billingapp.service.CategoryService;
import com.bajorski.billingapp.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final FileUploadService fileUploadService;

    private final ItemRepository itemRepository;

    @Override
    public CategoryResponse add(CategoryRequest request, MultipartFile file) {
        String imgUrl = fileUploadService.uploadFile(file);
        CategoryEntity newCategory = convertToEntity(request);

        newCategory.setImgUrl(imgUrl);
        newCategory = categoryRepository.save(newCategory);

        return convertToResponse(newCategory);
    }

    @Override
    public List<CategoryResponse> read() {
        return categoryRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public void delete(String categoryId) {
        CategoryEntity existingCategory = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        fileUploadService.deleteFile(existingCategory.getImgUrl());
        categoryRepository.delete(existingCategory);
    }

    private CategoryEntity convertToEntity(CategoryRequest request) {
        return CategoryEntity.builder()
                .categoryId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .bgColor(request.getBgColor())
                .build();
    }

    private CategoryResponse convertToResponse(CategoryEntity newCategory) {
        return CategoryResponse.builder()
                .categoryId(newCategory.getCategoryId())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgColor(newCategory.getBgColor())
                .imgUrl(newCategory.getImgUrl())
                .itemsCount(itemRepository.countByCategoryId(newCategory.getId()))
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .build();
    }
}
