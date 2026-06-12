package com.tod.campusmarketbackend.controller;

import com.tod.campusmarketbackend.entity.Product;
import com.tod.campusmarketbackend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品接口控制器
 * 负责接收前端或浏览器发来的商品相关请求
 */
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = {
        "http://localhost:8081",
        "http://115.159.47.131:8081",
        "http://115.159.47.131"
})
public class ProductController {

    private final ProductService productService;

    /**
     * 构造方法注入 ProductService
     * Spring Boot 会自动把 ProductService 传进来
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 查询商品列表
     * 访问地址：GET http://localhost:8080/products
     */
    @GetMapping
    public List<Product> listProducts() {
        return productService.getProductList();
    }

    /**
     * 查询商品详情
     * 访问地址：GET http://localhost:8080/products/1
     */
    @GetMapping("/{id}")
    public Product getProductDetail(@PathVariable Long id) {
        return productService.getProductDetail(id);
    }

    /**
     * 发布商品
     * 访问地址：POST http://localhost:8080/products
     */
    @PostMapping
    public String publishProduct(@RequestBody Product product) {
        boolean success = productService.publishProduct(product);

        if (success) {
            return "商品发布成功";
        } else {
            return "商品发布失败";
        }
    }

    /**
     * 下架商品
     * 访问地址：PUT http://localhost:8080/products/{id}/offline
     */
    @PutMapping("/{id}/offline")
    public String offlineProduct(@PathVariable Long id) {
        boolean success = productService.offlineProduct(id);

        if (success) {
            return "商品下架成功";
        } else {
            return "商品下架失败";
        }
    }

    /**
     * 搜索/筛选商品
     * 访问示例：
     * GET http://localhost:8080/products/search?keyword=台灯
     * GET http://localhost:8080/products/search?category=数码
     * GET http://localhost:8080/products/search?keyword=台灯&category=宿舍用品
     */
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        return productService.searchProducts(keyword, category);
    }
}
