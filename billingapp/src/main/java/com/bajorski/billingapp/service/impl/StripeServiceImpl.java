package com.bajorski.billingapp.service.impl;

import com.bajorski.billingapp.io.OrderItemRequest;
import com.bajorski.billingapp.io.OrderRequest;
import com.bajorski.billingapp.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.key.secret}")
    private String stripeKeySecret;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKeySecret;
    }

    @Override
    public String createCheckoutSession(OrderRequest orderRequest, String internalOrderId) throws StripeException {

        String successUrl = "http://localhost:5173/explore?payment=success";
        String cancelUrl = "http://localhost:5173/explore?payment=cancelled";

        List<SessionCreateParams.LineItem> lineItems = orderRequest.getCartItems().stream()
                .map(this::convertCartItemToLineItem)
                .collect(Collectors.toList());

        long taxInCents = orderRequest.getTax().multiply(BigDecimal.valueOf(100)).longValue();
        if (taxInCents > 0) {
            lineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("usd")
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName("Tax")
                                                            .build()
                                            )
                                            .setUnitAmount(taxInCents)
                                            .build()
                            )
                            .setQuantity(1L)
                            .build()
            );
        }

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(successUrl)
                        .setCancelUrl(cancelUrl)
                        .addAllLineItem(lineItems)
                        .setClientReferenceId(internalOrderId)
                        .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    private SessionCreateParams.LineItem convertCartItemToLineItem(OrderItemRequest item) {
        long priceInCents = item.getPrice().multiply(BigDecimal.valueOf(100)).longValue();

        return SessionCreateParams.LineItem.builder()
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(item.getName())
                                                .build()
                                )
                                .setUnitAmount(priceInCents)
                                .build()
                )
                .setQuantity(item.getQuantity().longValue())
                .build();
    }
}