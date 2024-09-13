package com.springboot.ecommerce.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prabhakar, @Date 11-09-2024
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse {
    private Long id;
    private String token;

}
