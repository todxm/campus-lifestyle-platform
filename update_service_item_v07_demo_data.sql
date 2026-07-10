USE campus_market;

-- V0.7 服务榜示例数据整理脚本
-- 说明：
-- 1. 不清空 service_item 表，不物理删除任何数据。
-- 2. 先备份当前 service_item 表，方便后续人工对照。
-- 3. 明显测试味数据仅改为 OFF，下架后不会出现在 /service-items 列表。
-- 4. 联系方式统一使用示例值，不写入真实商家微信或手机号。

CREATE TABLE IF NOT EXISTS service_item_backup_v07 AS
SELECT *
FROM service_item;

-- id=19、20 是本地测试数据，名称/描述/价格测试味较重，先下架处理。
UPDATE service_item
SET status = 'OFF'
WHERE id IN (19, 20);

-- 校外快递代拿：归入生活服务。
UPDATE service_item
SET
    service_category = '生活服务',
    merchant_type = '个人同学',
    contact_mode = 'CODE',
    contact_code = '南航小站',
    platform_contact = 'student_service_demo',
    promotion_label = '饭点常用',
    is_featured = 0,
    is_verified = 0,
    contact_click_count = COALESCE(contact_click_count, 0),
    contact_wechat = 'student_service_demo'
WHERE id = 13;

-- 宿舍零食配送：归入生活服务。
UPDATE service_item
SET
    service_category = '生活服务',
    merchant_type = '校园商家',
    contact_mode = 'CODE',
    contact_code = '南航小站',
    platform_contact = 'student_service_demo',
    promotion_label = '宿舍常用',
    is_featured = 0,
    is_verified = 0,
    contact_click_count = COALESCE(contact_click_count, 0),
    contact_wechat = 'student_service_demo'
WHERE id = 14;

-- 资料打印复印：归入打印复印。
UPDATE service_item
SET
    service_category = '打印复印',
    merchant_type = '校园商家',
    contact_mode = 'DIRECT',
    contact_code = '南航小站',
    platform_contact = 'student_service_demo',
    promotion_label = '新生常用',
    is_featured = 1,
    is_verified = 0,
    contact_click_count = COALESCE(contact_click_count, 0),
    contact_wechat = 'student_service_demo'
WHERE id = 15;

-- 电脑系统与软件维修：归入生活服务。
UPDATE service_item
SET
    service_category = '生活服务',
    merchant_type = '个人同学',
    contact_mode = 'DIRECT',
    contact_code = '南航小站',
    platform_contact = 'student_service_demo',
    promotion_label = '电脑应急',
    is_featured = 0,
    is_verified = 0,
    contact_click_count = COALESCE(contact_click_count, 0),
    contact_wechat = 'student_service_demo'
WHERE id = 16;

-- 大学生驾校咨询：归入驾校。
UPDATE service_item
SET
    service_category = '驾校',
    merchant_type = '中介/机构',
    contact_mode = 'PLATFORM',
    contact_code = '南航小站',
    platform_contact = 'student_service_demo',
    promotion_label = '新生常问',
    is_featured = 1,
    is_verified = 0,
    contact_click_count = COALESCE(contact_click_count, 0),
    contact_wechat = 'student_service_demo'
WHERE id = 17;

-- 校园宽带与电话卡办理：统一为“校园卡宽带”，不再使用“校园卡/宽带”。
UPDATE service_item
SET
    service_category = '校园卡宽带',
    merchant_type = '中介/机构',
    contact_mode = 'PLATFORM',
    contact_code = '南航小站',
    platform_contact = 'student_service_demo',
    promotion_label = '新生常问',
    is_featured = 1,
    is_verified = 0,
    contact_click_count = COALESCE(contact_click_count, 0),
    contact_wechat = 'student_service_demo'
WHERE id = 18;
