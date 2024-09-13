package com.springboot.ecommerce.exceptions;

/**
 * @author prabhakar, @Date 08-09-2024
 */
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
