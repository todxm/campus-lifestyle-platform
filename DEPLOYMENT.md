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

## 十、腾讯云真实部署记录

### 1. 部署结果

校园生活小站已经部署到腾讯云轻量应用服务器，并经过同学实际访问测试，目前可以正常打开和使用。

本次实际部署环境如下：

- 服务器：腾讯云轻量应用服务器
- 操作系统：Ubuntu 22.04 LTS
- 后端：Spring Boot 可执行 JAR
- 后端运行端口：`8080`
- 后端运行方式：由 `systemd` 常驻管理
- 前端：`frontend` 目录中的静态页面
- 前端运行方式：由 Nginx 托管
- 前端访问端口：`80`
- 数据库：MySQL
- 数据库名：`campus_market`

线上访问地址：

```text
http://115.159.47.131/home.html
```

后端接口根地址：

```text
http://115.159.47.131:8080
```

例如，商品列表和校园服务列表接口分别为：

```text
http://115.159.47.131:8080/products
http://115.159.47.131:8080/service-items
```

> 当前已经完成 systemd 和 Nginx 常驻部署，访问前端时不再需要 `8081`。目前仍使用公网 IP，还没有配置域名和 HTTPS。

### 2. 实际部署方式

本次部署没有直接通过 OrcaTerm 上传完整 JAR，而是从 GitHub 将代码拉取到服务器，再在服务器上执行 Maven Wrapper 打包。

从 GitHub 拉取代码：

```bash
git clone https://github.com/todxm/campus-lifestyle-platform.git campus-lifestyle-platform-new
```

进入项目目录：

```bash
cd ~/campus-lifestyle-platform-new
```

给 Maven Wrapper 增加执行权限并打包：

```bash
chmod +x mvnw
./mvnw clean package
```

打包完成后的后端 JAR 位于：

```text
target/campus-market-backend-0.0.1-SNAPSHOT.jar
```

### 3. 后端启动命令

先进入 JAR 所在目录，例如：

```bash
cd ~/campus-lifestyle-platform-new/target
```

后台启动后端：

```bash
nohup java -jar campus-market-backend-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url="jdbc:mysql://localhost:3306/campus_market?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai" \
  --spring.datasource.username=campus_user \
  --spring.datasource.password=<DB_PASSWORD> \
  > backend.log 2>&1 &
```

`<DB_PASSWORD>` 必须在服务器上替换为实际数据库密码。不要将真实密码写入本文档，也不要提交到 GitHub。

### 4. 前端启动命令

进入实际存放前端页面的目录：

```bash
cd ~/campus-lifestyle-platform/frontend
```

临时启动前端：

```bash
python3 -m http.server 8081
```

该命令会占用当前终端。临时后台运行可以使用：

```bash
nohup python3 -m http.server 8081 > frontend.log 2>&1 &
```

当前 Python 静态服务器适合测试。后续正式部署应改用 Nginx。

## 十一、部署问题与解决方式

### 1. OrcaTerm 上传 JAR 卡住

**现象：** 通过腾讯云 OrcaTerm 上传较大的后端 JAR 时长时间没有完成。

**解决方式：** 不再直接上传 JAR，改为从 GitHub 克隆项目代码，然后在服务器上执行：

```bash
chmod +x mvnw
./mvnw clean package
```

这样可以直接在服务器生成最新 JAR。

### 2. 前端目录多套了一层 `frontend`

**现象：** 上传前端后，服务器目录中出现了类似 `frontend/frontend` 的结构，导致启动目录与访问路径不一致。

**解决方式：** 调整目录结构，确保 `home.html`、`index.html` 等页面直接位于实际提供静态服务的 `frontend` 目录中。启动前可以执行：

```bash
pwd
ls
```

确认当前目录中能直接看到 `home.html`，再执行 `python3 -m http.server 8081`。

### 3. 前端能打开但数据加载失败

**现象：** 静态页面可以访问，但商品列表和校园服务列表显示加载失败。

**原因：** 前端仍请求 `http://localhost:8080`。页面部署到服务器后，访问者浏览器中的 `localhost` 代表访问者自己的电脑，并不代表腾讯云服务器。

**解决方式：** 将线上前端请求地址改为服务器后端地址：

```text
http://115.159.47.131:8080
```

### 4. 后端跨域配置只允许本地前端

**现象：** 直接访问后端接口可以返回数据，但前端通过 JavaScript 请求接口时被浏览器拦截。

**原因：** 后端原来只允许以下来源：

```text
http://localhost:8081
```

**解决方式：** 在商品和校园服务 Controller 的跨域配置中同时允许：

```text
http://localhost:8081
http://115.159.47.131:8081
```

修改后重新打包并重启后端。

### 5. 后端端口 `8080` 被旧进程占用

**现象：** 新后端启动失败，日志提示 `8080` 端口已经被占用。

**解决方式：** 先停止旧的校园生活小站后端：

```bash
pkill -f campus-market-backend
```

确认 `8080` 端口已经释放后，再启动新 JAR。

### 6. 数据库用户权限或密码错误导致 HTTP 500

**现象：** 后端可以启动，但访问商品或服务接口时返回 HTTP 500，日志中出现 MySQL 认证或权限错误。

**原因：** `campus_user` 的密码与启动参数不一致，或者该用户没有访问 `campus_market` 数据库的权限。

**解决方式：** 在 MySQL 中重新核对 `campus_user` 的密码和数据库权限，并确保后端启动参数使用相同密码。数据库用户至少需要拥有项目正常读写 `campus_market` 所需的权限。

不要在 GitHub、部署文档或公开聊天记录中保存真实数据库密码。

### 7. 前端端口 `8081` 未启动时出现 502

**现象：** 访问前端地址时显示 502。

**原因：** `8081` 端口没有正在运行的前端静态服务器，或后续代理无法连接该端口。

**解决方式：** 进入正确的前端目录并重新启动：

```bash
cd ~/campus-lifestyle-platform/frontend
python3 -m http.server 8081
```

然后检查 `8081` 是否已经监听。

## 十二、服务器常用检查和运维命令

### 1. 查看端口

查看后端 `8080` 端口：

```bash
ss -lntp | grep 8080
```

查看前端 `8081` 端口：

```bash
ss -lntp | grep 8081
```

如果命令没有输出，通常表示对应端口没有程序监听。

### 2. 停止后端

```bash
pkill -f campus-market-backend
```

执行后使用 `ss -lntp | grep 8080` 确认端口已经释放。

### 3. 启动后端

```bash
nohup java -jar campus-market-backend-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url="jdbc:mysql://localhost:3306/campus_market?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai" \
  --spring.datasource.username=campus_user \
  --spring.datasource.password=<DB_PASSWORD> \
  > backend.log 2>&1 &
```

### 4. 查看后端日志

```bash
tail -n 40 backend.log
```

需要持续查看新增日志时可以使用：

```bash
tail -f backend.log
```

### 5. 启动前端

```bash
cd ~/campus-lifestyle-platform/frontend
python3 -m http.server 8081
```

### 6. 从 GitHub拉取代码

首次下载项目：

```bash
git clone https://github.com/todxm/campus-lifestyle-platform.git campus-lifestyle-platform-new
```

如果项目目录已经存在，后续应在确认本地没有未保存修改后使用 `git pull` 更新，不要反复克隆到多个目录。

### 7. 在服务器打包后端

进入项目根目录后执行：

```bash
chmod +x mvnw
./mvnw clean package
```

打包成功后，新的 JAR 位于：

```text
target/campus-market-backend-0.0.1-SNAPSHOT.jar
```

## 十三、后续优化计划

当前部署已经满足 MVP 测试，后端 systemd 常驻和前端 Nginx 托管均已完成。后续建议：

- 使用 Nginx 反向代理后端接口，避免前端硬编码服务器 IP 和 `8080` 端口
- 配置域名和 HTTPS
- 仅开放必要的公网端口，不向公网开放 MySQL `3306`
- 使用环境变量或受权限保护的配置文件保存数据库密码
- 定期备份 `campus_market` 数据库

## 十四、服务常驻部署方案

本章记录将临时运行方式升级为稳定线上运行方式的实施方案。目前该方案已经完成：

- 后端由 `systemd` 管理 Spring Boot JAR
- 前端由 Nginx 托管静态页面

当前正式前端访问地址：

```text
http://115.159.47.131/home.html
```

当前后端接口根地址：

```text
http://115.159.47.131:8080
```

> 下面保留完整实施命令，供后续维护或重新部署参考。再次执行停止、重启或切换操作时，线上服务可能会短暂中断。

### 1. 为什么要做服务常驻

当前后端使用 `nohup java -jar`，前端使用 `python3 -m http.server 8081`。这两种方式适合开发和短期测试，但存在以下问题：

- 服务器重启后，原来的进程通常不会自动恢复
- 进程异常退出后，不会自动重启
- 不方便统一查看服务状态和日志
- Python 静态服务器不是正式生产环境的 Web 服务器
- 手动管理进程容易出现重复启动和端口占用

进入正式测试阶段后，建议：

- 使用 `systemd` 管理后端，实现开机自启、异常重启和统一日志查看
- 使用 Nginx 托管前端，实现稳定的静态文件访问

### 2. 切换前检查

先确认当前 JAR 可以正常运行：

```bash
java -version
ls -lh /home/ubuntu/campus-lifestyle-platform/backend/campus-market-backend-0.0.1-SNAPSHOT.jar
```

确认 Java 版本为 17，并且 JAR 文件确实存在。

检查当前端口：

```bash
ss -lntp | grep 8080
ss -lntp | grep 8081
```

检查前端目录中的主要页面：

```bash
ls -lh /home/ubuntu/campus-lifestyle-platform/frontend
```

至少应能看到：

```text
home.html
index.html
service.html
detail.html
service-detail.html
```

### 3. 后端 systemd 方案

后端部署信息：

```text
JAR 文件：
/home/ubuntu/campus-lifestyle-platform/backend/campus-market-backend-0.0.1-SNAPSHOT.jar

运行端口：
8080

systemd 服务文件：
/etc/systemd/system/campus-market-backend.service
```

#### 3.1 创建数据库环境变量文件

真实数据库密码不要直接写进 Git 仓库或本文档。建议将数据库配置放在服务器的独立环境变量文件中：

```bash
sudo nano /etc/campus-market-backend.env
```

填写以下内容，将 `<DB_PASSWORD>` 替换为服务器上的真实数据库密码：

```text
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/campus_market?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
SPRING_DATASOURCE_USERNAME=campus_user
SPRING_DATASOURCE_PASSWORD=<DB_PASSWORD>
```

保存后限制文件权限，避免普通用户读取数据库密码：

```bash
sudo chown root:root /etc/campus-market-backend.env
sudo chmod 600 /etc/campus-market-backend.env
```

#### 3.2 创建 systemd 服务文件

执行：

```bash
sudo nano /etc/systemd/system/campus-market-backend.service
```

写入以下内容：

```ini
[Unit]
Description=Campus Market Spring Boot Backend
After=network.target mysql.service
Wants=mysql.service

[Service]
Type=simple
User=ubuntu
Group=ubuntu
WorkingDirectory=/home/ubuntu/campus-lifestyle-platform/backend
EnvironmentFile=/etc/campus-market-backend.env
ExecStart=/usr/bin/java -jar /home/ubuntu/campus-lifestyle-platform/backend/campus-market-backend-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
```

配置说明：

- `User=ubuntu`：使用普通用户运行后端，不使用 root
- `WorkingDirectory`：后端运行目录
- `EnvironmentFile`：读取受保护的数据库配置
- `Restart=on-failure`：后端异常退出后自动重启
- `After` 和 `Wants`：让后端在网络和 MySQL 服务可用后启动

确认 Java 的实际路径：

```bash
which java
```

如果输出不是 `/usr/bin/java`，需要将服务文件中的 `ExecStart` 改为实际路径。

#### 3.3 停止旧的 nohup 后端

切换到 systemd 前，先停止旧后端，避免 `8080` 端口冲突：

```bash
pkill -f campus-market-backend
```

确认端口已经释放：

```bash
ss -lntp | grep 8080
```

此操作会暂时中断后端接口，应尽快完成后续启动和验证。

#### 3.4 加载并启动 systemd 服务

每次新增或修改服务文件后，都需要重新加载 systemd 配置：

```bash
sudo systemctl daemon-reload
```

设置开机自启并立即启动：

```bash
sudo systemctl enable --now campus-market-backend
```

检查状态：

```bash
sudo systemctl status campus-market-backend
```

如果进入分页状态页面，按 `q` 退出。也可以避免进入分页页面：

```bash
sudo systemctl status campus-market-backend --no-pager
```

测试接口：

```bash
curl http://localhost:8080/products
curl http://localhost:8080/service-items
```

#### 3.5 systemd 常用命令

启动后端：

```bash
sudo systemctl start campus-market-backend
```

停止后端：

```bash
sudo systemctl stop campus-market-backend
```

重启后端：

```bash
sudo systemctl restart campus-market-backend
```

查看状态：

```bash
sudo systemctl status campus-market-backend
```

持续查看日志：

```bash
journalctl -u campus-market-backend -f
```

查看最近 100 行日志：

```bash
journalctl -u campus-market-backend -n 100 --no-pager
```

更新 JAR 后，应执行：

```bash
sudo systemctl restart campus-market-backend
```

### 4. 前端 Nginx 方案

前端部署信息：

```text
前端文件目录：
/home/ubuntu/campus-lifestyle-platform/frontend

前端入口：
home.html

Nginx 访问端口：
80
```

使用 Nginx 后，前端地址计划从：

```text
http://115.159.47.131:8081/home.html
```

变为：

```text
http://115.159.47.131/home.html
```

也可以将 `home.html` 设置为默认首页，直接访问：

```text
http://115.159.47.131/
```

#### 4.1 安装并检查 Nginx

```bash
sudo apt update
sudo apt install nginx -y
sudo systemctl enable nginx
```

检查状态：

```bash
sudo systemctl status nginx --no-pager
```

#### 4.2 确认 Nginx 可以读取前端文件

Nginx 进程需要读取前端目录。先确保文件对 Web 服务器可读：

```bash
sudo chmod o+x /home/ubuntu
sudo chmod -R a+rX /home/ubuntu/campus-lifestyle-platform/frontend
```

这些命令只增加进入目录和读取静态文件所需的权限，不应给其他用户增加写权限。

#### 4.3 创建 Nginx 配置

创建站点配置：

```bash
sudo nano /etc/nginx/sites-available/campus-market
```

写入：

```nginx
server {
    listen 80;
    listen [::]:80;

    server_name 115.159.47.131;

    root /home/ubuntu/campus-lifestyle-platform/frontend;
    index home.html;

    location / {
        try_files $uri $uri/ /home.html;
    }
}
```

本阶段只让 Nginx 提供前端静态页面，不代理 MySQL，也不把 MySQL `3306` 端口暴露到公网。

#### 4.4 启用配置

创建配置链接：

```bash
sudo ln -s /etc/nginx/sites-available/campus-market /etc/nginx/sites-enabled/campus-market
```

如果不再需要 Nginx 默认站点，可以移除默认站点链接：

```bash
sudo rm /etc/nginx/sites-enabled/default
```

删除前应确认该路径只是 Nginx 默认配置链接，不是项目文件或业务数据。

检查 Nginx 配置：

```bash
sudo nginx -t
```

只有看到配置检查成功后，才能重新加载 Nginx：

```bash
sudo systemctl reload nginx
```

访问测试：

```text
http://115.159.47.131/
http://115.159.47.131/home.html
```

#### 4.5 跨域配置提醒

当前前端在 `8081` 端口运行时，浏览器请求来源是：

```text
http://115.159.47.131:8081
```

切换到 Nginx 的 `80` 端口后，请求来源会变成：

```text
http://115.159.47.131
```

如果后端跨域配置只允许带 `:8081` 的地址，Nginx 页面虽然能打开，但商品和服务数据可能加载失败。

因此，正式切换前需要检查后端跨域配置是否允许 `http://115.159.47.131`。如果需要修改，应先说明对本地和线上环境的影响，重新打包后端并重启 systemd 服务。本章不修改后端代码。

#### 4.6 停止旧的 Python 前端

确认 Nginx 页面和数据都正常后，才停止旧的 Python 静态服务器：

```bash
pkill -f "python3 -m http.server 8081"
```

然后确认 `8081` 已停止、Nginx 的 `80` 端口仍正常：

```bash
ss -lntp | grep 8081
ss -lntp | grep ':80 '
```

如果 Nginx 尚未验证成功，不要先停止 `8081`，避免线上前端完全无法访问。

### 5. 腾讯云安全组和端口

Nginx 切换完成后，需要确保腾讯云轻量应用服务器防火墙或安全组允许：

- `80`：HTTP 前端访问
- `22`：SSH 管理
- `443`：后续 HTTPS

当前前端仍直接请求后端 `8080` 时，`8080` 仍需允许访问。后续如果使用 Nginx 反向代理后端 API，可以再考虑关闭 `8080` 的公网访问。

不要开放 MySQL `3306` 到公网，Nginx 也不要代理 MySQL。

### 6. 推荐切换顺序

为了尽量缩短线上服务中断时间，建议按以下顺序执行：

1. 确认 JAR、Java 17、MySQL 和前端文件正常。
2. 创建受保护的后端数据库环境变量文件。
3. 创建并检查 systemd 服务文件。
4. 停止旧的 nohup 后端。
5. 启动 systemd 后端，并立即测试两个主要接口。
6. 安装 Nginx，创建配置并执行 `sudo nginx -t`。
7. 检查后端跨域是否允许不带 `:8081` 的前端来源。
8. 重新加载 Nginx，验证首页、商品列表和校园服务列表。
9. 全部验证成功后，再停止 Python `8081` 前端。
10. 最后检查 `systemctl` 状态和端口监听情况。

切换完成后的目标状态：

```text
后端：systemd 管理，监听 8080
前端：Nginx 管理，监听 80
数据库：MySQL 仅供服务器本机后端访问
```

### 7. 当前常用运维命令

查看后端状态：

```bash
sudo systemctl status campus-market-backend --no-pager
```

重启后端：

```bash
sudo systemctl restart campus-market-backend
```

查看后端最近 100 行日志：

```bash
sudo journalctl -u campus-market-backend -n 100 --no-pager
```

查看 Nginx 状态：

```bash
sudo systemctl status nginx --no-pager
```

重启 Nginx：

```bash
sudo systemctl restart nginx
```

当前仍未配置域名和 HTTPS。数据库密码不得写入本文档或 Git 仓库，所有示例继续统一使用：

```text
<DB_PASSWORD>
```
