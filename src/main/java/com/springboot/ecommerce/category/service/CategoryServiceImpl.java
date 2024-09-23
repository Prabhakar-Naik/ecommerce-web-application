package com.springboot.ecommerce.category.service;

import com.springboot.ecommerce.exceptions.AlreadyExistException;
import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.category.model.Category;
import com.springboot.ecommerce.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author prabhakar, @Date 07-09-2024
 */
@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(Long id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return this.categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(cat ->
                        !this.categoryRepository.existsByName(cat.getName()))
                .map(this.categoryRepository::save)
                .orElseThrow(() -> new AlreadyExistException(category.getName() + " already exists."));
    }

    @Override
    public Category updateCategory(Category category, Long category_id) {
        return Optional.ofNullable(getCategoryById(category_id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return this.categoryRepository.save(oldCategory);
                }).orElseThrow(() -> new ResourceNotFoundException("Category Not Found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        this.categoryRepository.findById(id).ifPresentOrElse(
                this.categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category Not Found!");
                });
    }
}
