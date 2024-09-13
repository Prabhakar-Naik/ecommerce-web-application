package com.springboot.ecommerce.service.cart;

import com.springboot.ecommerce.models.CartItem;

/**
 * @author prabhakar, @Date 09-09-2024
 */
public interface CartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);

    void  updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);

}
