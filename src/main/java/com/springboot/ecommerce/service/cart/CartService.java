package com.springboot.ecommerce.service.cart;

import com.springboot.ecommerce.dto.CartDto;
import com.springboot.ecommerce.models.Cart;
import com.springboot.ecommerce.users.model.User;

import java.math.BigDecimal;

/**
 * @author prabhakar, @Date 09-09-2024
 */
public interface CartService {

    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    //Long initializeNewCart();

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);

    CartDto convertToDto(Cart cart);
}
