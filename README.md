# 南航上海路校园生活小站

## 项目简介

南航上海路校园生活小站是一个面向南昌航空大学上海路校区学生的校园生活服务 MVP。

项目当前聚焦两个最基础、最常见的校园生活场景：二手闲置交易和校园服务入口。学生可以发布闲置物品、浏览商品详情、筛选商品分类，也可以发布跑腿代拿、打印复印、电脑维修等校园服务信息。

后续计划继续扩展新生指南、失物招领、校园服务整合等功能，让它逐步成为一个更完整的校园生活服务平台。

## 当前已完成功能

### 二手闲置模块

- 二手商品列表
- 商品详情
- 发布商品
- 下架商品
- 商品搜索和分类筛选
- 商品分类下拉选择
- 搜索框按 Enter 直接搜索
- 复制卖家微信号

### 校园服务模块

- 校园服务列表
- 服务详情
- 发布服务
- 下架服务
- 服务搜索和类型筛选
- 服务类型下拉选择
- 搜索框按 Enter 直接搜索
- 复制联系微信号

### 页面体验

- 统一顶部导航栏
- 发布表单基础校验
- 移动端适配
- 原生 HTML、CSS、JavaScript 实现，无 Vue/React

## 技术栈说明

| 类型 | 技术 |
| --- | --- |
| 后端 | Java、Spring Boot |
| 持久层 | MyBatis |
| 数据库 | MySQL |
| 前端 | 原生 HTML、CSS、JavaScript |
| 构建工具 | Maven |
| 版本管理 | Git、GitHub |

## 项目目录结构

```text
campus-market-backend
├── frontend
│   ├── index.html                 二手商品列表页
│   ├── detail.html                商品详情页
│   ├── publish.html               发布商品页
│   ├── service.html               校园服务列表页
│   ├── service-detail.html        服务详情页
│   └── service-publish.html       发布服务页
├── src
│   └── main
│       ├── java
│       │   └── com/tod/campusmarketbackend
│       │       ├── controller     接口控制层
│       │       ├── entity         实体类
│       │       ├── mapper         MyBatis Mapper 接口
│       │       └── service        业务逻辑层
│       └── resources
│           ├── mapper             MyBatis XML 映射文件
│           └── application.yaml   Spring Boot 配置文件
├── pom.xml                        Maven 项目配置
└── README.md
```

## 本地运行方式

### 1. 启动 MySQL

先启动本地 MySQL 服务，并创建项目使用的数据库。

数据库连接配置位于：

```text
src/main/resources/application.yaml
```

如果你的 MySQL 用户名、密码或数据库名不同，需要先修改这里的配置。

### 2. 启动 Spring Boot 后端

在项目根目录运行：

```bash
.\mvnw.cmd spring-boot:run
```

后端默认运行地址：

```text
http://localhost:8080
```

### 3. 启动前端静态服务器

进入 `frontend` 目录：

```bash
cd frontend
```

启动静态服务器：

```bash
python -m http.server 8081
```

### 4. 访问项目

浏览器打开：

```text
http://localhost:8081/index.html
```

## 后端接口说明

### 二手商品接口

| 方法 | 接口 | 说明 |
| --- | --- | --- |
| GET | `/products` | 查询二手商品列表 |
| GET | `/products/{id}` | 查询商品详情 |
| POST | `/products` | 发布商品 |
| GET | `/products/search` | 按关键词和分类搜索商品 |
| PUT | `/products/{id}/offline` | 下架商品 |

搜索示例：

```text
GET /products/search?keyword=教材&category=教材资料
```

### 校园服务接口

| 方法 | 接口 | 说明 |
| --- | --- | --- |
| GET | `/service-items` | 查询校园服务列表 |
| GET | `/service-items/{id}` | 查询服务详情 |
| POST | `/service-items` | 发布服务 |
| GET | `/service-items/search` | 按关键词和类型搜索服务 |
| PUT | `/service-items/{id}/offline` | 下架服务 |

搜索示例：

```text
GET /service-items/search?keyword=打印&type=打印复印
```

## 后续计划

- 图片上传
- 用户登录
- 管理员审核
- 新生指南模块
- 失物招领模块
- 校园服务信息整合
- 小程序版本

## 项目状态

当前项目是 MVP 版本，主要用于完成校园生活服务平台的基础功能验证，以及 Spring Boot、MyBatis、MySQL 和原生前端的前后端联调练习。

项目适合用于课程展示、个人学习总结、简历项目说明，也可以作为后续继续扩展校园应用的基础版本。
