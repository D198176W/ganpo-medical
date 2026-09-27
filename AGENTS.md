# AGENTS.md — ganpo-medical（赣鄱医枢）

> **本文件是 AI 编码助手的入口。请在开始任何改动前完整阅读。**
> 面向对象：本项目全体开发者及其 AI 编码助手（Claude Code / Cursor / DSH / Copilot 等）。
> 最后更新：2026-09-27

---

## 0. 两条铁律（违反会造成不可逆损失）

| # | 铁律 | 原因 |
|---|---|---|
| **1** | **构建/运行 Java 必须用 JDK 21** | 项目 `target` 是 Java 17，Spring Boot 3.2.4 官方支持上限为 21。本机 `PATH` 首位是 **JDK 25**，直接用 `java`/`mvn` 可能误用。构建走 `JAVA_HOME`（已指向 `D:\JDK21`），**运行 jar 时必须显式写 `D:\JDK21\bin\java`** |
| **2** | **不要把口令、密钥写进任何受版本控制的文件** | 仓库历史中曾出现明文数据库口令、JWT 弱密钥、`sk-` 开头的 API Key。全部改用 `${ENV_VAR:默认值}` 形式 |

---

## 1. 项目是什么

**赣鄱医枢** —— 南昌本地智能医疗**伴诊与判诊**平台，参加**全国计算机设计大赛（软件应用与开发赛道）**。

核心能力：医疗咨询 · 症状分析 · 科室推荐 · 号源查询 · 预约挂号 · 取消挂号 · 本地化医院知识库 · 紧急急诊引导。

产品人设与业务规则定义在：`consult/src/main/resources/ganpo-prompt-template1.txt`

---

## 2. 当前状态（重要：先读这里再动手）

项目原为一个早期半成品仓库，**后端从未编译成功过**（`target/` 下 0 个 `.class`）。已完成第一轮梳理：

### ✅ 已完成

- **清除全部 46 处 Git 冲突标记**（26 个文件）—— 这是后端此前无法编译的**首要原因**
- **修复 3 个致命 Bug**（详见 §7）
- **修复 pom 体系**：统一版本管理、消除重复依赖与未定义版本号、修正错误的 artifact 坐标
- **`mvn clean compile` 全量通过**，4 个模块共编译出 145 个 `.class`
- **删除废弃副本** `medical-app/`（含被误提交的 `node_modules`，共 13,728 个文件 / 171 MB）
- **删除垃圾文件**：`src/views/test.vue`、孤儿文件 `src/styles/global.css`、`dist/`
- **配置外部化**：数据库口令、JWT 密钥、AI Key 全部改为环境变量占位
- **重写 `.gitignore`**；`.idea/`、`settings.json` 已解除版本跟踪

### ⛔ 尚未开始（不要假设已存在）

- **项目无法启动**：4 个数据库（`guiguxiaozhi` / `health_management` / `pathology` / `medical_resource`）**本机都还不存在**，且 `health-management`、`consult` **没有建表脚本**
- **无注册中心 / 无网关**：4 个模块目前是各自独立的 Spring Boot 应用，端口 8080–8083
- **无统一响应体**：3 套不同的响应格式（见 §8）
- **无统一鉴权**：3 套互不兼容的 JWT 实现，`consult` 完全无鉴权
- **前端未对接后端**：健康记录 / 报告分析 / 紧急救助仍靠 `localStorage` 与静态数据
- **无测试 / 无 ESLint / 无 CI / 无 Docker**

> 完整现状与差距分析见 `docs/00-项目现状与差距分析.md`。

### 🎯 目标方向

微服务化改造：引入 Spring Cloud Alibaba 2023 + Nacos + Gateway，拆出 `common` / `common-web` / `api` 公共模块。
完整技术选型与落点见 `docs/01-技术选型与落点说明.md`，分阶段计划见 `docs/02-项目说明书.md`。

---

## 3. 目录地图

```
ganpo-medical/                        ← 项目根目录
├── AGENTS.md                         ← 你正在读的文件
├── pom.xml                           ← 父工程：所有依赖版本的唯一来源
├── README.md                         ← 面向人的快速开始
├── .gitignore
├── docs/                             ← 项目文档（先读这里）
│   ├── 00-项目现状与差距分析.md
│   ├── 01-技术选型与落点说明.md
│   ├── 02-项目说明书.md
│   └── 03-团队分工与协作规范.md
├── scripts/                          ← 启动/构建脚本
├── src/                              ← 前端 Vue 3 SPA（22,975 行 / 49 个 .vue）
│   ├── views/      17 个页面
│   ├── components/ 37 个组件
│   ├── router/ composables/ store/ utils/ assets/
├── consult/                          ← 后端① :8080  AI 智能问诊（LangChain4j）
├── health-management/                ← 后端② :8081  健康记录/报告/用户/医院
├── pathology/                        ← 后端③ :8082  病理知识问答 + 切片分析
└── medical-resource/                 ← 后端④ :8083  医院/科室/药店/报销/预约
```

**每个后端模块内都有独立的 `AGENTS.md`，进入模块前请一并阅读。**

---

## 4. 环境与命令

### 4.1 环境事实（本机实测）

| 项 | 值 |
|---|---|
| 项目根目录 | `D:\ideaAllCode\ganpo-medical\ganpo-medical` |
| JDK | `JAVA_HOME=D:\JDK21`（Maven 实际使用 21.0.11）；另有 JDK 1.8 / 11 / 25 |
| Maven | 3.9.4 |
| Node / npm | v24.15.0 / 11.12.1 |
| MySQL | 9.5.0 安装在 `D:\CodeTool\mysql-9.5.0-winx64` |
| **本项目数据库端口** | **3307**（独立实例，datadir `D:\CodeTool\mysql-ganpo-data`） |
| 前端端口 | 5173（Vite 默认） |
| 后端端口 | 8080 / 8081 / 8082 / 8083 |

> ⚠️ **Maven 本地仓库注意**：本机 `D:\Maven\apache-maven-3.9.4-bin\apache-maven-3.9.4\conf\settings.xml` 把 `localRepository` 改成了 `mvn_repo`（内容几乎为空），而项目依赖实际在默认位置 `C:\Users\<用户>\.m2\repository`。
> 因此在本机构建时需要显式指定：
> ```powershell
> mvn -B "-Dmaven.repo.local=$env:USERPROFILE\.m2\repository" clean package -DskipTests
> ```
> 若你的机器没有这个自定义配置，直接 `mvn clean package -DskipTests` 即可。

### 4.2 常用命令

```powershell
# 全量构建（跳过测试）
cd D:\ideaAllCode\ganpo-medical\ganpo-medical
mvn -B "-Dmaven.repo.local=$env:USERPROFILE\.m2\repository" clean package -DskipTests

# 只编译某个模块
mvn -B "-Dmaven.repo.local=$env:USERPROFILE\.m2\repository" -pl health-management compile

# 运行某个服务（注意必须显式用 JDK21 的 java）
D:\JDK21\bin\java -jar health-management\target\health-management-0.0.1-SNAPSHOT.jar

# 前端
cd src 所在的项目根目录
npm run dev          # 开发服务器
npm run build        # 生产构建
```

> 启动脚本（含 JDK、数据库、环境变量）规划放在 `scripts/`，见 `docs/02-项目说明书.md`。

---

## 5. 代码约定

### 5.1 包名

全仓库统一为 **`com.ganpo.*`**。已于本轮从旧包名 `com.atguigu.*` **全量迁移**（157 个文件：Java 包声明与 import、MyBatis XML 的 namespace、pom 的 groupId、yml 的 `type-aliases-package` 等）。

**禁止再引入 `com.atguigu.*`** —— 那是原课程项目的包名，已彻底移除；混用会导致编译失败。新增类请放在 `com.ganpo.<模块名>` 下。

### 5.2 语言与注释

- **注释与文档一律用中文**
- 代码标识符用英文，遵循 Java / Vue 社区规范

### 5.3 配置

- 数据库、JWT、AI Key 一律用 `${ENV_VAR:默认值}` 形式，**禁止硬编码**
- `health-management` 的 `jwt.secret` **必须是合法 Base64 字符串**（`JwtConfig` 会做 Base64 解码），其余模块为普通字符串

### 5.4 依赖版本

**所有依赖版本只能在根 `pom.xml` 的 `<properties>` + `<dependencyManagement>` 中声明。**
子模块的 `<dependency>` **不要写 `<version>`**（langchain4j 相关除外，用 `${langchain4j.version}`）。
不要在任何子模块里再 `import spring-boot-dependencies`（父工程已 import，重复会导致托管版本失效）。

---

## 6. 模块职责（修改前先确认归属）

| 模块 | 端口 | 职责 | 拥有的数据表 |
|---|---|---|---|
| `consult` | 8080 | AI 智能问诊、挂号工具（Function Calling）、会话记忆（MongoDB） | `appointment`（⚠️ 计划废弃，预约应归 medical-resource） |
| `health-management` | 8081 | 用户、健康记录、健康报告、医院 | `users` / `health_records` / `health_reports` / `hospitals` |
| `pathology` | 8082 | 病理知识问答、切片图像分析 | `users` / `chat_message` / `knowledge_article` / `image_entity` / `image_analysis` |
| `medical-resource` | 8083 | 医院、科室、药店、报销规则、**预约挂号** | `users` / `appointments` / `hospitals` / `departments` / `pharmacies` / `reimbursement_rules` |

⚠️ **已知的模型重复**（改造时要消除，改动前请先读 `docs/02`）：

- `users` 表被 **3 个模块各自实现**（字段名都不同）
- `hospitals` 同时存在于 `health-management` 与 `medical-resource`
- `appointments` 同时存在于 `consult` 与 `medical-resource`

---

## 7. 已修复的致命 Bug（**不要改回去**）

| # | 文件 | 原问题 | 修复 |
|---|---|---|---|
| 1 | `health-management/.../util/Result.java` | 5 个静态工厂里 `setCode/setMessage/setData` **全被注释掉**，导致 23 个接口恒返回 `null` | 恢复赋值 |
| 2 | `health-management/.../security/JwtAuthenticationFilter.java` | `getAuthorities()` 对可能为 `null` 的 `claims.get("authorities")` 直接 `.stream()` → **所有受保护接口 500** | 增加空值兜底，返回 `ROLE_USER` |
| 3 | `health-management/.../controller/AuthController.java` | 硬编码 `JWT_SECRET` 常量 | 改为注入 `SecretKey`（来自 `JwtConfig#secretKey()`） |

另外：`consult/.../assistant/SeparateChatAssistant.java` **已被整体注释停用**——它依赖的 `chatMemoryProvider` Bean 被注释、且 `my-prompt-template*.txt` 资源文件缺失，保留会导致启动失败。补齐依赖前不要启用。

---

## 8. 已知不一致（改造目标，不要沿用）

### 响应体有 3 套

| 模块 | 形式 |
|---|---|
| `health-management` | `Result<T>`，成功码 **200** |
| `medical-resource` | `ApiResponse<T>`，成功码 **0** |
| `pathology` | 裸 `ResponseEntity` + `RuntimeException` |
| `consult` | 裸 `Flux<String>` / `String` |

**目标：统一为 `{ code, message, data }`，成功码 `0`**（前端 `utils/request.js` 已按 `code === 0` 判定）。

### 前端已知问题

| 文件 | 问题 |
|---|---|
| `src/views/MedicalResources.vue` | 调用 `request.axios.post(...)`，但 `utils/request.js` 默认导出的是 axios 实例本身（无 `.axios` 属性）→ **运行时必崩**；且未 `await` 却访问 `res.data` |
| `src/views/Login.vue` | **假登录**：只写 `localStorage`，不发任何请求 |
| `src/router/index.js` | 路由 `name` 重复（`MedicalReports` ×2、`HelpCenter` ×2） |
| `vite.config.js` | **没有 `server.proxy`**，导致 `PathologyService.vue` 的相对路径 `/api/*` 404 |
| `components/emergency/EmergencyDetailModal.vue` 等 3 个组件 | `<style>` 里的 `@apply` 无 Tailwind 插件处理 → **按钮/卡片样式静默失效** |

---

## 9. 禁止事项

| 禁止 | 说明 |
|---|---|
| ❌ 用 JDK 25 构建/运行 | 见铁律 1 |
| ❌ 硬编码口令/密钥 | 见铁律 2 |
| ❌ 在子模块 pom 里写依赖 `<version>` | 见 §5.4 |
| ❌ 重新引入 `com.atguigu.*` 包名 | 已全量迁移为 `com.ganpo.*`，混用会导致编译失败 |
| ❌ 恢复 `SeparateChatAssistant` 的启用状态 | 见 §7 |
| ❌ 提交 `target/`、`node_modules/`、`dist/`、`.idea/` | 已加入 `.gitignore` |
| ❌ 大范围重构前不读 `docs/00` 与 `docs/02` | 本项目有大量「看起来实现了其实是空壳」的代码 |

---

## 10. 文档索引

| 文档 | 内容 | 什么时候读 |
|---|---|---|
| `docs/00-项目现状与差距分析.md` | 资产盘点、已完成/半成品/未实现分级、阻塞项 | **动手前必读** |
| `docs/01-技术选型与落点说明.md` | 目标架构、版本矩阵、每项技术的业务落点 | 做架构/选型相关工作时 |
| `docs/02-项目说明书.md` | 分阶段计划、模块详解、环境搭建、验收标准 | **接手任务时必读** |
| `docs/03-团队分工与协作规范.md` | 前后端分工、分支策略、提交流程、接口约定 | 开始写代码前必读 |
| `<模块>/AGENTS.md` | 该模块的局部上下文与注意事项 | 进入某个模块前 |

---

## 11. 协作方式

- 团队 4–5 人，**前端组与后端组各约一半**，全员使用 AI 编码助手
- 详细分工与 Git 流程见 `docs/03-团队分工与协作规范.md`
- **改动前先说明计划**；不确定的地方先查文档，不要臆造
- 回滚点：Git tag **`baseline-before-rescue`** 保存了本轮梳理之前的原始状态
