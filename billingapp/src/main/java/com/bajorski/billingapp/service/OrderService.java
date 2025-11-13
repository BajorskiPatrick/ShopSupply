package com.bajorski.billingapp.service;

import com.bajorski.billingapp.io.OrderRequest;
import com.bajorski.billingapp.io.OrderResponse;
import com.bajorski.billingapp.io.PaymentVerificationRequest;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    List<OrderResponse> getLatestOrders();

    void deleteOrder(String orderId);

    void fulfillOrder(String orderId, String paymentIntentId);
}
