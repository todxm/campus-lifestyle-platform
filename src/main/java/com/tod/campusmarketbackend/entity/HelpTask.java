package com.tod.campusmarketbackend.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 帮拿任务实体类
 * 对应数据库中的 help_task 表
 */
@Data
public class HelpTask {

    // 帮拿任务 ID
    private Long id;

    // 任务类型：外卖代拿、快递代取、打印资料、其他
    private String taskType;

    // 取件地点，例如校门口、菜鸟驿站、打印店
    private String pickupLocation;

    // 送达地点，例如宿舍楼、教学楼
    private String deliveryLocation;

    // 具体位置说明，例如校门口外卖架到 3 栋 302
    private String detailLocation;

    // 时间段：中午、晚上
    private String timeSlot;

    // 酬劳金额
    private BigDecimal reward;

    // 发布人联系方式
    private String contact;

    // 备注
    private String remark;

    // 任务状态：待接单、已接单、已完成、已取消
    private String status;

    // 接单人联系方式
    private String helperContact;

    // 创建时间
    private LocalDateTime createdTime;

    // 更新时间
    private LocalDateTime updatedTime;
}
