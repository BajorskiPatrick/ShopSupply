package com.bajorski.billingapp.service;

import com.bajorski.billingapp.io.OrderRequest;
import com.bajorski.billingapp.io.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getUserOrders();

    OrderResponse getOrder(String orderId);

    void deleteOrder(String orderId);

    void fulfillOrder(String orderId, String paymentIntentId);
}
