package com.reader.image.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.reader.image.service.ImageService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ImageResources {
    
    private ImageService imageService;

    
    public ImageResources(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping("/image")
    public ResponseEntity<String> getTextFromImg(@RequestParam("image") MultipartFile image){
        return ResponseEntity.ok(imageService.getStringFromImage(image));
    }
}
