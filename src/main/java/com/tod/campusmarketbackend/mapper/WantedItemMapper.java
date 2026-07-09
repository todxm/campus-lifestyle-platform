package com.tod.campusmarketbackend.mapper;

import com.tod.campusmarketbackend.entity.WantedItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * WantedItem Mapper 接口
 * 定义求购墙相关数据库操作
 */
@Mapper
public interface WantedItemMapper {

    // 查询求购列表，可按分类和状态筛选
    List<WantedItem> findAll(@Param("category") String category,
                             @Param("status") String status);

    // 根据 ID 查询求购详情
    WantedItem findById(@Param("id") Long id);

    // 插入新求购
    int insert(WantedItem wantedItem);

    // 标记已找到
    int markFoundById(@Param("id") Long id,
                      @Param("updatedTime") LocalDateTime updatedTime);

    // 关闭求购
    int closeById(@Param("id") Long id,
                  @Param("updatedTime") LocalDateTime updatedTime);

    // 软删除求购
    int deleteById(@Param("id") Long id,
                   @Param("updatedTime") LocalDateTime updatedTime);
}
