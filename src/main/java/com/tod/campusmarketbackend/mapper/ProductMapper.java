package com.tod.campusmarketbackend.mapper;

import com.tod.campusmarketbackend.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 商品数据库操作接口
 */
@Mapper
public interface ProductMapper {

    /**
     * 查询所有在售商品
     */
    List<Product> findAll();

    /**
     * 根据商品ID查询详情
     */
    Product findById(Long id);

    /**
     * 新增商品
     */
    int insert(Product product);

    /**
     * 根据商品ID下架商品
     */
    int offlineById(Long id);

    /**
     * 根据关键词和分类搜索商品
     */
    List<Product> search(String keyword, String category);
}

