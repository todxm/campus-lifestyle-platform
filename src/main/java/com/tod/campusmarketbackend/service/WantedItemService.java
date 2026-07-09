package com.tod.campusmarketbackend.service;

import com.tod.campusmarketbackend.entity.WantedItem;
import com.tod.campusmarketbackend.mapper.WantedItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 求购墙业务层
 * 负责处理求购发布、查询、标记已找到、关闭和软删除逻辑
 */
@Service
public class WantedItemService {

    private static final String STATUS_WANTED = "求购中";
    private static final String STATUS_FOUND = "已找到";
    private static final String STATUS_CLOSED = "已关闭";
    private static final String STATUS_DELETED = "已删除";

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            STATUS_WANTED,
            STATUS_FOUND,
            STATUS_CLOSED,
            STATUS_DELETED
    );

    private static final Set<String> ALLOWED_CATEGORIES = Set.of(
            "宿舍用品",
            "教材资料",
            "数码配件",
            "生活用品",
            "其他"
    );

    private static final Set<String> ALLOWED_URGENCIES = Set.of(
            "不急",
            "这两天",
            "今天就要"
    );

    private final WantedItemMapper wantedItemMapper;

    public WantedItemService(WantedItemMapper wantedItemMapper) {
        this.wantedItemMapper = wantedItemMapper;
    }

    /**
     * 查询求购列表。
     * 不传 status 时，Mapper 默认排除“已删除”的求购。
     */
    public List<WantedItem> getWantedItems(String category, String status) {
        if (StringUtils.hasText(category)) {
            validateCategory(category);
        }
        if (StringUtils.hasText(status)) {
            validateStatus(status);
        }
        return wantedItemMapper.findAll(category, status);
    }

    /**
     * 根据 ID 查询求购详情。
     */
    public WantedItem getWantedItemById(Long id) {
        return wantedItemMapper.findById(id);
    }

    /**
     * 发布求购。
     */
    public boolean publishWantedItem(WantedItem wantedItem) {
        validatePublishRequest(wantedItem);

        LocalDateTime now = LocalDateTime.now();
        wantedItem.setStatus(STATUS_WANTED);
        wantedItem.setCreatedTime(now);
        wantedItem.setUpdatedTime(now);

        return wantedItemMapper.insert(wantedItem) > 0;
    }

    /**
     * 标记已找到。
     */
    public boolean markFound(Long id) {
        return wantedItemMapper.markFoundById(id, LocalDateTime.now()) > 0;
    }

    /**
     * 关闭求购。
     */
    public boolean closeWantedItem(Long id) {
        return wantedItemMapper.closeById(id, LocalDateTime.now()) > 0;
    }

    /**
     * 软删除求购，不物理删除数据。
     */
    public boolean deleteWantedItem(Long id) {
        return wantedItemMapper.deleteById(id, LocalDateTime.now()) > 0;
    }

    private void validatePublishRequest(WantedItem wantedItem) {
        if (wantedItem == null) {
            throw new IllegalArgumentException("请求体不能为空");
        }
        if (!StringUtils.hasText(wantedItem.getTitle())) {
            throw new IllegalArgumentException("求购标题不能为空");
        }
        if (!StringUtils.hasText(wantedItem.getCategory())) {
            throw new IllegalArgumentException("求购分类不能为空");
        }
        validateCategory(wantedItem.getCategory());

        validateBudget(wantedItem.getBudgetMin(), wantedItem.getBudgetMax());

        if (!StringUtils.hasText(wantedItem.getDescription())) {
            throw new IllegalArgumentException("需求说明不能为空");
        }
        if (!StringUtils.hasText(wantedItem.getContact())) {
            throw new IllegalArgumentException("联系方式不能为空");
        }
        if (!StringUtils.hasText(wantedItem.getUrgency())) {
            throw new IllegalArgumentException("急需程度不能为空");
        }
        validateUrgency(wantedItem.getUrgency());
    }

    private void validateBudget(BigDecimal budgetMin, BigDecimal budgetMax) {
        if (budgetMin != null && budgetMin.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("最低预算不能小于 0");
        }
        if (budgetMax != null && budgetMax.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("最高预算不能小于 0");
        }
        if (budgetMin != null && budgetMax != null && budgetMax.compareTo(budgetMin) < 0) {
            throw new IllegalArgumentException("最高预算不能小于最低预算");
        }
    }

    private void validateCategory(String category) {
        if (!ALLOWED_CATEGORIES.contains(category)) {
            throw new IllegalArgumentException("求购分类只能是：宿舍用品、教材资料、数码配件、生活用品、其他");
        }
    }

    private void validateUrgency(String urgency) {
        if (!ALLOWED_URGENCIES.contains(urgency)) {
            throw new IllegalArgumentException("急需程度只能是：不急、这两天、今天就要");
        }
    }

    private void validateStatus(String status) {
        if (!ALLOWED_STATUSES.contains(status)) {
            throw new IllegalArgumentException("求购状态只能是：求购中、已找到、已关闭、已删除");
        }
    }
}
