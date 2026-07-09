SET NAMES utf8mb4;

USE campus_market;

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
