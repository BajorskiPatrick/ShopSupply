package com.bajorski.billingapp.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RazorpayOrderResponse {

    private String id;

    private String entity;

    private Integer amount;

    private String currency;

    private String status;

    private Date createdAt;

    private String receipt;
}
