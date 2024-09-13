package com.springboot.ecommerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author prabhakar, @Date 11-09-2024
 */
@Data
public class LoginRequest {
    @NotBlank(message = "Invalid credentials")
    private String email;
    @NotBlank(message = "Invalid credentials")
    private String password;

}
