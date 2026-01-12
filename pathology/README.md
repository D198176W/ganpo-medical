# Pathology Service (MySQL + Redis) - Demo

说明：
- 包名：com.itguigu.pathology
- 技术栈：Spring Boot 3 + MyBatis-Plus + MySQL + Redis
- 功能（MVP）：JWT 登录/注册、病理问答（简单检索和保存消息）、知识库 CRUD、图片上传 + 异步分析模拟。

运行步骤（推荐）：
1. 启动 MySQL 与 Redis：
    - 方法 A（推荐）：在项目根目录运行：
      docker-compose up -d
      （会启动 MySQL（rootpassword）和 Redis）
    - 方法 B：自行准备 MySQL & Redis 服务并修改 application.yml 的连接信息。

2. 在 MySQL 中执行 SQL 脚本（resources/db/schema-mysql.sql / data-mysql.sql）：
    - 连接到 MySQL（例如使用 mysql client 或 Workbench），执行脚本创建表与初始数据。

3. 修改 application.yml 中的数据库用户名/密码（如果与 docker-compose 不符）。

4. 构建并运行：
   mvn spring-boot:run

接口示例：
- POST /api/auth/login  { "username":"doctor1", "password":"123456" }
- POST /api/auth/register { "username","password" }
- POST /api/chat  { "userId", "role", "query" }
- POST /api/knowledge  (管理员/医生创建知识文章)
- POST /api/images/upload  (multipart form-data, key=file)
- GET /api/images/{id}/analysis

注意：
- 目前 LLM/RAG 为 stub（演示用），后续可接真实服务替换 ChatServiceImpl 中逻辑。
- 图片存储在本地 ./uploads 目录，生产请替换为 S3/MinIO 并做权限控制。