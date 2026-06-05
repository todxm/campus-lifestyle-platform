# 南航上海路校园生活小站

一个面向校园场景的生活服务平台 MVP，当前包含“二手闲置”和“校园服务”两个模块。

## 技术栈

- 后端：Spring Boot、MyBatis
- 数据库：MySQL
- 前端：原生 HTML、CSS、JavaScript
- 构建工具：Maven

## 已实现功能

### 二手闲置模块

- 商品列表展示
- 商品关键词和分类搜索
- 商品详情查看
- 发布商品
- 下架商品

### 校园服务模块

- 服务列表展示
- 服务关键词和类型搜索
- 服务详情查看
- 发布服务
- 下架服务

## 前端页面

- `frontend/index.html`：二手闲置列表页
- `frontend/detail.html`：商品详情页
- `frontend/publish.html`：发布商品页
- `frontend/service.html`：校园服务列表页
- `frontend/service-detail.html`：服务详情页
- `frontend/service-publish.html`：发布服务页

## 后端接口

### 商品接口

- `GET /products`：查询商品列表
- `GET /products/{id}`：查询商品详情
- `GET /products/search?keyword=xxx&category=xxx`：搜索商品
- `POST /products`：发布商品
- `PUT /products/{id}/offline`：下架商品

### 服务接口

- `GET /service-items`：查询服务列表
- `GET /service-items/{id}`：查询服务详情
- `GET /service-items/search?keyword=xxx&type=xxx`：搜索服务
- `POST /service-items`：发布服务
- `PUT /service-items/{id}/offline`：下架服务

## 运行方式

### 1. 准备数据库

启动 MySQL，并创建项目使用的数据库。数据库连接配置在：

```text
src/main/resources/application.yaml
```

如果你的 MySQL 用户名、密码、数据库名不同，需要先修改这里的配置。

### 2. 启动后端

在项目根目录运行：

```bash
.\mvnw.cmd spring-boot:run
```

后端默认启动在：

```text
http://localhost:8080
```

### 3. 启动前端静态服务

进入 `frontend` 目录：

```bash
cd frontend
```

启动静态服务：

```bash
python -m http.server 8081
```

前端默认访问地址：

```text
http://localhost:8081/index.html
```

## 主要目录结构

```text
src/main/java/com/tod/campusmarketbackend
├── controller   接口控制层
├── entity       实体类
├── mapper       MyBatis Mapper 接口
└── service      业务逻辑层

src/main/resources/mapper
├── ProductMapper.xml
└── ServiceItemMapper.xml

frontend
├── index.html
├── detail.html
├── publish.html
├── service.html
├── service-detail.html
└── service-publish.html
```

## 当前状态

这是一个用于学习和演示的 MVP 版本，重点是完成基础的前后端联调流程。
