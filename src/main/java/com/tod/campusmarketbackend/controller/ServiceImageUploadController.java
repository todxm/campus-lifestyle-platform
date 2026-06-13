package com.tod.campusmarketbackend.controller;

import com.tod.campusmarketbackend.service.ServiceImageStorageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

/**
 * 校园服务图片上传接口
 * 第一版只支持上传一张服务图片
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = {
        "http://localhost:8081",
        "http://115.159.47.131:8081",
        "http://115.159.47.131"
})
public class ServiceImageUploadController {

    private final ServiceImageStorageService serviceImageStorageService;

    public ServiceImageUploadController(ServiceImageStorageService serviceImageStorageService) {
        this.serviceImageStorageService = serviceImageStorageService;
    }

    /**
     * 上传字段名为 file，成功后返回浏览器可以访问的 imageUrl
     */
    @PostMapping("/service-image")
    public Map<String, String> uploadServiceImage(@RequestParam("file") MultipartFile file) {
        String savedFileName = serviceImageStorageService.store(file);
        String imageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/uploads/service-images/")
                .path(savedFileName)
                .toUriString();

        return Map.of("imageUrl", imageUrl);
    }
}
