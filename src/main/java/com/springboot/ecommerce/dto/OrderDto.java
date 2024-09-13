package com.springboot.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalPrice;
    private String status;
    private List<OrderItemDto> items;
}
