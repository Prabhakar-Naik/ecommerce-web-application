package com.springboot.ecommerce.exceptions;

/**
 * @author prabhakar, @Date 08-09-2024
 */
public class AlreadyExistException extends RuntimeException{

    public AlreadyExistException(String message){
        super(message);
    }
}
