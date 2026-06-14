package com.tod.campusmarketbackend.mapper;

import com.tod.campusmarketbackend.entity.ServiceItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * ServiceItem Mapper 接口
 * 定义数据库操作方法
 */
@Mapper
public interface ServiceItemMapper {

    // 查询所有服务
    List<ServiceItem> findAll();

    // 根据 ID 查询服务
    ServiceItem findById(@Param("id") Long id);

    // 插入新服务
    int insert(ServiceItem serviceItem);

    // 根据关键词和类型搜索
    List<ServiceItem> search(@Param("keyword") String keyword,
                             @Param("type") String type);

    // 下架服务
    int offlineById(@Param("id") Long id);

    // 记录一次用户点击联系按钮
    int incrementContactClickCount(@Param("id") Long id);
}
