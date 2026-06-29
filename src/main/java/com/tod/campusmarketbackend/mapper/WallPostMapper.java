package com.tod.campusmarketbackend.mapper;

import com.tod.campusmarketbackend.entity.WallPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * WallPost Mapper 接口
 * 定义校园墙帖子相关数据库操作
 */
@Mapper
public interface WallPostMapper {

    // 查询校园墙帖子列表，可按类型和状态筛选
    List<WallPost> findAll(@Param("postType") String postType,
                           @Param("status") String status);

    // 根据 ID 查询帖子详情
    WallPost findById(@Param("id") Long id);

    // 插入新帖子
    int insert(WallPost wallPost);

    // 关闭帖子
    int closeById(@Param("id") Long id,
                  @Param("updatedTime") LocalDateTime updatedTime);

    // 软删除帖子
    int deleteById(@Param("id") Long id,
                   @Param("updatedTime") LocalDateTime updatedTime);
}
