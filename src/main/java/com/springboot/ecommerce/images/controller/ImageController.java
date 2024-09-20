package com.springboot.ecommerce.images.controller;

import com.springboot.ecommerce.dto.ImageDto;
import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.images.model.Images;
import com.springboot.ecommerce.response.ApiResponse;
import com.springboot.ecommerce.images.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author prabhakar, @Date 08-09-2024
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    private final ImageService imageService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,
                                                  @RequestParam Long productId){
        try {
            List<ImageDto> imageDtos = imageService.SaveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Images Uploaded successfully!", imageDtos));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload Failed",e.getMessage()));
        }
    }


    @GetMapping(value = "/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImages(@PathVariable Long imageId) throws SQLException {
            Images image = this.imageService.getImageById(imageId);
            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\""+image.getFileName()+"\"")
                    .body(resource);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,
                                                   @RequestBody MultipartFile file){
        try {
            Images image = imageService.getImageById(imageId);
            if (image != null){
                imageService.updateImage(file,imageId);
                return ResponseEntity.ok(new ApiResponse("Update Success", null));
            }
        }catch (ResourceNotFoundException exception){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update Failed!",INTERNAL_SERVER_ERROR));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try {
            Images image = imageService.getImageById(imageId);
            if (image != null){
                imageService.deleteImage(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete Success", null));
            }
        }catch (ResourceNotFoundException exception){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete Failed!",INTERNAL_SERVER_ERROR));
    }
}
