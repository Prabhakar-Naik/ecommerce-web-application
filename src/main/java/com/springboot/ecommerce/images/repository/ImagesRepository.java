package com.springboot.ecommerce.images.repository;

import com.springboot.ecommerce.images.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author prabhakar, @Date 08-09-2024
 */
public interface ImagesRepository extends JpaRepository<Images, Long> {
    List<Images> findByProductId(Long id);
}
