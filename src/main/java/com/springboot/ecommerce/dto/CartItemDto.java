package com.springboot.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@Data
public class CartItemDto {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
