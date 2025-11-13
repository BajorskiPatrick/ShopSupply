package com.bajorski.billingapp.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String orderId;

    private String customerName;

    private String phoneNumber;

    private List<OrderItemRequest> cartItems;

    private BigDecimal subtotal;

    private BigDecimal tax;

    private BigDecimal grandTotal;

    private String paymentMethod;

}
