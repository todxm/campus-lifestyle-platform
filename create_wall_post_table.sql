SET NAMES utf8mb4;

USE campus_market;

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
