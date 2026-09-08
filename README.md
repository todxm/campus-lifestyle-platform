# 南航上海路小站 · 后端与早期网页端

面向南昌航空大学上海路校区的校园生活项目，为微信小程序提供饭点帮拿、校园墙、求购墙、商品和服务榜接口。当前业务功能版本为 **V0.7**；Maven 制品版本仍为 `0.0.1-SNAPSHOT`。

当前重点是现有功能收尾、本地复现和 GitHub 文档整理，暂不推进正式上线。仓库保留早期原生网页端，不能将网页端、后端接口和小程序的能力视为完全一致。

## 项目组成

| 部分 | 位置与用途 |
| --- | --- |
| Spring Boot 后端 | 本仓库 `src/`，提供业务接口 |
| 微信原生小程序 | 本仓库 `miniprogram/`，当前主要客户端 |
| 早期网页端 | 本仓库 `frontend/`，保留商品和服务等早期页面 |
| MySQL | 保存 `product`、`service_item`、`help_task`、`wall_post`、`wanted_item` 等业务数据 |

后端仓库：[todxm/campus-lifestyle-platform](https://github.com/todxm/campus-lifestyle-platform)。

## 已有能力

- V0.4 饭点帮拿：查询、发布、接单、完成、取消。
- V0.5 校园墙：查询、分类、发布、详情、关闭和逻辑删除接口。
- V0.6 求购墙：查询、分类、发布、详情、标记已找到、关闭和逻辑删除接口。
- V0.7 服务榜：服务查询、分类相关字段、详情、联系方式和联系点击统计。
- 商品与早期服务功能：发布、搜索、详情、下架。
- 商品/服务图片上传接口；小程序尚未接入上传流程。

部分接口没有对应小程序入口。例如小程序“正在出售”为静态示例，不能据此宣称小程序商品交易闭环已经完成。

## 技术栈

| 技术 | 仓库配置 |
| --- | --- |
| Java | 17 |
| Spring Boot | 4.0.6 |
| MyBatis Spring Boot Starter | 4.0.1 |
| 数据库 | MySQL，数据库名默认 `campus_market` |
| 构建 | Maven Wrapper，无需单独安装 Maven |
| 早期网页端 | HTML、CSS、JavaScript |

```text
src/main/java/com/tod/campusmarketbackend/
  controller/   HTTP 接口与参数处理
  service/      业务处理
  mapper/       数据访问接口
  entity/       业务实体
  config/       Web 配置
src/main/resources/
  mapper/                         MyBatis XML
  application.yaml                共享配置
  application-local.example.yaml  本地配置模板
src/test/                         当前应用启动测试
frontend/                         早期网页端
miniprogram/                      微信原生小程序
*.sql                             分阶段建表、升级与数据脚本
```

## 本地运行（Windows）

### 1. 获取代码与环境

```powershell
git clone https://github.com/todxm/campus-lifestyle-platform.git campus-market-backend
cd campus-market-backend
java -version
```

准备 JDK 17、可连接的 MySQL，以及首次下载 Maven 和依赖所需的网络环境。

### 2. 创建独立开发数据库

完整 V0.7 建表和可选示例数据已放入 [database/](database/README.md)。按照该说明创建新的 `campus_market_demo`，依次执行 `database/schema.sql` 和 `database/demo-data.sql`，再将本地连接配置指向新库。

新脚本包含全部五张业务表，不需要继续运行根目录的历史升级/数据脚本。已有 `campus_market` 业务库保持原样；不要混用含固定库名或删除数据语句的旧脚本。

2026-09-06 已完成独立 MySQL 空库验证、重复导入检查和 48 次接口请求验证，具体范围与限制见 [验证记录](database/README.md#本次验证记录)。

### 3. 设置本地数据库配置

推荐复制仓库内的非敏感模板：

```powershell
Copy-Item src/main/resources/application-local.example.yaml src/main/resources/application-local.yaml
```

仅在目标文件不存在时执行，避免覆盖自己的已有配置。编辑本地文件中的连接地址、用户名及密码；`application-local.yaml` 已被 Git 忽略，不应提交。

共享配置默认启用 `local` profile，端口为 `8080`。也支持 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 和 `SPRING_PROFILES_ACTIVE` 环境变量。若选择环境变量方式，注意本地 profile 中显式填写的同名配置可能覆盖共享配置的取值；不要混用后误以为环境变量必然生效。不要把真实密码写入 README 或共享配置。

### 4. 启动后端

```powershell
.\mvnw.cmd spring-boot:run
```

保留运行终端。关闭进程后接口会不可用；出现 `ERR_CONNECTION_REFUSED` 时先检查进程、端口和请求结果。

只读连通性检查：

```powershell
curl.exe -i http://127.0.0.1:8080/products
curl.exe -i http://127.0.0.1:8080/service-items
curl.exe -i http://127.0.0.1:8080/help-tasks
curl.exe -i http://127.0.0.1:8080/wall-posts
curl.exe -i http://127.0.0.1:8080/wanted-items
```

确认 HTTP 状态及 JSON 内容；HTTP 200 不代表发布、状态变更等业务流程都已通过。

### 5. 打开客户端

主要客户端为微信小程序：用微信开发者工具导入 `miniprogram/`，其 `utils/config.js` 默认连接 `http://127.0.0.1:8080`。此地址用于本机模拟器，真机不能直接访问电脑的回环地址。

早期网页端为可选历史演示入口：

```powershell
cd frontend
python -m http.server 8081
```

打开 `http://localhost:8081/home.html`。运行前需核对网页中的 API 地址是否指向当前本地后端；旧页面可能保留部署地址，不能保证仅启动静态服务器即可完成本地联调。

## 接口索引

以下路径来自当前 Controller 映射。请求体字段应以 `entity/` 和 Controller 校验为准，查询结果与写操作返回体并未统一成同一种包装格式。

| 模块 | 查询 | 写操作 |
| --- | --- | --- |
| 帮拿 | GET `/help-tasks`、`/help-tasks/{id}` | POST `/help-tasks`；PUT `/help-tasks/{id}/accept`、`/finish`、`/cancel` |
| 校园墙 | GET `/wall-posts`、`/wall-posts/{id}` | POST `/wall-posts`；PUT `/wall-posts/{id}/close`、`/delete` |
| 求购墙 | GET `/wanted-items`、`/wanted-items/{id}` | POST `/wanted-items`；PUT `/wanted-items/{id}/found`、`/close`、`/delete` |
| 商品 | GET `/products`、`/products/{id}`、`/products/search` | POST `/products`；PUT `/products/{id}/offline` |
| 服务榜 | GET `/service-items`、`/service-items/{id}`、`/service-items/search` | POST `/service-items`；PUT `/service-items/{id}/offline`、`/contact-click` |
| 图片 | 无独立查询接口，上传结果返回地址 | POST `/upload/product-image`、`/upload/service-image`，multipart 字段 `file` |

表中同一单元格的操作后缀沿用该行完整资源前缀，例如 `/finish` 指 `/help-tasks/{id}/finish`。

帮拿列表支持 `status`、`taskType` 查询参数。接单请求体字段为 `helperContact`。发布和状态变更会修改数据，应使用独立测试记录。

## 测试与已知限制

```powershell
.\mvnw.cmd clean package
```

上述命令已于 2026-09-06 在独立验证数据库配置下执行，结果为 BUILD SUCCESS。仓库目前的 Java 测试仅为 `contextLoads`，不代表业务回归覆盖。历史人工/API 检查记录与可重复执行的自动化测试应分别描述。

- 小程序已完成关键流程自动回归和一轮开发者工具体验检查；真机、多基础库兼容性与完整录屏仍待确认。
- 尚无完整用户认证和权限体系，本地联系方式不是可信身份依据。
- 服务榜使用示例数据，不代表真实商家运营。
- 空库初始化已验证；真实页面截图和 V0.7 完整人工验收记录仍待补齐。
- 历史 Git 曾包含数据库凭据；当前配置改为本地文件或环境变量，不能证明历史凭据已轮换失效。公开整理前需单独核实，文档不包含真实密码。

## 历史资料与当前收尾方向

[TESTING.md](TESTING.md) 为早期 V0.1 网页端测试资料；[DEPLOYMENT.md](DEPLOYMENT.md) 为历史服务器部署记录，其中公网地址和运行结论未经本轮验证，不作为当前在线演示承诺。它们尚待与 V0.7 文档统一。

当前顺序：完成现有功能的页面验收，整理真实截图与仓库互链。数据库复现流程已补齐。正式部署、商家运营和大型新功能不属于当前收尾目标。
