# 赣鄱医枢 · ganpo-medical

> 面向南昌本地的**智能医疗伴诊与判诊平台** —— 参赛作品（全国计算机设计大赛 · 软件应用与开发赛道）

AI 医疗咨询 · 症状分析 · 科室推荐 · 号源查询 · 预约挂号 · 健康记录 · 病理知识库 · 紧急急诊引导

---

## 📌 先读这个

**无论你是人还是 AI 助手，请按以下顺序阅读：**

| 文档 | 内容 | 何时读 |
|---|---|---|
| **`AGENTS.md`** | **三条铁律、环境、代码约定、禁止事项** | **动手前必读（AI 助手会自动加载）** |
| `docs/00-项目现状与差距分析.md` | 什么已完成 / 什么是空壳 / 阻塞项 | 了解项目真实状态 |
| `docs/01-技术选型与落点说明.md` | 目标架构、版本矩阵、技术落点 | 架构与选型相关 |
| `docs/02-项目说明书.md` | 分阶段计划、模块详解、环境搭建 | **接手任务时** |
| `docs/03-团队分工与协作规范.md` | 分工、分支策略、接口约定 | **开始写代码前** |
| `<模块>/AGENTS.md` | 该模块的局部注意事项 | 进入模块前 |

---

## ⚠️ 两条铁律

1. **构建/运行 Java 必须用 JDK 21** —— `PATH` 首位是 JDK 25，运行 jar 时要显式写 `D:\JDK21\bin\java`。
2. **不要把口令、密钥写进任何受版本控制的文件** —— 统一用 `${ENV_VAR:默认值}`。

---

## 🏗️ 架构总览

### 现状（4 个独立 Spring Boot 服务 + 1 个 Vue 3 SPA）

| 模块 | 端口 | 职责 | 数据库 |
|---|---|---|---|
| `consult` | 8080 | AI 智能问诊（LangChain4j + DashScope + Pinecone + MongoDB） | `guiguxiaozhi` |
| `health-management` | 8081 | 用户、健康记录、健康报告、医院 | `health_management` |
| `pathology` | 8082 | 病理知识问答、切片图像分析 | `pathology` |
| `medical-resource` | 8083 | 医院、科室、药店、报销、**预约挂号** | `medical_resource` |
| `src/`（前端） | 5173 | Vue 3 SPA，49 个组件 / 22,975 行 | — |

### 目标（微服务化）

```
浏览器 → Nginx → ganpo-gateway(:8080) → 各业务服务
                    ↑ Nacos 注册配置中心
   ganpo-common / ganpo-common-web / ganpo-api ← 公共层
```

技术栈：Spring Boot **3.2.4** + Spring Cloud **2023.0.1** + Spring Cloud Alibaba **2023.0.1.0** + Nacos **2.3.2**
（详见 `docs/01-技术选型与落点说明.md`）

---

## ✅ 当前进度

**Phase 0 + Phase 1 已完成：**

- ✅ 清除全部 **46 处 Git 冲突标记**（26 个文件）—— 这是后端此前无法编译的首要原因
- ✅ 修复 **3 个致命 Bug**（响应体恒为空 / 鉴权 NPE / 硬编码 JWT 密钥）
- ✅ 重构 pom 体系：统一版本管理、消除重复依赖与未定义版本号
- ✅ **后端首次编译成功**：4 模块 145 个 `.class`，产出 4 个**可执行 fat jar**
- ✅ 清理 171 MB 冗余（`medical-app/` 副本含被误提交的 `node_modules`，13,728 个文件）
- ✅ 配置外部化、`.gitignore` 重写、IDE 配置解除跟踪

**下一步（Phase 2）**：搭建 3307 数据库实例 + 补齐建表脚本 + Redis 落地。详见 `docs/02` §5。

---

## 🚀 快速开始

### 环境要求

| 依赖 | 版本 |
|---|---|
| JDK | **21**（`JAVA_HOME=D:\JDK21`） |
| Maven | 3.9+ |
| Node.js | 18+ |
| MySQL | 9.x（**项目专用实例，端口 3307**） |

### 构建后端

```powershell
cd D:\ideaAllCode\ganpo-medical\ganpo-medical

# 本机需显式指定本地仓库（原因见 AGENTS.md §4.1）
mvn -B "-Dmaven.repo.local=$env:USERPROFILE\.m2\repository" clean package -DskipTests
```

产物：

```
consult/target/consult-0.0.1-SNAPSHOT.jar                     (253 MB)
health-management/target/health-management-0.0.1-SNAPSHOT.jar  (38 MB)
pathology/target/pathology-0.0.1-SNAPSHOT.jar                  (37 MB)
medical-resource/target/medical-resource-0.0.1-SNAPSHOT.jar    (30 MB)
```

### 运行服务

```powershell
# 必须用 JDK21 的 java（PATH 首位是 JDK25）
D:\JDK21\bin\java -jar health-management\target\health-management-0.0.1-SNAPSHOT.jar

# health-management 的接口都带 context-path
curl "http://localhost:8081/health/api/hospitals"
```

> ⛔ **目前服务还起不来** —— 4 个数据库尚未创建（Phase 2 任务）。建库步骤见 `docs/02` §4.1。

### 运行前端

```powershell
npm run dev      # http://localhost:5173
npm run build    # 生产构建
```

---

## 📁 目录结构

```
ganpo-medical/
├── AGENTS.md              ← AI 助手入口（铁律与约定）
├── README.md              ← 本文件
├── pom.xml                ← 父工程（依赖版本的唯一来源）
├── docs/                  ← 项目文档
├── scripts/               ← 启动/构建脚本
├── src/                   ← 前端 Vue 3 SPA
├── consult/               ← AI 智能问诊服务
├── health-management/     ← 健康管理服务
├── pathology/             ← 病理服务
└── medical-resource/      ← 医疗资源与预约服务
```

---

## 🧭 已知技术债（改造目标）

| 类别 | 问题 |
|---|---|
| 架构 | 无网关、无注册中心；4 个模块各自为政 |
| 鉴权 | **3 套互不兼容的 JWT**；`consult` 无鉴权；前端是**假登录** |
| 数据模型 | `users` 表重复 3 次、`hospitals` 重复 2 次、`appointments` 重复 2 次 |
| 响应体 | 3 种格式（`Result` 200 / `ApiResponse` 0 / 裸 `ResponseEntity`） |
| AI 真实性 | `pathology` 图像分析是**伪实现**（`sleep` + 写死诊断）；问答是 `LIKE` 匹配 |
| 数据分析 | `AnalysisServiceImpl` 8 个方法返回空 Map、评分写死 85 |
| 前后端联通 | 健康记录/报告/紧急救助仍靠 `localStorage` 与静态数据 |
| 工程化 | 无 ESLint、无测试、无 CI、无 Docker |

完整清单见 `docs/00-项目现状与差距分析.md`。

---

## 🔄 回滚点

Git tag **`baseline-before-rescue`** 保存了本轮梳理之前的原始代码状态：

```powershell
git diff baseline-before-rescue -- <文件路径>
git show baseline-before-rescue:<文件路径>
```

---

## 📄 许可证

MIT License
