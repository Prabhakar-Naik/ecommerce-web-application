package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author prabhakar, @Date 10-09-2024
 */
public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findByUserId(Long userId);
}
