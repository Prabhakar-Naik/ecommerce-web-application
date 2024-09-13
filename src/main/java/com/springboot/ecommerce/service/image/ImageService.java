package com.springboot.ecommerce.service.image;

import com.springboot.ecommerce.dto.ImageDto;
import com.springboot.ecommerce.models.Images;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author prabhakar, @Date 07-09-2024
 */
public interface ImageService {

    Images getImageById(Long id);

    void deleteImage(Long id);
    List<ImageDto> SaveImages(List<MultipartFile> files, Long productId);

    void updateImage(MultipartFile file, Long imageId);
}
