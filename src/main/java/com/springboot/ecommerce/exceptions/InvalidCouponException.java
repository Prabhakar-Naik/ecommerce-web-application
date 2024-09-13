package com.springboot.ecommerce.exceptions;

/**
 * @author prabhakar, @Date 12-09-2024
 */
public class InvalidCouponException extends RuntimeException{

    public InvalidCouponException(String message){
        super(message);
    }
}
