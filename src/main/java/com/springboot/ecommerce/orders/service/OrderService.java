package com.springboot.ecommerce.orders.service;

import com.springboot.ecommerce.dto.OrderDto;
import com.springboot.ecommerce.orders.model.Order;

import java.util.List;

/**
 * @author prabhakar, @Date 10-09-2024
 */
public interface OrderService {

    Order placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
