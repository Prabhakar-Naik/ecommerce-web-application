package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author prabhakar, @Date 07-09-2024
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    //@Query("SELECT 1 FROM Category WHERE name = ?1") // no need
    boolean existsByName(String name);
}
