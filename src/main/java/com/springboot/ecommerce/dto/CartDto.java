package com.springboot.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@Data
public class CartDto {
    private Long cartId;
    private Set<CartItemDto> items;
    private BigDecimal totalAmount;
}