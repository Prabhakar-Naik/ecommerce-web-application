package com.springboot.ecommerce.cart.repository;

import com.springboot.ecommerce.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author prabhakar, @Date 09-09-2024
 */
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserId(Long userId);
}
