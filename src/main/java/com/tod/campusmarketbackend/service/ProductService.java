package com.tod.campusmarketbackend.service;

import com.tod.campusmarketbackend.entity.Product;
import com.tod.campusmarketbackend.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品业务层
 * 负责处理商品相关的业务逻辑
 */
@Service
public class ProductService {

    private final ProductMapper productMapper;

    /**
     * 构造方法注入 ProductMapper
     * Spring Boot 会自动把 ProductMapper 传进来
     */
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /**
     * 查询商品列表
     */
    public List<Product> getProductList() {
        return productMapper.findAll();
    }

    /**
     * 根据商品ID查询商品详情
     */
    public Product getProductDetail(Long id) {
        return productMapper.findById(id);
    }

    /**
     * 发布商品
     */
    public boolean publishProduct(Product product) {
        // 新发布的商品默认设置为在售状态
        product.setStatus("ON_SALE");

        // 设置发布时间为当前时间
        product.setCreatedTime(java.time.LocalDateTime.now());

        // 调用 Mapper 把商品插入数据库
        int rows = productMapper.insert(product);

        // 如果影响行数大于 0，说明插入成功
        return rows > 0;
    }

    /**
     * 下架商品
     */
    public boolean offlineProduct(Long id) {
        int rows = productMapper.offlineById(id);
        return rows > 0;
    }

    /**
     * 搜索商品
     */
    public List<Product> searchProducts(String keyword, String category) {
        return productMapper.search(keyword, category);
    }
}