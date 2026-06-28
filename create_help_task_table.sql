SET NAMES utf8mb4;

USE campus_market;

CREATE TABLE IF NOT EXISTS help_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帮拿任务ID',
    task_type VARCHAR(50) NOT NULL COMMENT '任务类型',
    pickup_location VARCHAR(100) NOT NULL COMMENT '取件地点',
    delivery_location VARCHAR(100) NOT NULL COMMENT '送达地点',
    detail_location VARCHAR(200) COMMENT '具体位置说明',
    time_slot VARCHAR(50) NOT NULL COMMENT '时间段',
    reward DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '酬劳',
    contact VARCHAR(100) NOT NULL COMMENT '联系方式',
    remark VARCHAR(500) COMMENT '备注',
    status VARCHAR(50) NOT NULL DEFAULT '待接单' COMMENT '状态',
    helper_contact VARCHAR(100) COMMENT '接单人联系方式',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_help_task_status_created_time (status, created_time),
    INDEX idx_help_task_type_created_time (task_type, created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帮拿任务表';
