package com.springboot.ecommerce.category.repository;

import com.springboot.ecommerce.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author prabhakar, @Date 07-09-2024
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    //@Query("SELECT 1 FROM Category WHERE name = ?1") // no need
    boolean existsByName(String name);
}
