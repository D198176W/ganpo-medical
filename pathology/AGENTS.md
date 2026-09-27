# AGENTS.md — pathology（病理服务）

> **进入本模块前，请先阅读根目录 `AGENTS.md`（尤其 §0 三条铁律）。**
> 负责人：**后端 C（AI 与病理）**

---

## 1. 模块职责

病理知识问答、病理切片图像上传与分析。

| 项 | 值 |
|---|---|
| 端口 | **8082**（无 context-path） |
| 技术栈 | Spring Boot 3.2.4 + MyBatis-Plus 3.5.7 + Redis + spring-security-crypto |
| 数据库 | MySQL `pathology` + Redis |
| 接口数 | 8 个 |

> ✅ **本模块是全仓库唯一同时提供 `schema-mysql.sql` + `data-mysql.sql` 的模块**——建库时优先使用。

---

## 2. 包结构

```
com.ganpo.pathology
├── PathologyApplication.java
├── config/
│   ├── RedisConfig.java
│   ├── AsyncConfig.java             切片分析的异步线程池
│   ├── FilterConfig.java            注册 JwtFilter 到 /api/*
│   ├── PasswordConfig.java          BCryptPasswordEncoder
│   ├── MybatisPlusConfig.java
│   └── FixFactoryBeanObjectTypeConfig.java   ⚠️ 临时的依赖兼容补丁
├── filter/JwtFilter.java            ⚠️ 只解析不拦截，见 §5
├── controller/
│   ├── AuthController.java          /api/auth/login|register
│   ├── ChatController.java          /api/chat
│   ├── ImageController.java         /api/images/upload、/api/images/{id}/analysis
│   └── KnowledgeController.java     /api/knowledge
├── service/ + impl/
│   ├── ChatServiceImpl.java         ⚠️ LIKE 匹配 + 关键词硬编码，无模型调用
│   ├── ImageServiceImpl.java        ⚠️ 伪实现，见 §5
│   ├── AuthServiceImpl.java
│   └── KnowledgeServiceImpl.java
├── entity/  User / ChatMessage / KnowledgeArticle / ImageEntity / ImageAnalysis
├── mapper/  5 个 Mapper
├── dto/     AuthRequest / AuthResponse / ChatRequest / ChatResponse
└── util/JwtUtil.java                HS256，读 app.jwt.secret
```

---

## 3. 对外接口（8 个）

| 方法 | 路径 | 功能 | 鉴权 |
|---|---|---|---|
| POST | `/api/auth/login` | 登录签发 JWT | 免过滤 |
| POST | `/api/auth/register` | 注册（BCrypt） | 免过滤 |
| POST | `/api/chat` | 病理知识问答 | ⚠️ 见 §5 |
| POST | `/api/images/upload` | 切片上传（multipart）→ 落盘 + 异步"分析" | 取 `claims` attribute |
| GET | `/api/images/{imageId}/analysis` | 查询分析结果 | — |
| POST | `/api/knowledge` | 新增知识条目 | ⚠️ **实际无鉴权** |
| GET | `/api/knowledge/{id}` | 知识详情 | ⚠️ 实际无鉴权 |
| GET | `/api/knowledge/category/{cat}` | 按分类列表 | ⚠️ 实际无鉴权 |

> ⚠️ 前端 `PathologyService.vue` 调用的 **`/api/auth/verify` 在本模块中不存在**。

---

## 4. 数据库

库 `pathology`，有完整脚本：`src/main/resources/db/schema-mysql.sql` + `data-mysql.sql`

| 表 | 关键字段 |
|---|---|
| `users` | username、password、role、verified、institution、doctor_name |
| `chat_message` | session_id、sender、content、sources |
| `knowledge_article` | title、content、summary、category、source_url、evidence_level |
| `image_entity` | uploader_id、file_path、file_name、metadata、status |
| `image_analysis` | image_id、model_version、result、confidence、reviewer_id |
| `chat_session` | ⚠️ **死表**：脚本建了但**没有对应实体/Mapper**，需二选一（补实体或删表） |

**共 6 张表 DDL**，另有种子用户 + 1 条知识条目。

---

## 5. ⚠️ 已知问题（改动前必读）

| # | 问题 | 说明 |
|---|---|---|
| 1 | **图像分析是伪实现** | `ImageServiceImpl` 用 `Thread.sleep(3000)` + **写死诊断文本与 confidence**，无任何模型调用。**这是本模块最大的技术债**，计划改用 Qwen-VL 多模态做真实识别 |
| 2 | **问答是伪 AI** | `ChatServiceImpl` 用 MySQL `LIKE` 匹配 `knowledge_article` + 关键词硬编码兜底（如 `query.contains("鳞状细胞癌")`），**没有模型调用** |
| 3 | **`JwtFilter` 只解析不拦截** | `doFilterInternal` 在无 token 时**直接放行**；`shouldNotFilter` 只排除 `/api/auth` → **`POST /api/knowledge` 实际无鉴权**。计划统一为与 `medical-resource` 一致的 401 拦截 + 白名单 |
| 4 | `chat_session` 是死表 | 见 §4 |
| 5 | `FixFactoryBeanObjectTypeConfig` | 临时兼容补丁，仅 `medical-resource` 也有一份（重复）。**只应保留一份** |
| 6 | Redis 依赖已引入但**用途不明** | 需明确（缓存知识库？会话？）或移除 |
| 7 | 上传目录 | `app.storage.upload-dir=./uploads`，文件写本地磁盘。计划改用对象存储 |
| 8 | `users` 表与其他 2 个模块**重复** | 计划收归 `ganpo-auth` |

---

## 6. ✅ 本模块已修复（Phase 1）

`dto/ChatRequest.java` 与 `service/impl/ChatServiceImpl.java` 的冲突标记已清理。
**裁决取向：两侧统一取"另一侧"**——`userId` / `sessionId` 均为 **`String`**，`ChatServiceImpl` 用「提取数字 → 否则取 hash → 兜底 1L」的健壮逻辑转成 `long`。

> ⚠️ **不要**把 `ChatRequest` 的 `userId`/`sessionId` 改回 `Long`：HEAD 侧原为 `Long` 且直接 `1L` 兜底，前端传非数字字符串会 500。两文件**必须保持同侧**。

---

## 7. 本地运行

```powershell
# 依赖：MySQL(3307) 的 pathology 库（有现成脚本）+ Redis(6379)
# 1) 建库（注意把脚本里的库名确认为 pathology）
mysql -h 127.0.0.1 -P 3307 -u root -p < pathology\src\main\resources\db\schema-mysql.sql
mysql -h 127.0.0.1 -P 3307 -u root -p pathology < pathology\src\main\resources\db\data-mysql.sql

# 2) 启动
D:\JDK21\bin\java -jar pathology\target\pathology-0.0.1-SNAPSHOT.jar
```
