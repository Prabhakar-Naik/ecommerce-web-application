package com.springboot.ecommerce.dto;

import lombok.Data;

import java.util.List;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
