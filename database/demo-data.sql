-- Optional fictional examples. Run after schema.sql in a dedicated demo database.
-- Sequential replay does not overwrite or duplicate matching demonstration records.
SET NAMES utf8mb4;
START TRANSACTION;

INSERT INTO product (title, description, price, category, condition_level, contact_wechat, status)
SELECT '演示：高数教材', '仅用于本地功能演示，无真实交易。', 15.00, '教材资料', '九成新', 'demo_only_not_contact', 'ON_SALE'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE title = '演示：高数教材' AND contact_wechat = 'demo_only_not_contact');

INSERT INTO service_item (name, type, description, price_info, contact_wechat, merchant_type, service_category,
    is_featured, is_verified, promotion_label, contact_mode, contact_code, platform_contact, contact_click_count, status)
SELECT '演示：资料打印', '打印复印', '虚构服务，仅用于查看详情和联系计数测试。', '演示价格', 'demo_only_not_contact',
    '个人同学', '打印复印', 1, 0, '示例服务', 'DIRECT', '演示', 'demo_only_not_contact', 0, 'ON'
WHERE NOT EXISTS (SELECT 1 FROM service_item WHERE name = '演示：资料打印' AND contact_wechat = 'demo_only_not_contact');

INSERT INTO help_task (task_type, pickup_location, delivery_location, detail_location, time_slot, reward, contact, remark, status)
SELECT '外卖代拿', '演示取餐点', '演示宿舍楼', '仅供测试', '中午', 2.00, 'demo_only_not_contact', '演示：帮拿任务', '待接单'
WHERE NOT EXISTS (SELECT 1 FROM help_task WHERE remark = '演示：帮拿任务' AND contact = 'demo_only_not_contact');

INSERT INTO wall_post (post_type, title, content, location, contact, tag, status)
SELECT '新生提问', '演示：图书馆怎么走', '这是虚构的校园墙示例，用于验证分类和详情。', '演示地点',
    'demo_only_not_contact', '新生求助', '已发布'
WHERE NOT EXISTS (SELECT 1 FROM wall_post WHERE title = '演示：图书馆怎么走' AND contact = 'demo_only_not_contact');

INSERT INTO wanted_item (title, category, budget_min, budget_max, description, contact, urgency, status)
SELECT '演示：求购床上桌', '宿舍用品', 10.00, 30.00, '虚构需求，仅用于求购流程演示。',
    'demo_only_not_contact', '不急', '求购中'
WHERE NOT EXISTS (SELECT 1 FROM wanted_item WHERE title = '演示：求购床上桌' AND contact = 'demo_only_not_contact');

COMMIT;
