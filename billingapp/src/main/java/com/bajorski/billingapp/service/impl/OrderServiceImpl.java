package com.bajorski.billingapp.service.impl;

import com.bajorski.billingapp.entity.OrderEntity;
import com.bajorski.billingapp.entity.OrderItemEntity;
import com.bajorski.billingapp.io.*;
import com.bajorski.billingapp.repository.OrderEntityRepository;
import com.bajorski.billingapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderEntityRepository orderEntityRepository;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        OrderEntity newOrder = convertToOrderEntity(request);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setStatus(
                newOrder.getPaymentMethod() == PaymentMethod.CASH ?
                        PaymentDetails.PaymentStatus.COMPLETED : PaymentDetails.PaymentStatus.PENDING
        );
        newOrder.setPaymentDetails(paymentDetails);

        List<OrderItemEntity> orderItems = request.getCartItems().stream()
                .map(this::convertToOrderItemEntity)
                .toList();
        newOrder.setItems(orderItems);

        newOrder = orderEntityRepository.save(newOrder);

        return convertToResponse(newOrder);
    }

    private OrderResponse convertToResponse(OrderEntity order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerName(order.getCustomerName())
                .phoneNumber(order.getPhoneNumber())
                .subtotal(order.getSubtotal())
                .tax(order.getTax())
                .grandTotal(order.getGrandTotal())
                .paymentMethod(order.getPaymentMethod())
                .items(order.getItems().stream()
                        .map(this::convertToItemResponse)
                        .toList()
                )
                .paymentDetails(order.getPaymentDetails())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private OrderItemResponse convertToItemResponse(OrderItemEntity orderItemEntity) {
        return OrderItemResponse.builder()
                .itemId(orderItemEntity.getItemId())
                .name(orderItemEntity.getName())
                .price(orderItemEntity.getPrice())
                .quantity(orderItemEntity.getQuantity())
                .build();
    }

    private OrderItemEntity convertToOrderItemEntity(OrderItemRequest request) {
        return OrderItemEntity.builder()
                .itemId(request.getItemId())
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();
    }

    private OrderEntity convertToOrderEntity(OrderRequest request) {
        return OrderEntity.builder()
                .orderId("ORD" + System.currentTimeMillis())
                .customerName(request.getCustomerName())
                .phoneNumber(request.getPhoneNumber())
                .subtotal(request.getSubtotal())
                .tax(request.getTax())
                .grandTotal(request.getGrandTotal())
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public List<OrderResponse> getLatestOrders() {
        return orderEntityRepository.findAllByOrderByCreatedAtAsc().stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrder(String orderId) {
        OrderEntity retrievedOrder = orderEntityRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return convertToResponse(retrievedOrder);
    }

    @Override
    public void deleteOrder(String orderId) {
        OrderEntity order = orderEntityRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderEntityRepository.delete(order);
    }

    @Override
    public void fulfillOrder(String orderId, String paymentIntentId) {
        log.info("Fulfilling order: {}", orderId);

        OrderEntity order = orderEntityRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        if (order.getPaymentDetails().getStatus() == PaymentDetails.PaymentStatus.COMPLETED) {
            log.warn("Order {} already fulfilled.", orderId);
            return;
        }

        PaymentDetails paymentDetails = order.getPaymentDetails();

        paymentDetails.setPaymentTransactionId(paymentIntentId);

        paymentDetails.setStatus(PaymentDetails.PaymentStatus.COMPLETED);

        order.setPaymentDetails(paymentDetails);
        orderEntityRepository.save(order);

        log.info("Order {} marked as COMPLETED.", orderId);
    }
}
