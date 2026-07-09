package com.tod.campusmarketbackend.controller;

import com.tod.campusmarketbackend.entity.WantedItem;
import com.tod.campusmarketbackend.service.WantedItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 求购墙接口控制器
 * 提供求购信息的查询、发布、标记已找到、关闭和删除接口。
 */
@RestController
@RequestMapping("/wanted-items")
@CrossOrigin(origins = {
        "http://localhost:8081",
        "http://115.159.47.131:8081",
        "http://115.159.47.131"
})
public class WantedItemController {

    private final WantedItemService wantedItemService;

    public WantedItemController(WantedItemService wantedItemService) {
        this.wantedItemService = wantedItemService;
    }

    /**
     * 查询求购列表。
     * 可选参数：
     * category=宿舍用品
     * status=求购中
     */
    @GetMapping
    public ResponseEntity<?> listWantedItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status
    ) {
        try {
            return ResponseEntity.ok(wantedItemService.getWantedItems(category, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 根据 ID 查询求购详情。
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getWantedItemDetail(@PathVariable Long id) {
        WantedItem wantedItem = wantedItemService.getWantedItemById(id);
        if (wantedItem == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(wantedItem);
    }

    /**
     * 发布求购。
     */
    @PostMapping
    public ResponseEntity<String> publishWantedItem(@RequestBody WantedItem wantedItem) {
        try {
            boolean success = wantedItemService.publishWantedItem(wantedItem);
            return success
                    ? ResponseEntity.ok("求购发布成功")
                    : ResponseEntity.badRequest().body("求购发布失败");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 标记已找到。
     */
    @PutMapping("/{id}/found")
    public ResponseEntity<String> markFound(@PathVariable Long id) {
        boolean success = wantedItemService.markFound(id);
        return success
                ? ResponseEntity.ok("求购已标记为已找到")
                : ResponseEntity.badRequest().body("只有求购中或已关闭的求购可以标记为已找到");
    }

    /**
     * 关闭求购。
     */
    @PutMapping("/{id}/close")
    public ResponseEntity<String> closeWantedItem(@PathVariable Long id) {
        boolean success = wantedItemService.closeWantedItem(id);
        return success
                ? ResponseEntity.ok("求购已关闭")
                : ResponseEntity.badRequest().body("只有求购中的求购可以关闭");
    }

    /**
     * 软删除求购。
     */
    @PutMapping("/{id}/delete")
    public ResponseEntity<String> deleteWantedItem(@PathVariable Long id) {
        boolean success = wantedItemService.deleteWantedItem(id);
        return success
                ? ResponseEntity.ok("求购已删除")
                : ResponseEntity.badRequest().body("求购删除失败");
    }
}
