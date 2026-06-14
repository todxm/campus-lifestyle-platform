package com.tod.campusmarketbackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 校园服务实体类，对应数据库中的 service_item 表。
 */
@Data
public class ServiceItem {

    // 服务 ID
    private Long id;

    // 服务名称
    private String name;

    // 原有服务类型，用于列表筛选
    private String type;

    // 服务说明
    private String description;

    // 价格说明
    private String priceInfo;

    // 商家或服务提供者微信
    private String contactWechat;

    // 服务图片访问地址
    private String imageUrl;

    // 服务主体类型
    private String merchantType;

    // 服务细分类别
    private String serviceCategory;

    // 是否为推荐服务：0 表示普通，1 表示推荐
    private Integer isFeatured;

    // 是否为认证商家：0 表示未认证，1 表示已认证
    private Integer isVerified;

    // 推广标签，例如“热门”“新生优惠”
    private String promotionLabel;

    // 联系方式模式：DIRECT、PLATFORM 或 CODE
    private String contactMode;

    // 联系商家时使用的咨询暗号
    private String contactCode;

    // 平台商业号联系方式
    private String platformContact;

    // 用户点击联系按钮的累计次数
    private Integer contactClickCount;

    // 服务状态
    private String status;

    // 发布时间
    private LocalDateTime createdTime;
}
