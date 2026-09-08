# V0.7 数据库初始化与复现

本目录用于在**新建的独立开发数据库**中建立五张业务表，并可选导入虚构示例数据。适用于 MySQL 8.0，2026-09-06 已在 MySQL 8.0.46、Java 17.0.12 上验证。

## 文件与执行顺序

1. `schema.sql`：完整 V0.7 表结构，包含商品、服务榜、帮拿、校园墙和求购。
2. `demo-data.sql`：可选，每个模块一条示例，联系方式统一为 `demo_only_not_contact`，不可用于真实联系。

两个脚本都不指定数据库名，不包含删除或清空表的操作。建表使用 `IF NOT EXISTS`；示例使用标记字段判断记录是否已存在，顺序重复导入不会覆盖已有状态或重复插入匹配记录。不要并发导入示例；如果手动修改了示例的标题/标记或联系方式，再导入可能重新生成示例。

**这不是旧库升级工具。** `IF NOT EXISTS` 不会修补已有表的缺失字段。已有 `campus_market` 数据库应保留，不要为复现删除、重建或覆盖它。

## 从空库启动（Windows）

在后端项目根目录执行。使用有创建数据库权限的本地 MySQL 账号；密码由 MySQL 提示输入，不写入命令或文档：

```powershell
mysql --host=127.0.0.1 --port=3306 --user=root --password --default-character-set=utf8mb4
```

进入 MySQL 后：

```sql
CREATE DATABASE campus_market_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_market_demo;
SOURCE database/schema.sql;
SOURCE database/demo-data.sql;
SHOW TABLES;
SELECT COUNT(*) AS help_examples FROM help_task;
```

`CREATE DATABASE` 故意不使用 `IF NOT EXISTS`。如果名称已存在，停止并换一个新名称，不要继续向未知旧库导入。全新库应有五张表，每表一条示例。省略 `demo-data.sql` 则得到空表。

若 MySQL 不在 PATH 中，使用本机 `mysql.exe` 的实际路径。如果 `SOURCE` 找不到文件，确认从后端根目录启动客户端，或使用以正斜杠分隔的完整脚本路径。

### 配置后端连接

按根目录 README 复制 `application-local.example.yaml`，仅在本地 `application-local.yaml` 中填入：

- 数据库地址：将库名改为本次新建的 `campus_market_demo`。
- 用户名和密码：使用对该开发库有访问权限的本地账号。
- 默认后端端口：8080。

不要将本地配置提交。已有账号若无新库权限，需要使用有权限的开发账号；不要修改现有业务库授权来凑合测试。

在后端根目录执行：

```powershell
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

构建和启动是两次命令，启动命令会持续运行。若机器设置了全局 Java 编码为 GBK，可在当前 PowerShell 会话显式使用：

```powershell
$env:JAVA_TOOL_OPTIONS = '-Dfile.encoding=UTF-8'
```

### 核对接口

```powershell
curl.exe -i http://127.0.0.1:8080/products
curl.exe -i http://127.0.0.1:8080/service-items
curl.exe -i http://127.0.0.1:8080/help-tasks
curl.exe -i http://127.0.0.1:8080/wall-posts
curl.exe -i http://127.0.0.1:8080/wanted-items
```

首次导入示例后，五个接口应返回 JSON 数组，各含一条虚构记录。随后在微信开发者工具中验证页面；小程序“正在出售”仍为静态示例，不会显示这里的商品记录。

## 与旧 SQL 的关系

根目录保留的 SQL 是历史建表、升级或数据整理记录，不需要在新 `schema.sql` 之后逐一执行：

- 图片字段与服务榜扩展字段已包含在完整结构中。
- `campus_market_mvp_data.sql` 含删除旧数据的语句，不用于本流程。
- `update_service_item_v07_demo_data.sql` 依赖旧记录 ID，不用于新库播种。
- 旧脚本可能包含 `USE campus_market`，不要在独立演示库流程中混用。

## 本次验证记录

2026-09-06，使用临时目录内的独立 MySQL 实例（127.0.0.1:13307）与独立后端（127.0.0.1:18080）。没有写入原有业务库；这些是验证端口，不是项目默认配置。

| 检查 | 结果 |
| --- | --- |
| 全新数据库创建五张表并导入示例 | 通过 |
| 两份脚本各顺序执行两遍 | 每表仍为一条示例 |
| MySQL 客户端 SOURCE 导入另一新库 | 通过 |
| `mvnw.cmd clean package` | BUILD SUCCESS，1 个现有启动测试通过 |
| 五类列表及详情、字段映射 | 通过 |
| 五类发布与重新查询 | 通过 |
| 帮拿接单、完成、取消及重复接单/完成后取消拒绝 | 通过 |
| 校园墙分类、关闭、逻辑删除后的列表排除 | 通过 |
| 求购关闭、已找到、逻辑删除与空分类结果 | 通过 |
| 商品下架、服务联系计数与下架 | 通过 |
| 状态操作后重新导入示例 | 行数不增加，已取消的示例任务未被重置 |

本次接口验证共 48 次 HTTP 请求，使用临时验证脚本执行，不属于仓库现有 JUnit 测试覆盖。未验证并发、权限安全、图片上传或微信页面视觉效果；不能据此声称整个 V0.7 已全部验收。

## 当前后续事项

完成微信小程序核心页面的人工验收，再补充真实截图与验收结果。数据脚本只用于独立开发/演示环境，不包含真实业务数据、密码或服务器配置。
