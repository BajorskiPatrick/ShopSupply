package com.bajorski.billingapp.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private String orderId;

    private String customerName;

    private String phoneNumber;

    private List<OrderItemResponse> items;

    private BigDecimal subtotal;

    private BigDecimal tax;

    private BigDecimal grandTotal;

    private PaymentMethod paymentMethod;

    private PaymentDetails paymentDetails;

    private LocalDateTime createdAt;

}
