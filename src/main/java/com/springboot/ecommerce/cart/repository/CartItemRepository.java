package com.springboot.ecommerce.cart.repository;

import com.springboot.ecommerce.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author prabhakar, @Date 09-09-2024
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    void deleteAllByCartId(Long id);

    List<CartItem> findByProductId(Long id);
}
