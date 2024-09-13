package com.springboot.ecommerce.request;

import com.springboot.ecommerce.models.Category;
import com.springboot.ecommerce.models.Images;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author prabhakar, @Date 07-09-2024
 */
@Data
public class AddProductRequest {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
    private List<Images> images;
}
