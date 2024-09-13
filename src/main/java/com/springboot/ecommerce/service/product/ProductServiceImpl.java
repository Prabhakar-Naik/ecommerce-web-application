package com.springboot.ecommerce.service.product;

import com.springboot.ecommerce.dto.ImageDto;
import com.springboot.ecommerce.dto.ProductDto;
import com.springboot.ecommerce.exceptions.AlreadyExistException;
import com.springboot.ecommerce.exceptions.ProductNotFoundException;
import com.springboot.ecommerce.models.*;
import com.springboot.ecommerce.repository.*;
import com.springboot.ecommerce.request.AddProductRequest;
import com.springboot.ecommerce.request.ProductUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author prabhakar, @Date 07-09-2024
 * service or component annotation must for bean config
 */
@Service
@RequiredArgsConstructor        // 2nd way
public class ProductServiceImpl implements ProductService{
    /*  // 1st way
    @Autowired
    private ProductRepository productRepository;
    */

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImagesRepository imagesRepository;
    private final ModelMapper modelMapper;

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

   /*   3rd way
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    */

    @Override
    public Product addProduct(AddProductRequest request) throws AlreadyExistException {

        // check if the category is found in database
        // if present set it as a new product category
        // if not then save it as a new category
        // then set as the new product category.

        if (this.productExist(request.getName(),request.getBrand())){
            throw new AlreadyExistException(request.getBrand() + " " + request.getName() + " Already Exists. you may update this product instead.");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return this.categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return this.productRepository.save(createProduct(request,category));
    }


    private boolean productExist(String name, String brand){
        return this.productRepository.existsByNameAndBrand(name,brand);
    }

    private Product createProduct(AddProductRequest request, Category category){

        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );

    }

    @Override
    public Product getProductById(Long id) {
        return this.productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product Not Found."));
    }

   /* @Override
    public void deleteProductById(Long id) {
        this.productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                () -> {throw new ProductNotFoundException("Product Not Found");});
    }*/


    @Override
    public void deleteProductById(Long id) {
        List<CartItem> cartItems = cartItemRepository.findByProductId(id);
        List<OrderItem> orderItems = orderItemRepository.findByProductId(id);
        productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    // Functional approach for category removal
                    Optional.ofNullable(product.getCategory())
                            .ifPresent(category -> category.getProducts().remove(product));
                    product.setCategory(null);

                    // Functional approach for updating cart items
                    cartItems.stream()
                            .peek(cartItem -> cartItem.setProduct(null))
                            .peek(CartItem::setTotalPrice)
                            .forEach(cartItemRepository::save);

                    // Functional approach for updating order items
                    orderItems.stream()
                            .peek(orderItem -> orderItem.setProduct(null))
                            .forEach(orderItemRepository::save);

                    productRepository.delete(product);
                }, () -> {
                    throw new EntityNotFoundException("Product not found!");
                });
    }

    @Override
    public Product updateProductById(ProductUpdateRequest request, Long product_Id) {
        return this.productRepository.findById(product_Id)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(this.productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = this.categoryRepository.findByName(request.getCategory().getName());

        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategoryName(String categoryName) {
        return this.productRepository.findByCategoryName(categoryName);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return this.productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryNameAndBrand(String categoryName, String brand) {
        return this.productRepository.findByCategoryNameAndBrand(categoryName,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return this.productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return this.productRepository.countByBrandAndName(brand,name);
    }


    @Override
    public List<Product> findDistinctProductsByName() {
        List<Product> products = productRepository.findAll();
        Map<String, Product> distinctProductsMap = products.stream()
                .collect(Collectors.toMap(
                        Product::getName,
                        product -> product,
                        (existing, replacement) -> existing));
        return new ArrayList<>(distinctProductsMap.values());
    }

    @Override
    public List<String> getAllDistinctBrands() {
        return productRepository.findAll().stream()
                .map(Product::getBrand)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
    ProductDto productDto = this.modelMapper.map(product,ProductDto.class);
    List<Images> images = this.imagesRepository.findByProductId(product.getId());
    List<ImageDto> imageDtos = images.stream()
            .map(image -> this.modelMapper.map(image,ImageDto.class))
            .toList();
    productDto.setImages(imageDtos);
    return productDto;
    }
}
