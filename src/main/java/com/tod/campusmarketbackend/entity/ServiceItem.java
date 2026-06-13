package com.tod.campusmarketbackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 校园服务实体类
 * 对应数据库中的 service_item 表
 */
@Data
public class ServiceItem {

    // 服务ID，对应数据库 id
    private Long id;

    // 服务名称，对应数据库 name
    private String name;

    // 服务类型，对应数据库 type
    private String type;

    // 服务说明，对应数据库 description
    private String description;

    // 价格说明，对应数据库 price_info
    private String priceInfo;

    // 联系微信，对应数据库 contact_wechat
    private String contactWechat;

    // 服务图片访问地址，对应数据库 image_url
    private String imageUrl;

    // 服务状态，对应数据库 status
    private String status;

    // 发布时间，对应数据库 created_time
    private LocalDateTime createdTime;
}
