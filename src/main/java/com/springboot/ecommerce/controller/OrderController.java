package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.dto.OrderDto;
import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.orders.model.Order;
import com.springboot.ecommerce.response.ApiResponse;
import com.springboot.ecommerce.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

   /* @Autowired
    private PaymentService paymentService;*/

    @PostMapping(value = "/placeOrder/{userId}")
    public ResponseEntity<ApiResponse> createOrder(@PathVariable Long userId) {
        try {
            Order order = this.orderService.placeOrder(userId);
            OrderDto orderDto = this.orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Items Order Success", orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error occurred: " , e.getMessage()));
        }
    }


    @GetMapping(value = "/getOrderById/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order = this.orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Your Order: ", order));
        } catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("OOPS! ",exception.getMessage()));
        }
    }


    @GetMapping(value = "/getUserOrdersByUserId/{userId}")
    public ResponseEntity<ApiResponse> getUserOrdersByUserId(@PathVariable Long userId) {
        try {
            List<OrderDto> orders = this.orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Your Order: ", orders));
        } catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("OOPS! ",exception.getMessage()));
        }
    }

    /*@PostMapping("/payment/create-payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {
        try {
            System.out.println("The body :" +paymentInfo);
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfo);
            String paymentString = paymentIntent.toJson();
            System.out.println("The payment string :" + paymentString);
            return ResponseEntity.ok(paymentString);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }*/


}
