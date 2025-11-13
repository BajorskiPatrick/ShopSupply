package com.bajorski.billingapp.service;

import com.bajorski.billingapp.io.OrderRequest;
import com.stripe.exception.StripeException;

public interface StripeService {
    String createCheckoutSession(OrderRequest orderRequest, String internalOrderId) throws StripeException;
}