package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author prabhakar, @Date 10-09-2024
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByProductId(Long id);
}
