-- V0.7 fresh database schema. Select an empty database before running.
-- No USE, DROP, DELETE or ALTER statements. This is not a legacy migration.
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(50),
    condition_level VARCHAR(20),
    contact_wechat VARCHAR(100),
    status VARCHAR(20) DEFAULT 'ON_SALE',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    image_url VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS service_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    description TEXT,
    price_info VARCHAR(100),
    contact_wechat VARCHAR(100),
    status VARCHAR(20) DEFAULT 'ON',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    image_url VARCHAR(255),
    merchant_type VARCHAR(50),
    service_category VARCHAR(50),
    is_featured TINYINT DEFAULT 0,
    is_verified TINYINT DEFAULT 0,
    promotion_label VARCHAR(50),
    contact_mode VARCHAR(50),
    contact_code VARCHAR(50),
    platform_contact VARCHAR(100),
    contact_click_count INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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

CREATE TABLE IF NOT EXISTS wall_post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '校园墙帖子ID',
    post_type VARCHAR(50) NOT NULL COMMENT '帖子类型',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    content VARCHAR(1000) NOT NULL COMMENT '内容',
    location VARCHAR(100) COMMENT '地点',
    contact VARCHAR(100) COMMENT '联系方式',
    tag VARCHAR(50) COMMENT '标签',
    status VARCHAR(50) NOT NULL DEFAULT '已发布' COMMENT '状态',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_wall_post_type_created_time (post_type, created_time),
    INDEX idx_wall_post_status_created_time (status, created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='校园墙帖子表';

CREATE TABLE IF NOT EXISTS wanted_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '求购ID',
    title VARCHAR(100) NOT NULL COMMENT '求购标题',
    category VARCHAR(50) NOT NULL COMMENT '分类',
    budget_min DECIMAL(10,2) COMMENT '最低预算',
    budget_max DECIMAL(10,2) COMMENT '最高预算',
    description VARCHAR(1000) NOT NULL COMMENT '需求说明',
    contact VARCHAR(100) NOT NULL COMMENT '联系方式',
    urgency VARCHAR(50) NOT NULL COMMENT '急需程度',
    status VARCHAR(50) NOT NULL DEFAULT '求购中' COMMENT '状态',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_wanted_item_category (category),
    INDEX idx_wanted_item_status_created (status, created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='求购墙表';
