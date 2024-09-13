package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.models.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author prabhakar, @Date 08-09-2024
 */
public interface ImagesRepository extends JpaRepository<Images, Long> {
    List<Images> findByProductId(Long id);
}
