USE campus_market;

-- 本脚本只补充校园服务商业化展示字段，不会删除表，也不会清空已有数据。
-- 每个字段都会先检查是否存在，因此脚本可以重复执行。

SET @sql = IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'service_item' AND COLUMN_NAME = 'merchant_type') = 0,
    'ALTER TABLE service_item ADD COLUMN merchant_type VARCHAR(50) COMMENT ''服务主体类型''',
    'SELECT ''merchant_type 已存在，跳过'' AS message'
);
PREPARE statement FROM @sql;
EXECUTE statement;
DEALLOCATE PREPARE statement;

SET @sql = IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'service_item' AND COLUMN_NAME = 'service_category') = 0,
    'ALTER TABLE service_item ADD COLUMN service_category VARCHAR(50) COMMENT ''服务细分类别''',
    'SELECT ''service_category 已存在，跳过'' AS message'
);
PREPARE statement FROM @sql;
EXECUTE statement;
DEALLOCATE PREPARE statement;

SET @sql = IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'service_item' AND COLUMN_NAME = 'is_featured') = 0,
    'ALTER TABLE service_item ADD COLUMN is_featured TINYINT DEFAULT 0 COMMENT ''是否推荐服务''',
    'SELECT ''is_featured 已存在，跳过'' AS message'
);
PREPARE statement FROM @sql;
EXECUTE statement;
DEALLOCATE PREPARE statement;

SET @sql = IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'service_item' AND COLUMN_NAME = 'is_verified') = 0,
    'ALTER TABLE service_item ADD COLUMN is_verified TINYINT DEFAULT 0 COMMENT ''是否认证商家''',
    'SELECT ''is_verified 已存在，跳过'' AS message'
);
PREPARE statement FROM @sql;
EXECUTE statement;
DEALLOCATE PREPARE statement;

SET @sql = IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'service_item' AND COLUMN_NAME = 'promotion_label') = 0,
    'ALTER TABLE service_item ADD COLUMN promotion_label VARCHAR(50) COMMENT ''推广标签''',
    'SELECT ''promotion_label 已存在，跳过'' AS message'
);
PREPARE statement FROM @sql;
EXECUTE statement;
DEALLOCATE PREPARE statement;

SET @sql = IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'service_item' AND COLUMN_NAME = 'contact_mode') = 0,
    'ALTER TABLE service_item ADD COLUMN contact_mode VARCHAR(50) COMMENT ''联系方式模式''',
    'SELECT ''contact_mode 已存在，跳过'' AS message'
);
PREPARE statement FROM @sql;
EXECUTE statement;
DEALLOCATE PREPARE statement;

SET @sql = IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'service_item' AND COLUMN_NAME = 'contact_code') = 0,
    'ALTER TABLE service_item ADD COLUMN contact_code VARCHAR(50) COMMENT ''咨询暗号''',
    'SELECT ''contact_code 已存在，跳过'' AS message'
);
PREPARE statement FROM @sql;
EXECUTE statement;
DEALLOCATE PREPARE statement;

SET @sql = IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'service_item' AND COLUMN_NAME = 'platform_contact') = 0,
    'ALTER TABLE service_item ADD COLUMN platform_contact VARCHAR(100) COMMENT ''平台商业号联系方式''',
    'SELECT ''platform_contact 已存在，跳过'' AS message'
);
PREPARE statement FROM @sql;
EXECUTE statement;
DEALLOCATE PREPARE statement;

SET @sql = IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'service_item' AND COLUMN_NAME = 'contact_click_count') = 0,
    'ALTER TABLE service_item ADD COLUMN contact_click_count INT DEFAULT 0 COMMENT ''联系点击次数''',
    'SELECT ''contact_click_count 已存在，跳过'' AS message'
);
PREPARE statement FROM @sql;
EXECUTE statement;
DEALLOCATE PREPARE statement;
