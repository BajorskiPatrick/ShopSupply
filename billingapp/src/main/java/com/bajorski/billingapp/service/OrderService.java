package com.bajorski.billingapp.service;

import com.bajorski.billingapp.io.OrderRequest;
import com.bajorski.billingapp.io.OrderResponse;
import com.bajorski.billingapp.io.PaymentVerificationRequest;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    List<OrderResponse> getLatestOrders();

    OrderResponse getOrder(String orderId);

    void deleteOrder(String orderId);

    void fulfillOrder(String orderId, String paymentIntentId);
}
