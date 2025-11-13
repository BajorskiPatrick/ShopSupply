package com.bajorski.billingapp.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVerificationRequest {

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String razorpaySignature;

    private String orderId;
}
