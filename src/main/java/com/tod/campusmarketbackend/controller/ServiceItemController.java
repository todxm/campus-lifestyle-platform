package com.tod.campusmarketbackend.controller;

import com.tod.campusmarketbackend.entity.ServiceItem;
import com.tod.campusmarketbackend.service.ServiceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ServiceItem Controller
 * 提供校园服务相关接口给前端调用。
 */
@RestController
@RequestMapping("/service-items")
@CrossOrigin(origins = "http://localhost:8081")
public class ServiceItemController {

    @Autowired
    private ServiceItemService serviceItemService;

    /**
     * 查询所有在架服务。
     */
    @GetMapping
    public List<ServiceItem> getAllServiceItems() {
        return serviceItemService.getAllServiceItems();
    }

    /**
     * 搜索服务。
     * 前端请求示例：/service-items/search?keyword=跑腿&type=生活服务
     */
    @GetMapping("/search")
    public List<ServiceItem> searchServiceItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type
    ) {
        return serviceItemService.searchServiceItems(keyword, type);
    }

    /**
     * 根据 ID 查询服务详情。
     */
    @GetMapping("/{id}")
    public ServiceItem getServiceItemDetail(@PathVariable Long id) {
        return serviceItemService.getServiceItemById(id);
    }

    /**
     * 添加新服务。
     */
    @PostMapping
    public void addServiceItem(@RequestBody ServiceItem serviceItem) {
        serviceItemService.addServiceItem(serviceItem);
    }

    /**
     * 下架服务。
     */
    @PutMapping("/{id}/offline")
    public String offlineServiceItem(@PathVariable Long id) {
        boolean success = serviceItemService.offlineServiceItem(id);

        if (success) {
            return "服务下架成功";
        } else {
            return "服务下架失败";
        }
    }
}
