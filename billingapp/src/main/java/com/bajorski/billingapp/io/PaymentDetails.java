package com.bajorski.billingapp.io;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {

    private String paymentTransactionId; // Tutaj będziemy przechowywać np. ID PaymentIntent ze Stripe

    private PaymentStatus status;

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED
    }
}
