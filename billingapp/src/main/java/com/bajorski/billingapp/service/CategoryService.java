package com.bajorski.billingapp.service;

import com.bajorski.billingapp.io.CategoryRequest;
import com.bajorski.billingapp.io.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    CategoryResponse add(CategoryRequest request, MultipartFile file);

    List<CategoryResponse> read();

    void delete(String categoryId);
}
