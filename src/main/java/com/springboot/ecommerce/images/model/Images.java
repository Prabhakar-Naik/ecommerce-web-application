package com.springboot.ecommerce.images.model;

import com.springboot.ecommerce.products.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

/**
 * @author prabhakar, @Date 07-09-2024
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @Lob
    private Blob image;
    private String downloadUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
