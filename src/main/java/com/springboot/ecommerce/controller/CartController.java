package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.dto.CartDto;
import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.models.Cart;
import com.springboot.ecommerce.response.ApiResponse;
import com.springboot.ecommerce.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService service) {
        this.cartService = service;
    }


    @GetMapping(value = "/getCartById/{cartId}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = this.cartService.getCart(cartId);
            CartDto cartDto = this.cartService.convertToDto(cart);
            return ResponseEntity.ok(new ApiResponse("Success", cartDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}/my-cart")
    public ResponseEntity<ApiResponse> getUserCart( @PathVariable Long userId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);
            CartDto cartDto = cartService.convertToDto(cart);
            return ResponseEntity.ok(new ApiResponse("Success", cartDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping(value = "/clearCartById/{cartId}")
    public ResponseEntity<ApiResponse> clearTheCart(@PathVariable Long cartId) {
        this.cartService.clearCart(cartId);
        return ResponseEntity.ok(new ApiResponse("Clear Cart Success", null));
    }

    @GetMapping(value = "/getTotalAmountById/{cartId}")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId){
        try {
            BigDecimal totalPrice = this.cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("Total Price: ",totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


}
