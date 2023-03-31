package com.quanht.controller.admin;

import com.quanht.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping(value = "/ecommerce/api/v1/product/{id}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createImage(@PathVariable Long id,
                                         @RequestPart("file[]") MultipartFile[] files){
        return ResponseEntity.ok(imageService.uploadImagesForProduct(id, files));
    }

    @DeleteMapping("/ecommerce/api/v1/image/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Long id){
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }

}
