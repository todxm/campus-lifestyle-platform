package com.tod.campusmarketbackend.service;

import com.tod.campusmarketbackend.entity.HelpTask;
import com.tod.campusmarketbackend.mapper.HelpTaskMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 帮拿任务业务层
 * 负责处理发布、查询、接单、完成和取消等业务逻辑
 */
@Service
public class HelpTaskService {

    private static final String STATUS_WAITING = "待接单";
    private static final String STATUS_ACCEPTED = "已接单";
    private static final String STATUS_FINISHED = "已完成";
    private static final String STATUS_CANCELED = "已取消";

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            STATUS_WAITING,
            STATUS_ACCEPTED,
            STATUS_FINISHED,
            STATUS_CANCELED
    );

    private static final Set<String> ALLOWED_TASK_TYPES = Set.of(
            "外卖代拿",
            "快递代取",
            "打印资料",
            "其他"
    );

    private static final Set<String> ALLOWED_TIME_SLOTS = Set.of(
            "中午",
            "晚上"
    );

    private final HelpTaskMapper helpTaskMapper;

    public HelpTaskService(HelpTaskMapper helpTaskMapper) {
        this.helpTaskMapper = helpTaskMapper;
    }

    /**
     * 查询帮拿任务列表。
     */
    public List<HelpTask> getHelpTasks(String status, String taskType) {
        if (StringUtils.hasText(status)) {
            validateStatus(status);
        }
        if (StringUtils.hasText(taskType)) {
            validateTaskType(taskType);
        }
        return helpTaskMapper.findAll(status, taskType);
    }

    /**
     * 根据 ID 查询帮拿任务详情。
     */
    public HelpTask getHelpTaskById(Long id) {
        return helpTaskMapper.findById(id);
    }

    /**
     * 发布帮拿任务。
     */
    public boolean publishHelpTask(HelpTask helpTask) {
        validatePublishRequest(helpTask);

        LocalDateTime now = LocalDateTime.now();
        helpTask.setStatus(STATUS_WAITING);
        helpTask.setHelperContact(null);
        helpTask.setCreatedTime(now);
        helpTask.setUpdatedTime(now);

        return helpTaskMapper.insert(helpTask) > 0;
    }

    /**
     * 固定互助员接单。
     */
    public boolean acceptHelpTask(Long id, String helperContact) {
        if (!StringUtils.hasText(helperContact)) {
            throw new IllegalArgumentException("接单人联系方式不能为空");
        }
        return helpTaskMapper.acceptIfWaiting(id, helperContact, LocalDateTime.now()) > 0;
    }

    /**
     * 完成帮拿任务。
     */
    public boolean finishHelpTask(Long id) {
        return helpTaskMapper.finishIfAccepted(id, LocalDateTime.now()) > 0;
    }

    /**
     * 取消帮拿任务。
     */
    public boolean cancelHelpTask(Long id) {
        return helpTaskMapper.cancelIfActive(id, LocalDateTime.now()) > 0;
    }

    private void validatePublishRequest(HelpTask helpTask) {
        if (helpTask == null) {
            throw new IllegalArgumentException("请求体不能为空");
        }
        if (!StringUtils.hasText(helpTask.getTaskType())) {
            throw new IllegalArgumentException("任务类型不能为空");
        }
        validateTaskType(helpTask.getTaskType());

        if (!StringUtils.hasText(helpTask.getPickupLocation())) {
            throw new IllegalArgumentException("取件地点不能为空");
        }
        if (!StringUtils.hasText(helpTask.getDeliveryLocation())) {
            throw new IllegalArgumentException("送达地点不能为空");
        }
        if (!StringUtils.hasText(helpTask.getTimeSlot())) {
            throw new IllegalArgumentException("时间段不能为空");
        }
        validateTimeSlot(helpTask.getTimeSlot());

        if (helpTask.getReward() == null) {
            throw new IllegalArgumentException("酬劳不能为空");
        }
        if (helpTask.getReward().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("酬劳不能小于 0");
        }
        if (!StringUtils.hasText(helpTask.getContact())) {
            throw new IllegalArgumentException("联系方式不能为空");
        }
    }

    private void validateStatus(String status) {
        if (!ALLOWED_STATUSES.contains(status)) {
            throw new IllegalArgumentException("任务状态只能是：待接单、已接单、已完成、已取消");
        }
    }

    private void validateTaskType(String taskType) {
        if (!ALLOWED_TASK_TYPES.contains(taskType)) {
            throw new IllegalArgumentException("任务类型只能是：外卖代拿、快递代取、打印资料、其他");
        }
    }

    private void validateTimeSlot(String timeSlot) {
        if (!ALLOWED_TIME_SLOTS.contains(timeSlot)) {
            throw new IllegalArgumentException("时间段只能是：中午、晚上");
        }
    }
}
