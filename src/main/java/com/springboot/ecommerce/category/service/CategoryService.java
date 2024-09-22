package com.springboot.ecommerce.category.service;

import com.springboot.ecommerce.category.model.Category;

import java.util.List;

/**
 * @author prabhakar, @Date 07-09-2024
 */
public interface CategoryService {

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category addCategory(Category category);

    Category updateCategory(Category category, Long category_id);

    void deleteCategoryById(Long id);

}
