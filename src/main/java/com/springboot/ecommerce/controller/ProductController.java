package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.dto.ProductDto;
import com.springboot.ecommerce.exceptions.AlreadyExistException;
import com.springboot.ecommerce.exceptions.ProductNotFoundException;
import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.models.Product;
import com.springboot.ecommerce.request.AddProductRequest;
import com.springboot.ecommerce.request.ProductUpdateRequest;
import com.springboot.ecommerce.response.ApiResponse;
import com.springboot.ecommerce.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * @author prabhakar, @Date 08-09-2024
 */
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/getAllProducts")
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products = this.productService.getAllProducts();
        List<ProductDto> convertedProducts = this.productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Success: ",convertedProducts));
    }

    @GetMapping(value = "/getProductById/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        try {
            Product product = this.productService.getProductById(id);
            var productDto = this.productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Success: ", productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/addProduct")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request){
        try {
            Product product = this.productService.addProduct(request);
            var productDto = this.productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Add Product Success",productDto));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/updateProductById/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request){
        try {
            Product product = this.productService.updateProductById(request,id);
            var productDto = this.productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Update Product Success!.",productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteProductById/{id}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long id){
        try {
            this.productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Delete Success!",null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping(value = "/getProductByBrandAndName/{brand}/{name}")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@PathVariable String brand,
                                                                @PathVariable String name){
        try {
            List<Product> products = this.productService.getProductsByBrandAndName(brand,name);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found: ",null));
            }
            List<ProductDto> convertedProducts = this.productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping(value = "/getProductByCategoryAndBrand/{categoryName}/{brand}")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@PathVariable String categoryName,
                                                                    @PathVariable String brand){
        try {
            List<Product> products = this.productService.getProductsByCategoryNameAndBrand(categoryName,brand);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found: ",null));
            }
            List<ProductDto> convertedProducts = this.productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping(value = "/getProductByName/{name}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name){
        try {
            List<Product> products = this.productService.getProductsByName(name);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found: ",null));
            }
            List<ProductDto> convertedProducts = this.productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success!",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping(value = "/getProductByBrand/{brand}")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String brand){
        try {
            List<Product> products = this.productService.getProductsByBrand(brand);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found: ",null));
            }
            List<ProductDto> convertedProducts = this.productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success!",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping(value = "/getProductByCategory/{category}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category){
        try {
            List<Product> products = this.productService.getProductsByCategoryName(category);

            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found: ",null));
            }
            List<ProductDto> convertedProducts = this.productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success!",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping(value = "/countProductByBrandAndName")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try {
            var countProducts = this.productService.countProductsByBrandAndName(brand,name);
            return ResponseEntity.ok(new ApiResponse("Product Count!",countProducts));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping("/distinct/products")
    public ResponseEntity<ApiResponse> getDistinctProductsByCategory() {
        try {
            List<Product> products = productService.findDistinctProductsByName();
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return  ResponseEntity.ok(new ApiResponse("success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    // New end point 2
    @GetMapping("/distinct/brands")
    public ResponseEntity<ApiResponse> getAllDistinctBrands() {
        try {
            List<String> brands = productService.getAllDistinctBrands();
            return  ResponseEntity.ok(new ApiResponse("success", brands));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }





}
