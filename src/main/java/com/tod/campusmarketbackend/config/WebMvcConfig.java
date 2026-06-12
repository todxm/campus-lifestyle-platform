package com.tod.campusmarketbackend.config;

import com.tod.campusmarketbackend.service.ProductImageStorageService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源访问配置
 * 把浏览器访问路径映射到代码目录之外的商品图片目录
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ProductImageStorageService productImageStorageService;

    public WebMvcConfig(ProductImageStorageService productImageStorageService) {
        this.productImageStorageService = productImageStorageService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadLocation = productImageStorageService
                .getUploadDirectory()
                .toUri()
                .toString();

        registry.addResourceHandler("/uploads/product-images/**")
                .addResourceLocations(uploadLocation);
    }
}
