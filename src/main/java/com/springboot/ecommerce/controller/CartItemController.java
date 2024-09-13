package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.models.Cart;
import com.springboot.ecommerce.models.User;
import com.springboot.ecommerce.response.ApiResponse;
import com.springboot.ecommerce.service.cart.CartItemService;
import com.springboot.ecommerce.service.cart.CartService;
import com.springboot.ecommerce.service.user.UserService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@RestController
@RequestMapping(value = "${api.prefix}/cartItems")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/addItem")
    public ResponseEntity<ApiResponse> addItemToCart(/*@RequestParam(required = false) Long cartId,*/
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            /*if (cartId == null) {
                cartId= cartService.initializeNewCart();
            }*/
            //User user = this.userService.getUserById(2L);
            User user = this.userService.getAuthenticatedUser();
            Cart cart = this.cartService.initializeNewCart(user);
            this.cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item added to cart successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (JwtException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping(value = "removeItem/{cartId}/{productId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                          @PathVariable Long productId){
        try {
            this.cartItemService.removeItemFromCart(cartId,productId);
            return ResponseEntity.ok(new ApiResponse("Remove Item Success",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping(value = "/updateItemQuantityBy/{cartId}/{productId}")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long productId,
                                                          @RequestParam Integer quantity){
        try {
            this.cartItemService.updateItemQuantity(cartId,productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Update item Quantity",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }
}
