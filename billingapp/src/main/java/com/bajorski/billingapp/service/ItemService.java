package com.bajorski.billingapp.service;

import com.bajorski.billingapp.io.ItemRequest;
import com.bajorski.billingapp.io.ItemResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    ItemResponse add(ItemRequest itemRequest, MultipartFile imgFile);

    List<ItemResponse> read();

    void deleteItem(String itemId);
}
