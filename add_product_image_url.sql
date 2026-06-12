USE campus_market;

-- 先检查 product 表是否已经存在 image_url 字段
SELECT COUNT(*) AS image_url_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'product'
  AND COLUMN_NAME = 'image_url';

-- 字段不存在时执行 ALTER TABLE；已经存在时只返回提示，避免重复执行报错
SET @image_url_exists = (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'product'
      AND COLUMN_NAME = 'image_url'
);

SET @add_image_url_sql = IF(
    @image_url_exists = 0,
    'ALTER TABLE product ADD COLUMN image_url VARCHAR(255) COMMENT ''商品图片地址''',
    'SELECT ''product.image_url 字段已存在，无需重复添加'' AS message'
);

PREPARE add_image_url_statement FROM @add_image_url_sql;
EXECUTE add_image_url_statement;
DEALLOCATE PREPARE add_image_url_statement;
