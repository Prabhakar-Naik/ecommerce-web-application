package com.springboot.ecommerce.request;

import lombok.Data;

/**
 * @author prabhakar, @Date 12-09-2024
 */
@Data
public class PaymentInfo {
    private int amount;
    private String currency;
}
