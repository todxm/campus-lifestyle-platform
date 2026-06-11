# 南航上海路校园生活小站部署说明

本文档记录校园生活小站从本地运行到云服务器部署的基本步骤，适合第一次部署 Spring Boot 项目的同学参考。

> 注意：文档中的用户名、密码、服务器 IP 和目录需要根据实际环境修改。

## 一、项目组成

项目主要分为三部分：

- 后端：Spring Boot + MyBatis，默认端口为 `8080`
- 前端：`frontend` 目录中的原生 HTML、CSS、JavaScript 页面
- 数据库：MySQL，数据库名为 `campus_market`

## 二、本地运行和检查

正式上传服务器前，建议先在本地确认后端、数据库和前端都能正常运行。

### 1. 启动 MySQL

启动本机 MySQL 服务，确认已经创建 `campus_market` 数据库，并检查
`src/main/resources/application.yaml` 中的数据库地址、用户名和密码是否正确。

### 2. 打包 Spring Boot 后端

在项目根目录执行：

```powershell
.\mvnw.cmd clean package
```

如果系统提示找不到 `powershell`，可以在当前终端临时补充 PowerShell 路径：

```powershell
$env:Path = "C:\Windows\System32\WindowsPowerShell\v1.0;$env:Path"
.\mvnw.cmd clean package
```

打包成功后会生成：

```text
target/campus-market-backend-0.0.1-SNAPSHOT.jar
```

### 3. 本地使用 JAR 启动后端

在项目根目录执行：

```powershell
java -jar .\target\campus-market-backend-0.0.1-SNAPSHOT.jar
```

后端默认访问地址：

```text
http://localhost:8080
```

例如，可以在浏览器中检查商品接口：

```text
http://localhost:8080/products
```

### 4. 本地启动前端静态页面

打开一个新的终端，进入前端目录：

```powershell
cd frontend
python -m http.server 8081
```

然后访问：

```text
http://localhost:8081/home.html
```

此命令只适合本地开发和临时测试，不建议作为正式生产环境的启动方式。

## 三、云服务器准备

建议准备一台 Linux 云服务器。服务器至少需要：

- 公网 IP
- Java 17
- MySQL
- Python 3，用于临时启动前端
- 后续正式部署使用的 Nginx
- 可通过 SSH 连接服务器的账号

还需要在云平台的安全组和服务器防火墙中按需开放端口：

- `22`：SSH 登录
- `8080`：Spring Boot 后端临时访问端口
- `8081`：前端临时访问端口
- `80`：Nginx HTTP 访问端口
- `443`：后续配置 HTTPS 时使用

生产环境中不建议把 MySQL 的 `3306` 端口直接开放给所有公网地址。

### 1. 检查 Java 17

安装完成后执行：

```bash
java -version
```

输出中应能看到 Java 17。Spring Boot 后端运行时必须使用与项目兼容的 Java 版本。

### 2. 检查 MySQL

安装完成后执行：

```bash
mysql --version
```

然后确认 MySQL 服务已经启动。

## 四、创建和初始化数据库

### 1. 登录 MySQL

```bash
mysql -u root -p
```

输入 MySQL 密码后进入数据库命令行。

### 2. 创建数据库

```sql
CREATE DATABASE IF NOT EXISTS campus_market
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

执行完成后可以检查：

```sql
SHOW DATABASES;
```

### 3. 上传并导入示例数据

将项目根目录中的以下文件上传到服务器：

```text
campus_market_mvp_data.sql
```

假设上传到 `/opt/campus-market/`，可以执行：

```bash
mysql -u root -p campus_market < /opt/campus-market/campus_market_mvp_data.sql
```

该 SQL 文件会清理并重新插入 MVP 示例数据，执行前应确认数据库中没有需要保留的正式数据。

> 如果 SQL 文件只包含测试数据，没有建表语句，需要先确认 `product` 和 `service_item` 表已经创建。

## 五、上传项目文件

建议在服务器创建统一目录：

```bash
sudo mkdir -p /opt/campus-market/backend
sudo mkdir -p /opt/campus-market/frontend
```

需要从本地上传：

1. 后端 JAR：

```text
target/campus-market-backend-0.0.1-SNAPSHOT.jar
```

建议上传到：

```text
/opt/campus-market/backend/campus-market-backend-0.0.1-SNAPSHOT.jar
```

2. 整个前端目录：

```text
frontend/
```

建议将其中的页面上传到：

```text
/opt/campus-market/frontend/
```

3. 数据库示例脚本：

```text
campus_market_mvp_data.sql
```

可以使用云服务器控制台上传，也可以在本地使用 `scp`、WinSCP 或其他文件传输工具。

## 六、配置并启动后端

### 1. 检查数据库连接

服务器上的后端必须连接服务器 MySQL，不能继续使用错误的本地数据库地址。

需要确认以下信息与服务器环境一致：

- 数据库地址
- 数据库端口
- 数据库名 `campus_market`
- MySQL 用户名
- MySQL 密码

正式部署时不建议把真实数据库密码提交到 GitHub。后续可以使用环境变量或服务器上的独立配置文件保存敏感信息。

### 2. 启动后端

进入 JAR 所在目录：

```bash
cd /opt/campus-market/backend
```

前台启动并观察日志：

```bash
java -jar campus-market-backend-0.0.1-SNAPSHOT.jar
```

看到类似 `Started CampusMarketBackendApplication` 的日志，通常表示启动成功。

可以在服务器本机检查接口：

```bash
curl http://localhost:8080/products
```

临时在后台启动可以使用：

```bash
nohup java -jar campus-market-backend-0.0.1-SNAPSHOT.jar > backend.log 2>&1 &
```

查看后端日志：

```bash
tail -f backend.log
```

`nohup` 适合临时测试。正式部署建议使用 `systemd` 管理后端服务，实现开机启动、异常重启和统一日志管理。

## 七、临时启动前端

进入服务器上的前端目录：

```bash
cd /opt/campus-market/frontend
```

临时启动：

```bash
python -m http.server 8081
```

如果服务器命令是 `python3`，则执行：

```bash
python3 -m http.server 8081
```

浏览器访问：

```text
http://服务器公网IP:8081/home.html
```

此时前端代码中的后端接口地址不能继续指向访问者电脑上的 `localhost:8080`。因为浏览器中的 `localhost` 代表用户自己的电脑，不是云服务器。

正式上线前，需要将前端 API 地址改为云服务器后端地址，或者使用 Nginx 反向代理统一前后端访问地址。

## 八、后续使用 Nginx 正式部署

Python 静态服务器只适合测试。正式部署建议使用 Nginx：

- Nginx 提供 `frontend` 静态页面
- Nginx 将后端接口请求转发到 Spring Boot 的 `8080` 端口
- 用户只需要访问 `80` 或 `443` 端口
- 可以绑定域名并配置 HTTPS
- Spring Boot 的 `8080` 端口可以只允许服务器内部访问

推荐的访问结构：

```text
用户浏览器
    |
    v
Nginx（80/443）
    |-- frontend 静态页面
    |
    `-- Spring Boot（127.0.0.1:8080）
            |
            `-- MySQL（campus_market）
```

后续正式部署时，还应完成：

- 使用 Nginx 将默认首页设置为 `home.html`
- 配置后端接口反向代理
- 使用 `systemd` 管理 Spring Boot
- 申请域名并完成备案（根据服务器地区和服务商要求）
- 配置 HTTPS 证书
- 限制数据库公网访问
- 定期备份 MySQL 数据
- 将数据库密码等敏感配置移出 Git 仓库

## 九、最终访问目标

完成正式部署后，希望同学可以直接通过公网链接访问，例如：

```text
https://你的域名/home.html
```

或者：

```text
http://服务器公网IP/home.html
```

访问者不需要安装 Java、MySQL、Python 或开发工具，只需使用手机或电脑浏览器即可进入校园生活小站。
