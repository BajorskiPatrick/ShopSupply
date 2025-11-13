package com.bajorski.billingapp.controller;

import com.bajorski.billingapp.io.OrderRequest;
import com.bajorski.billingapp.io.OrderResponse;
import com.bajorski.billingapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse createdOrder = orderService.createOrder(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdOrder.getOrderId())
                .toUri();

        return ResponseEntity.created(location).body(createdOrder);
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/orders/my-orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders() {
        try {
            List<OrderResponse> order = orderService.getUserOrders();
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        try {
            OrderResponse order = orderService.getOrder(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
         try {
             orderService.deleteOrder(orderId);
             return ResponseEntity.noContent().build();
         } catch (RuntimeException e) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
         }
    }

}
