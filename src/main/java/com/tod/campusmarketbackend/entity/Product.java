package com.tod.campusmarketbackend.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 对应数据库中的 product 表
 */
@Data
public class Product {

    // 商品ID，对应数据库 id
    private Long id;

    // 商品标题，对应数据库 title
    private String title;

    // 商品描述，对应数据库 description
    private String description;

    // 商品价格，对应数据库 price
    private BigDecimal price;

    // 商品分类，对应数据库 category
    private String category;

    // 新旧程度，对应数据库 condition_level
    private String conditionLevel;

    // 卖家微信，对应数据库 contact_wechat
    private String contactWechat;

    // 商品状态，对应数据库 status
    private String status;

    // 发布时间，对应数据库 created_time
    private LocalDateTime createdTime;
}