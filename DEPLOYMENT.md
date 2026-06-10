# 校园生活小站部署计划

本文档记录“南航上海路校园生活小站”从本地 MVP 逐步部署到公网的计划，便于后续配置服务器、数据库和访问地址。

## 当前本地运行方式

### 1. 启动 MySQL

启动本机 MySQL 服务，并确认已经创建 `campus_market` 数据库。

数据库连接配置位于：

```text
src/main/resources/application.yaml
```

如需导入 MVP 示例数据，可执行项目根目录下的：

```text
campus_market_mvp_data.sql
```

### 2. 启动 Spring Boot 后端

在项目根目录执行：

```bash
.\mvnw.cmd spring-boot:run
```

后端默认地址：

```text
http://localhost:8080
```

### 3. 启动前端静态服务器

进入前端目录：

```bash
cd frontend
```

启动静态服务器：

```bash
python -m http.server 8081
```

浏览器访问：

```text
http://localhost:8081/home.html
```

## Spring Boot 后端打包计划

部署前在项目根目录执行：

```bash
.\mvnw.cmd clean package
```

打包成功后，`target` 目录中会生成可运行的 JAR 文件。服务器上可以使用以下方式启动：

```bash
java -jar campus-market-backend.jar
```

正式部署时需要：

- 安装合适版本的 JDK
- 将数据库连接、账号和密码改为服务器环境配置
- 使用环境变量或独立配置文件保存敏感信息
- 使用 systemd、Docker 或其他进程管理工具保证后端持续运行
- 配置日志目录和服务自动重启

## 前端静态页面部署计划

`frontend` 目录中的页面是原生 HTML、CSS 和 JavaScript，可以部署到：

- Nginx 静态网站目录
- 云服务器静态目录
- 对象存储静态网站服务
- GitHub Pages（仅适合前端展示，后端接口仍需公网服务）

推荐使用 Nginx 托管前端页面，并将默认首页配置为：

```text
home.html
```

部署到公网后，需要把前端代码中的：

```text
http://localhost:8080
```

替换为真实的后端 API 地址，或通过 Nginx 反向代理统一为同一域名下的 `/api` 路径。

## MySQL 数据库部署计划

数据库可以选择：

- 安装在同一台云服务器上
- 使用云数据库 MySQL 服务

部署时需要完成：

- 创建 `campus_market` 数据库
- 创建业务所需的 `product` 和 `service_item` 表
- 设置 UTF-8 字符集，推荐 `utf8mb4`
- 导入初始化数据或 MVP 示例数据
- 创建专用数据库账号，不直接使用 root 账号运行项目
- 限制数据库公网访问范围
- 配置定期备份和数据恢复方案

## 云服务器准备清单

云服务器至少需要准备：

- Linux 云服务器
- 公网 IP
- JDK
- MySQL 或云数据库连接信息
- Nginx
- 开放 HTTP/HTTPS 端口
- 后端服务使用的内部端口
- 域名（可选但推荐）
- HTTPS 证书
- 防火墙和安全组规则

推荐部署结构：

```text
浏览器
  -> Nginx
     -> frontend 静态页面
     -> Spring Boot 后端接口
        -> MySQL 数据库
```

## 公网访问目标

后续目标是为项目配置一个稳定的公网访问地址，让南昌航空大学上海路校区的同学可以直接通过浏览器或手机链接访问，例如：

```text
https://your-domain.example/home.html
```

完成部署后，同学无需安装开发工具，也不需要启动本地 MySQL、Spring Boot 或 Python 静态服务器，即可使用二手闲置、校园服务和反馈建议等功能。

## 后续部署事项

- 将数据库密码等敏感配置移出 Git 仓库
- 配置生产环境跨域或 Nginx 反向代理
- 配置 HTTPS
- 增加用户登录和权限控制
- 增加图片上传与文件存储
- 配置数据库备份
- 增加服务监控和错误日志
