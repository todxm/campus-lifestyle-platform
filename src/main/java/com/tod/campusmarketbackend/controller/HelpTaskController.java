package com.tod.campusmarketbackend.controller;

import com.tod.campusmarketbackend.entity.HelpTask;
import com.tod.campusmarketbackend.service.HelpTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 帮拿任务接口控制器
 * 提供饭点帮拿任务的查询、发布、接单、完成和取消接口。
 */
@RestController
@RequestMapping("/help-tasks")
@CrossOrigin(origins = {
        "http://localhost:8081",
        "http://115.159.47.131:8081",
        "http://115.159.47.131"
})
public class HelpTaskController {

    private final HelpTaskService helpTaskService;

    public HelpTaskController(HelpTaskService helpTaskService) {
        this.helpTaskService = helpTaskService;
    }

    /**
     * 查询帮拿任务列表。
     * 可选参数：
     * status=待接单
     * taskType=外卖代拿
     */
    @GetMapping
    public ResponseEntity<?> listHelpTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String taskType
    ) {
        try {
            return ResponseEntity.ok(helpTaskService.getHelpTasks(status, taskType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 根据 ID 查询帮拿任务详情。
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getHelpTaskDetail(@PathVariable Long id) {
        HelpTask helpTask = helpTaskService.getHelpTaskById(id);
        if (helpTask == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(helpTask);
    }

    /**
     * 发布帮拿任务。
     */
    @PostMapping
    public ResponseEntity<String> publishHelpTask(@RequestBody HelpTask helpTask) {
        try {
            boolean success = helpTaskService.publishHelpTask(helpTask);
            return success
                    ? ResponseEntity.ok("帮拿任务发布成功")
                    : ResponseEntity.badRequest().body("帮拿任务发布失败");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 接单。
     * 请求体示例：
     * {"helperContact":"helper001"}
     */
    @PutMapping("/{id}/accept")
    public ResponseEntity<String> acceptHelpTask(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        try {
            String helperContact = request.get("helperContact");
            boolean success = helpTaskService.acceptHelpTask(id, helperContact);
            return success
                    ? ResponseEntity.ok("帮拿任务接单成功")
                    : ResponseEntity.badRequest().body("只有待接单任务可以接单");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 完成任务。
     */
    @PutMapping("/{id}/finish")
    public ResponseEntity<String> finishHelpTask(@PathVariable Long id) {
        boolean success = helpTaskService.finishHelpTask(id);
        return success
                ? ResponseEntity.ok("帮拿任务已完成")
                : ResponseEntity.badRequest().body("只有已接单任务可以完成");
    }

    /**
     * 取消任务。
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelHelpTask(@PathVariable Long id) {
        boolean success = helpTaskService.cancelHelpTask(id);
        return success
                ? ResponseEntity.ok("帮拿任务已取消")
                : ResponseEntity.badRequest().body("只有待接单或已接单任务可以取消");
    }
}
