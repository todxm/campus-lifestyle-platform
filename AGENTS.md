# 南航上海路小站后端开发规则

## 项目定位

本项目是“南航上海路小站”的 Spring Boot 后端，为微信小程序提供接口。

当前技术栈：

* Java 17
* Spring Boot
* MyBatis
* MySQL
* Maven Wrapper
* Windows 本地开发
* 本地默认端口 8080

当前主要模块：

* product：二手商品
* service_item：服务榜
* help_task：饭点帮拿
* wall_post：校园墙
* wanted_item：求购墙

## 总体工作方式

1. 每次任务开始前，先执行 `git status`。
2. 如果工作区不干净，先报告已有改动，不得直接覆盖。
3. 一次只完成一个明确的小目标。
4. 修改前先列出本轮预计修改的文件。
5. 只有任务明确允许的文件可以修改。
6. 如果发现需要修改白名单外文件，必须停止并说明原因。
7. 不得顺手重构无关模块。
8. 不得因为修复一个模块而修改其他稳定模块。
9. 完成后必须执行 `git diff --name-only` 并报告实际改动文件。
10. 未通过测试不得声称任务完成。

## 禁止操作

除非用户明确授权，否则禁止：

* push
* force push
* 部署服务器
* 修改远程仓库
* 删除数据库
* 删除数据表
* DROP TABLE
* TRUNCATE
* 大范围 DELETE
* 清空测试数据
* 重写 Git 历史
* 修改真实数据库密码
* 输出数据库密码
* 将真实密码写入可提交文件
* 修改生产环境配置
* 杀死无关进程
* 自动提交代码

如果操作存在数据丢失、账号风险或环境破坏风险，必须先询问用户。

## 配置与敏感信息

1. `application.yaml` 中不得写入真实密码。
2. 数据库配置使用环境变量或被忽略的本地配置。
3. `application-local.yaml` 不得提交。
4. 不得提交：

   * 密码
   * Token
   * 真实手机号
   * 真实微信号
   * 服务器密钥
   * 数据库备份
5. 修改配置后必须检查 `.gitignore` 和 `git status`。
6. 不得在报告中打印任何密码内容。

## 数据库规则

1. 默认不得删除、重建或清空已有业务表。
2. 新增表前先检查是否已有可复用模块。
3. SQL 文件必须避免危险语句。
4. 执行数据整理前先说明影响范围。
5. 涉及真实数据时必须先询问用户。
6. 状态删除优先使用逻辑删除，不得默认物理删除。
7. 不得影响以下稳定表：

   * product
   * service_item
   * help_task
   * wall_post
   * wanted_item

## Java 和 MyBatis 规则

1. 保持现有项目结构和命名方式。
2. 不引入新框架，除非任务明确要求。
3. 不擅自升级 Java、Spring Boot、MyBatis 或 Maven 版本。
4. 新字段必须同步检查：

   * Entity
   * Controller
   * Service
   * Mapper
   * Mapper XML
   * SQL 表字段
5. Java 驼峰字段和数据库下划线字段必须明确映射。
6. 参数校验失败应返回清楚错误，不得无意义地抛出 500。
7. 不得为了一个接口重构整个模块。

## 后端测试规则

修改 Java、Mapper、XML、配置或 SQL 后，必须执行：

```powershell
.\mvnw.cmd clean package
```

要求：

* BUILD SUCCESS
* 如果失败，必须报告关键错误栈、文件和行号
* 不得只说“后端错误”

涉及接口修改时，至少测试对应接口。

完成较大功能后还要回归：

```text
GET /products
GET /service-items
GET /help-tasks
GET /wall-posts
GET /wanted-items
```

如果出现：

* `ERR_CONNECTION_REFUSED`：先检查 Spring Boot 是否启动、8080 是否监听
* 404：检查接口路径和 Controller 映射
* 500：查看后端错误栈、Mapper、SQL 和数据库字段
* 数据为空：检查数据库内容、状态筛选和字段映射

## Git 规则

1. 开始前执行 `git status`。
2. 完成后执行：

   * `git status`
   * `git diff --name-only`
3. 不提交：

   * `target/`
   * 日志
   * uploads
   * IDE 缓存
   * `*.pid`
   * 本地私有配置
4. 只有用户明确要求封版时才 commit。
5. commit 前列出拟提交文件。
6. commit 后报告 commit id。
7. 永远不自动 push。

## 完成报告格式

每次任务结束必须报告：

1. 本轮目标
2. 修改文件
3. 未修改的稳定模块
4. 执行的测试
5. 测试结果
6. Git 状态
7. 遗留问题
8. 是否需要用户人工确认

不得用“应该没问题”“大概正常”代替测试结果。
