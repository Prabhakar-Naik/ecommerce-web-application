package com.springboot.ecommerce.request;

import lombok.Data;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
