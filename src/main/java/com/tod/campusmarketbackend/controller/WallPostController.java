package com.tod.campusmarketbackend.controller;

import com.tod.campusmarketbackend.entity.WallPost;
import com.tod.campusmarketbackend.service.WallPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 校园墙接口控制器
 * 提供失物招领、新生提问、校园动态的查询、发布、关闭和软删除接口。
 */
@RestController
@RequestMapping("/wall-posts")
@CrossOrigin(origins = {
        "http://localhost:8081",
        "http://115.159.47.131:8081",
        "http://115.159.47.131"
})
public class WallPostController {

    private final WallPostService wallPostService;

    public WallPostController(WallPostService wallPostService) {
        this.wallPostService = wallPostService;
    }

    /**
     * 查询校园墙帖子列表。
     * 可选参数：
     * postType=失物招领
     * status=已发布
     */
    @GetMapping
    public ResponseEntity<?> listWallPosts(
            @RequestParam(required = false) String postType,
            @RequestParam(required = false) String status
    ) {
        try {
            return ResponseEntity.ok(wallPostService.getWallPosts(postType, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 根据 ID 查询校园墙帖子详情。
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getWallPostDetail(@PathVariable Long id) {
        WallPost wallPost = wallPostService.getWallPostById(id);
        if (wallPost == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(wallPost);
    }

    /**
     * 发布校园墙帖子。
     */
    @PostMapping
    public ResponseEntity<String> publishWallPost(@RequestBody WallPost wallPost) {
        try {
            boolean success = wallPostService.publishWallPost(wallPost);
            return success
                    ? ResponseEntity.ok("校园墙发布成功")
                    : ResponseEntity.badRequest().body("校园墙发布失败");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 关闭帖子。
     */
    @PutMapping("/{id}/close")
    public ResponseEntity<String> closeWallPost(@PathVariable Long id) {
        boolean success = wallPostService.closeWallPost(id);
        return success
                ? ResponseEntity.ok("帖子已关闭")
                : ResponseEntity.badRequest().body("帖子关闭失败");
    }

    /**
     * 软删除帖子，不物理删除数据。
     */
    @PutMapping("/{id}/delete")
    public ResponseEntity<String> deleteWallPost(@PathVariable Long id) {
        boolean success = wallPostService.deleteWallPost(id);
        return success
                ? ResponseEntity.ok("帖子已删除")
                : ResponseEntity.badRequest().body("帖子删除失败");
    }
}
