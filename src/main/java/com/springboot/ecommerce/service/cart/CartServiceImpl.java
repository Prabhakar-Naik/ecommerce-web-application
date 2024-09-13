package com.springboot.ecommerce.service.cart;

import com.springboot.ecommerce.dto.CartDto;
import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.models.Cart;
import com.springboot.ecommerce.models.User;
import com.springboot.ecommerce.repository.CartItemRepository;
import com.springboot.ecommerce.repository.CartRepository;
import com.springboot.ecommerce.service.user.UserService;
import com.springboot.ecommerce.service.user.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author prabhakar, @Date 09-09-2024
 */
@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final ModelMapper modelMapper;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    private final UserService userService;


    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ModelMapper modelMapper, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public Cart getCart(Long cartId) {
        Cart cart = this.cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return this.cartRepository.save(cart);
    }


    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        this.cartRepository.deleteById(id);
        this.cartItemRepository.deleteAllByCartId(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        /*return cart.getCartIItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);*/
        return cart.getTotalAmount();
    }

//    @Override
//    public Long initializeNewCart() {
//        Cart newCart = new Cart();
//        Long newCartId = this.cartIdGenerator.incrementAndGet();
//        newCart.setId(newCartId);
//        return this.cartRepository.save(newCart).getId();
//
//    }

    @Override
    public Cart initializeNewCart(User user){
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return this.cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return this.cartRepository.findByUserId(userId);
    }

    @Override
    public CartDto convertToDto(Cart cart){
        return modelMapper.map(cart,CartDto.class);
    }

}
