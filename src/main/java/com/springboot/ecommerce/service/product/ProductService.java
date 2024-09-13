package com.springboot.ecommerce.service.product;

import com.springboot.ecommerce.dto.ProductDto;
import com.springboot.ecommerce.exceptions.AlreadyExistException;
import com.springboot.ecommerce.models.Product;
import com.springboot.ecommerce.request.AddProductRequest;
import com.springboot.ecommerce.request.ProductUpdateRequest;

import java.util.List;

/**
 * @author prabhakar, @Date 07-09-2024
 */
public interface ProductService {

    Product addProduct(AddProductRequest request) throws AlreadyExistException;

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProductById(ProductUpdateRequest product, Long product_Id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategoryName(String categoryName);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryNameAndBrand(String categoryName, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String brand, String name);


    Long countProductsByBrandAndName(String brand, String name);

    List<Product> findDistinctProductsByName();

    List<String> getAllDistinctBrands();

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
