package com.springboot.ecommerce.service.order;

import com.springboot.ecommerce.dto.OrderDto;
import com.springboot.ecommerce.enums.OrderStatus;
import com.springboot.ecommerce.cart.model.Cart;
import com.springboot.ecommerce.models.Order;
import com.springboot.ecommerce.models.OrderItem;
import com.springboot.ecommerce.models.Product;
import com.springboot.ecommerce.repository.OrderRepository;
import com.springboot.ecommerce.repository.ProductRepository;
import com.springboot.ecommerce.cart.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = this.cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order,cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalPrice(calculateTotalAmount(orderItems));

        Order savedOrder = this.orderRepository.save(order);
        this.cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        // set the user
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            this.productRepository.save(product);

            return new OrderItem(order,product,cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();
    }

    public BigDecimal calculateTotalAmount(List<OrderItem> orderItems){
        return orderItems.stream().map(item -> item.getPrice()
                .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return this.orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceAccessException("No Order Found!"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        return this.orderRepository.findByUserId(userId)
                .stream().map(this :: convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }

}
