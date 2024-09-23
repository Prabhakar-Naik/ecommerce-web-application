package com.springboot.ecommerce.cart.service;

import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.cart.model.Cart;
import com.springboot.ecommerce.cart.model.CartItem;
import com.springboot.ecommerce.products.model.Product;
import com.springboot.ecommerce.cart.repository.CartItemRepository;
import com.springboot.ecommerce.cart.repository.CartRepository;
import com.springboot.ecommerce.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author prabhakar, @Date 09-09-2024
 */
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService{

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartService cartService;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1. get the cart
        // 2. get the product
        // 3. check if the product already in the cart
        // 4. if yes, then increase the quantity with requested quantity
        // 5. if no, the initiate a new cartItem entry

        Cart cart = this.cartService.getCart(cartId);
        Product product = this.productService.getProductById(productId);

        System.out.println("\n\n=====================================================================================");
        System.out.println("The product Id:" + productId);
        System.out.println("The product:" + product);
        System.out.println("\n\n=====================================================================================");

        CartItem cartItem = cart.getItems()
                .stream().filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if (cartItem.getId() == null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());

        }else {
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        this.cartItemRepository.save(cartItem);
        this.cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = this.cartService.getCart(cartId);
        /*CartItem itemToRemove = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceAccessException("Product Not Found!"));*/
        // or
        CartItem itemToRemove = this.getCartItem(cartId,productId);
        cart.removeItem(itemToRemove);
        this.cartRepository.save(cart);

    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = this.cartService.getCart(cartId);
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item ->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItems()
                .stream().map(CartItem ::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //cart.setTotalAmount(totalAmount.add(BigDecimal.valueOf(quantity)));
        cart.setTotalAmount(totalAmount);
        this.cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart = this.cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item Not Found!"));

    }



}
