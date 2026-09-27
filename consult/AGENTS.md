# AGENTS.md — consult（AI 智能问诊服务）

> **进入本模块前，请先阅读根目录 `AGENTS.md`（尤其 §0 三条铁律）。**
> 负责人：**后端 C（AI 与病理）**

---

## 1. 模块职责

AI 智能问诊服务 —— 基于 LangChain4j 的医疗问答 Agent，具备 RAG 检索、会话记忆与 Function Calling 挂号能力。

| 项 | 值 |
|---|---|
| 端口 | **8080**（无 context-path） |
| 技术栈 | Spring Boot 3.2.4 + WebFlux + LangChain4j 1.0.0-beta3 + MyBatis-Plus |
| 大模型 | 阿里云百炼 DashScope：`qwen3-max`（对话/流式）、`text-embedding-v3`（向量） |
| 向量库 | Pinecone（index `xiaozhi-index`，namespace `xiaozhi-namespace`） |
| 会话记忆 | MongoDB `chat_memory_db`（`MongoChatMemoryStore`） |
| 业务库 | MySQL `guiguxiaozhi`（表 `appointment`） |

---

## 2. 包结构

```
com.ganpo.consult
├── ConsultApplication.java          启动类（@SpringBootApplication）
├── assistant/                       AI 服务接口（LangChain4j @AiService）
│   ├── XiaozhiAgent.java            ★ 生产对话入口（流式 + 记忆 + RAG + 工具）
│   ├── MemoryChatAssistant.java     演示用（提示词写死"用上海话回答"）
│   ├── SeparateChatAssistant.java   ⚠️ 已停用，见 §5
│   └── Assistant.java               演示用（无记忆无工具）
├── config/
│   ├── XiaozhiAgentConfig.java      ChatMemoryProvider + ContentRetriever
│   ├── EmbeddingStoreConfig.java    Pinecone 向量库
│   ├── MemoryChatAssistantConfig.java
│   ├── SeparateChatAssistantConfig.java  ⚠️ 内部 chatMemoryProvider 已被注释
│   └── CorsConfig.java              ⚠️ 使用了 "*" + allowCredentials（需修）
├── controller/XiaozhiController.java  HTTP 入口
├── tools/
│   ├── AppoinmentTools.java         ★ Function Calling：预约/取消预约/查号源
│   └── CalculatorTools.java
├── store/MongoChatMemoryStore.java  MongoDB 会话记忆持久化
├── entity/Appointment.java          无 @TableName（MP 按类名映射到 appointment 表）
├── bean/ChatMessages.java           MongoDB 文档 @Document("chat_messages")
├── mapper/AppointmentMapper.java    （XML 为空）
├── service/                         AppointmentService 等
└── dto/MessageDTO.java
```

---

## 3. 对外接口

| 方法 | 路径 | 功能 | 备注 |
|---|---|---|---|
| POST | `/xiaozhi/chat` | 流式智能问诊 | `produces = "text/stream;charset=utf-8"` ⚠️ **非标准媒体类型**，前端需自定义解析；计划改为标准 SSE |
| GET | `/xiaozhi/history/{memoryId}` | 读取会话历史 | 从 MongoDB 取 |
| DELETE | `/xiaozhi/history/{memoryId}` | 清除会话历史 | ⚠️ **空壳实现**，未真正删除 Mongo 记录 |

**本模块无任何鉴权。**

---

## 4. 配置

`src/main/resources/application.properties`

| 配置 | 说明 |
|---|---|
| `server.port=8080` | |
| `spring.application.name=consult` | 原为 `java-ai-langchain4j`（课程项目残留），已改 |
| `DASHSCOPE_API_KEY` | **必须由环境变量提供**，否则 AI 链路不可用 |
| `PINECONE_API_KEY` | 向量库 Key（代码从 `System.getenv` 读取） |
| `DB_*` | 默认 `localhost:3307` |
| `MONGODB_URI` | 默认 `mongodb://localhost:27017/chat_memory_db` |

Prompt 模板：
- `ganpo-prompt-template1.txt`（58 行，**内容更完整，推荐使用**）
- `ganpo-prompt-template.txt`（21 行，旧版）

---

## 5. ⚠️ 已知问题（改动前必读）

| # | 问题 | 说明 |
|---|---|---|
| 1 | **`SeparateChatAssistant` 已整体注释停用** | 它依赖的 `chatMemoryProvider` Bean 在 `SeparateChatAssistantConfig` 中被注释；`my-prompt-template.txt`/`my-prompt-template3.txt` 资源不存在。**补齐依赖前不要启用** |
| 2 | **`AppoinmentTools.queryDepartment()` 是假实现** | 只 `System.out.println` 后 `return true`。这是**真实业务缺口**（号源查询），需改为真实实现 |
| 3 | **预约数据归属错误** | 本模块自己维护 `appointment` 表，但 `medical-resource` 才是预约的权威服务（模型更完整，含 `ticketNumber`/`timeslot`）。**计划：删除本模块的 Appointment 相关代码，改为 Feign 调用 `medical-resource`** |
| 4 | **`DELETE /xiaozhi/history` 未真正删除数据** | 需实现 |
| 5 | **`CorsConfig` 过宽** | `addAllowedOriginPattern("*")` + `allowCredentials(true)`，浏览器会直接拒绝；且计划由网关统一管理 CORS |
| 6 | **同时引入 `spring-boot-starter-web` 与 `webflux`** | 导致 Spring MVC 优先，`Flux` 流式受限于 servlet 栈。计划改为标准 SSE |
| 7 | **`AppointmentServiceImpl.getOne()` 字段错配** | 用 `Appointment::getIdCard` 去比 `appointment.getUsername()` |
| 8 | 双版本 open-ai starter 的历史问题 | **已修复**（原 pom 声明了两次且版本不同） |
| 9 | `langchain4j-ollama-spring-boot-starter` | **已移除**（团队决定不用本地模型） |

---

## 6. 数据

| 存储 | 内容 |
|---|---|
| MySQL `guiguxiaozhi.appointment` | 预约记录（⚠️ 无建表脚本，计划废弃） |
| MongoDB `chat_memory_db.chat_messages` | 会话记忆（`_id`、`messageId`、`content`(JSON)、`memoryId`） |

---

## 7. 本地运行

```powershell
# 依赖：MySQL(3307) + MongoDB(27017) + DASHSCOPE_API_KEY
$env:DASHSCOPE_API_KEY = "<你的 Key>"
D:\JDK21\bin\java -jar consult\target\consult-0.0.1-SNAPSHOT.jar
```

> ⚠️ 缺 MongoDB 会导致 `MongoChatMemoryStore` 初始化失败、服务起不来。
