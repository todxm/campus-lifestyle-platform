package com.tod.campusmarketbackend.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 求购信息实体类
 * 对应数据库中的 wanted_item 表
 */
@Data
public class WantedItem {

    // 求购 ID
    private Long id;

    // 求购标题，例如：求一个床上桌
    private String title;

    // 分类：宿舍用品、教材资料、数码配件、生活用品、其他
    private String category;

    // 最低预算，可为空
    private BigDecimal budgetMin;

    // 最高预算，可为空
    private BigDecimal budgetMax;

    // 需求说明
    private String description;

    // 发布人联系方式
    private String contact;

    // 急需程度：不急、这两天、今天就要
    private String urgency;

    // 状态：求购中、已找到、已关闭、已删除
    private String status;

    // 创建时间
    private LocalDateTime createdTime;

    // 更新时间
    private LocalDateTime updatedTime;
}
