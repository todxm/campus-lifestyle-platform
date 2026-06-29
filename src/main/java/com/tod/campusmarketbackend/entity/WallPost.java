package com.tod.campusmarketbackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 校园墙帖子实体类
 * 对应数据库中的 wall_post 表
 */
@Data
public class WallPost {

    // 帖子 ID
    private Long id;

    // 帖子类型：失物招领、新生提问、校园动态
    private String postType;

    // 标题
    private String title;

    // 内容
    private String content;

    // 地点，可为空
    private String location;

    // 联系方式，可为空
    private String contact;

    // 标签，例如急寻、待认领、新生求助、校园提醒
    private String tag;

    // 状态：已发布、已关闭、已删除
    private String status;

    // 创建时间
    private LocalDateTime createdTime;

    // 更新时间
    private LocalDateTime updatedTime;
}
