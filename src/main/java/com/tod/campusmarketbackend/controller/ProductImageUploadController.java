package com.tod.campusmarketbackend.controller;

import com.tod.campusmarketbackend.service.ProductImageStorageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

/**
 * 商品图片上传接口
 * 第一版只支持上传一张商品图片
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = {
        "http://localhost:8081",
        "http://115.159.47.131:8081",
        "http://115.159.47.131"
})
public class ProductImageUploadController {

    private final ProductImageStorageService productImageStorageService;

    public ProductImageUploadController(ProductImageStorageService productImageStorageService) {
        this.productImageStorageService = productImageStorageService;
    }

    /**
     * 上传字段名为 file，成功后返回浏览器可以访问的 imageUrl
     */
    @PostMapping("/product-image")
    public Map<String, String> uploadProductImage(@RequestParam("file") MultipartFile file) {
        String savedFileName = productImageStorageService.store(file);
        String imageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/uploads/product-images/")
                .path(savedFileName)
                .toUriString();

        return Map.of("imageUrl", imageUrl);
    }
}
