package com.springboot.ecommerce.category.controller;

import com.springboot.ecommerce.exceptions.AlreadyExistException;
import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.models.Category;
import com.springboot.ecommerce.response.ApiResponse;
import com.springboot.ecommerce.service.category.CategoryService;
import com.springboot.ecommerce.service.category.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * @author prabhakar, @Date 08-09-2024
 */
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryServiceImpl service){
        this.categoryService = service;
    }


    @GetMapping(value = "/getAllCategories")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = this.categoryService.getAllCategories();

            return ResponseEntity.ok(new ApiResponse("Found", categories));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:",INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping(value = "/addCategory")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        try {
            Category theCategory = this.categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Success",theCategory));
        } catch (AlreadyExistException exception) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(exception.getMessage(),null));
        }

    }

    @GetMapping(value = "/getCategoryById/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category category = this.categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found:",category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping(value = "/getCategoryByName/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category category = this.categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found:",category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping(value = "/deleteCategoryById/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try {
            this.categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted:",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PutMapping(value = "/updateCategoryById/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id,@RequestBody Category category){
        try {
            Category updatedCategory = this.categoryService.updateCategory(category,id);
            return ResponseEntity.ok(new ApiResponse("Updated:",updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
