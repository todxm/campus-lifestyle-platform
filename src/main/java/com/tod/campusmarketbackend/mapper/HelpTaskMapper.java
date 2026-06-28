package com.tod.campusmarketbackend.mapper;

import com.tod.campusmarketbackend.entity.HelpTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * HelpTask Mapper 接口
 * 定义帮拿任务相关数据库操作
 */
@Mapper
public interface HelpTaskMapper {

    // 查询帮拿任务列表，可按状态和任务类型筛选
    List<HelpTask> findAll(@Param("status") String status,
                           @Param("taskType") String taskType);

    // 根据 ID 查询帮拿任务详情
    HelpTask findById(@Param("id") Long id);

    // 插入新帮拿任务
    int insert(HelpTask helpTask);

    // 接单：只有待接单任务可以接单
    int acceptIfWaiting(@Param("id") Long id,
                        @Param("helperContact") String helperContact,
                        @Param("updatedTime") LocalDateTime updatedTime);

    // 完成：只有已接单任务可以完成
    int finishIfAccepted(@Param("id") Long id,
                         @Param("updatedTime") LocalDateTime updatedTime);

    // 取消：待接单或已接单任务可以取消
    int cancelIfActive(@Param("id") Long id,
                       @Param("updatedTime") LocalDateTime updatedTime);
}
