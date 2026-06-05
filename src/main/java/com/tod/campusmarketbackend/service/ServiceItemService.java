package com.tod.campusmarketbackend.service;

import com.tod.campusmarketbackend.entity.ServiceItem;
import com.tod.campusmarketbackend.mapper.ServiceItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service 层
 * 封装 ServiceItem 业务逻辑
 */
@Service
public class ServiceItemService {

    @Autowired
    private ServiceItemMapper serviceItemMapper;

    /**
     * 获取所有服务
     * @return List<ServiceItem>
     */
    public List<ServiceItem> getAllServiceItems() {
        return serviceItemMapper.findAll();
    }

    /**
     * 根据 ID 获取服务
     */
    public ServiceItem getServiceItemById(Long id) {
        return serviceItemMapper.findById(id);
    }

    /**
     * 添加新服务
     */
    public void addServiceItem(ServiceItem serviceItem) {
        serviceItemMapper.insert(serviceItem);
    }

    /**
     * 搜索服务（按关键词和类型）
     */
    public List<ServiceItem> searchServiceItems(String keyword, String type) {
        return serviceItemMapper.search(keyword, type);
    }

    /**
     * 下架服务
     */
    public boolean offlineServiceItem(Long id) {
        return serviceItemMapper.offlineById(id) > 0;
    }
}
