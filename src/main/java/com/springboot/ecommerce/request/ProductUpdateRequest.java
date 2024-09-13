package com.springboot.ecommerce.request;

import com.springboot.ecommerce.models.Category;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author prabhakar, @Date 08-09-2024
 */
@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
