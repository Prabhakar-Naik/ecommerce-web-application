package com.springboot.ecommerce.service.image;

import com.springboot.ecommerce.dto.ImageDto;
import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.models.Images;
import com.springboot.ecommerce.models.Product;
import com.springboot.ecommerce.repository.ImagesRepository;
import com.springboot.ecommerce.service.product.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author prabhakar, @Date 07-09-2024
 */
@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ImagesRepository imagesRepository;


    @Override
    public Images getImageById(Long id) {
        return this.imagesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Images found with "+id));
    }

    @Override
    public void deleteImage(Long id) {
        this.imagesRepository.findById(id).ifPresentOrElse(
                this.imagesRepository::delete,
                () -> {throw new ResourceNotFoundException("No Images found with "+id);});
    }

    @Override
    public List<ImageDto> SaveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file: files){
            try {
                Images image = new Images();
                image.setFileType(file.getContentType());
                image.setFileName(file.getOriginalFilename());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";

                String downloadUrl = buildDownloadUrl+image.getId();
                image.setDownloadUrl(downloadUrl);
                Images savedImage = this.imagesRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
                this.imagesRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Images image = this.getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            this.imagesRepository.save(image);

        }catch (IOException | SQLException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }
}
