package com.tod.campusmarketbackend.service;

import com.tod.campusmarketbackend.entity.WallPost;
import com.tod.campusmarketbackend.mapper.WallPostMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 校园墙业务层
 * 负责处理帖子发布、查询、关闭和软删除逻辑
 */
@Service
public class WallPostService {

    private static final String STATUS_PUBLISHED = "已发布";
    private static final String STATUS_CLOSED = "已关闭";
    private static final String STATUS_DELETED = "已删除";

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            STATUS_PUBLISHED,
            STATUS_CLOSED,
            STATUS_DELETED
    );

    private static final Set<String> ALLOWED_POST_TYPES = Set.of(
            "失物招领",
            "新生提问",
            "校园动态"
    );

    private final WallPostMapper wallPostMapper;

    public WallPostService(WallPostMapper wallPostMapper) {
        this.wallPostMapper = wallPostMapper;
    }

    /**
     * 查询校园墙帖子列表。
     * 不传 status 时，Mapper 默认排除“已删除”的帖子。
     */
    public List<WallPost> getWallPosts(String postType, String status) {
        if (StringUtils.hasText(postType)) {
            validatePostType(postType);
        }
        if (StringUtils.hasText(status)) {
            validateStatus(status);
        }
        return wallPostMapper.findAll(postType, status);
    }

    /**
     * 根据 ID 查询帖子详情。
     */
    public WallPost getWallPostById(Long id) {
        return wallPostMapper.findById(id);
    }

    /**
     * 发布校园墙帖子。
     */
    public boolean publishWallPost(WallPost wallPost) {
        validatePublishRequest(wallPost);

        LocalDateTime now = LocalDateTime.now();
        wallPost.setStatus(STATUS_PUBLISHED);
        wallPost.setCreatedTime(now);
        wallPost.setUpdatedTime(now);

        return wallPostMapper.insert(wallPost) > 0;
    }

    /**
     * 关闭帖子。
     */
    public boolean closeWallPost(Long id) {
        return wallPostMapper.closeById(id, LocalDateTime.now()) > 0;
    }

    /**
     * 软删除帖子，不物理删除数据。
     */
    public boolean deleteWallPost(Long id) {
        return wallPostMapper.deleteById(id, LocalDateTime.now()) > 0;
    }

    private void validatePublishRequest(WallPost wallPost) {
        if (wallPost == null) {
            throw new IllegalArgumentException("请求体不能为空");
        }
        if (!StringUtils.hasText(wallPost.getPostType())) {
            throw new IllegalArgumentException("帖子类型不能为空");
        }
        validatePostType(wallPost.getPostType());

        if (!StringUtils.hasText(wallPost.getTitle())) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (!StringUtils.hasText(wallPost.getContent())) {
            throw new IllegalArgumentException("内容不能为空");
        }
    }

    private void validatePostType(String postType) {
        if (!ALLOWED_POST_TYPES.contains(postType)) {
            throw new IllegalArgumentException("帖子类型只能是：失物招领、新生提问、校园动态");
        }
    }

    private void validateStatus(String status) {
        if (!ALLOWED_STATUSES.contains(status)) {
            throw new IllegalArgumentException("帖子状态只能是：已发布、已关闭、已删除");
        }
    }
}
