package com.springboot.ecommerce.exceptions;

/**
 * @author prabhakar, @Date 07-09-2024
 */
public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message) {
       super(message);
    }
}
