package com.springboot.ecommerce.dto;

import lombok.Data;

/**
 * @author prabhakar, @Date 08-09-2024
 */

@Data
public class ImageDto {
    private Long id;
    private String fileName;
    private String downloadUrl;
}
