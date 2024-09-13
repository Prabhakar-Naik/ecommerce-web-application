package com.springboot.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;
}
