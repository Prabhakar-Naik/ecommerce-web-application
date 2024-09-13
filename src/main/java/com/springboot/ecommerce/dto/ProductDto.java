package com.springboot.ecommerce.dto;

import com.springboot.ecommerce.models.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author prabhakar, @Date 09-09-2024
 */
@Data
public class ProductDto {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
    private List<ImageDto> images;
}
