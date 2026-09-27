# AGENTS.md — health-management（健康管理服务）

> **进入本模块前，请先阅读根目录 `AGENTS.md`（尤其 §0 三条铁律）。**
> 负责人：**后端 B（业务服务）**

---

## 1. 模块职责

用户体系、健康记录、健康报告、医院信息。

| 项 | 值 |
|---|---|
| 端口 | **8081**，**context-path `/health`**（所有接口均需前置 `/health`） |
| 技术栈 | Spring Boot 3.2.4 + Spring Security + MyBatis-Plus 3.5.7 |
| 数据库 | MySQL `health_management` |
| 接口数 | 31 个（4 个模块中最多的） |

> ⚠️ **context-path 陷阱**：`AuthController` 映射 `/api/auth`，但实际访问路径是 **`/health/api/auth/login`**。

---

## 2. 包结构

```
com.ganpo.healthmanagement
├── HealthManagementApplication.java
├── config/
│   ├── JwtConfig.java               ★ @ConfigurationProperties("jwt")，提供 SecretKey Bean
│   ├── SecurityConfig.java
│   ├── WebConfig.java
│   ├── MyBatisPlusConfig.java
│   └── MetaObjectHandlerConfig.java  自动填充 createdAt/updatedAt
├── controller/
│   ├── AuthController.java          ★ 登录/注册/当前用户（含 JWT 签发）
│   ├── UserController.java          用户 CRUD + 统计
│   ├── HealthRecordController.java  ★ 健康记录（日历/按日查/分页/增删）
│   ├── HealthReportController.java  ★ 健康报告（含 generate 生成）
│   └── HospitalController.java      医院
├── security/
│   ├── JwtAuthenticationFilter.java ★ 解析 token 写入 SecurityContext
│   └── JwtAuthenticationEntryPoint.java
├── service/  IHealthRecordService / IHealthReportService / IHospitalService / IUserService / JwtService ...
│   └── impl/
│       ├── HealthReportServiceImpl.java   ⚠️ 1079 行，含 16 处 return null + 36 处空集合
│       ├── AnalysisServiceImpl.java       ⚠️ 152 行，8 个方法返回空 Map、评分写死 85
│       ├── JwtServiceImpl.java            ★ 228 行，4 个模块中最完整的 JWT 实现
│       └── ...
├── entity/   User / HealthRecord / HealthReport / Hospital
├── mapper/   4 个 Mapper（XML：HealthRecordMapper.xml、HealthReportMapper.xml）
├── dto/      PasswordUpdateRequest / UserStats
└── util/Result.java                统一响应体（成功码 200）
```

---

## 3. 对外接口（共 31 个，路径均需前置 `/health`）

### 认证 `/health/api/auth`
| 方法 | 路径 | 功能 |
|---|---|---|
| POST | `/health/api/auth/login` | 登录，签发 JWT |
| POST | `/health/api/auth/register` | 注册（BCrypt 加密） |
| GET | `/health/api/auth/me` | 由 token 解析当前用户 |

### 用户 `/health/api/users`
| 方法 | 路径 | 功能 |
|---|---|---|
| GET | `/health/api/users` | 用户列表 |
| GET | `/health/api/users/{id}` | 用户详情（清空密码字段） |
| PUT | `/health/api/users/{id}` | 更新用户 |
| PUT | `/health/api/users/{id}/password` | 修改密码（校验旧密码） |
| DELETE | `/health/api/users/{id}` | 删除用户 |
| GET | `/health/api/users/{id}/stats` | 用户统计 ⚠️ **恒返回 null** |
| GET | `/health/api/users/search?keyword=` | 用户搜索 |

### 健康记录 `/health/api/health-records`
| 方法 | 路径 | 功能 |
|---|---|---|
| GET | `/health/api/health-records/calendar?year=&month=&userId=` | 日历热力数据 |
| GET | `/health/api/health-records/{date}?userId=` | 按日期查询 |
| POST | `/health/api/health-records` | 新增/更新 |
| GET | `/health/api/health-records/page?userId=&current=&size=` | 分页 |
| DELETE | `/health/api/health-records/{date}?userId=` | 删除 |

### 健康报告 `/health/api/health-reports`
| 方法 | 路径 | 功能 |
|---|---|---|
| GET | `/health/api/health-reports?userId=` | 报告列表 |
| GET | `/health/api/health-reports/{id}` | 报告详情 |
| POST | `/health/api/health-reports` | 创建 |
| PUT | `/health/api/health-reports/{id}` | 更新 |
| DELETE | `/health/api/health-reports/{id}` | 删除 |
| POST | `/health/api/health-reports/generate?userId=&startDate=&endDate=` | 生成报告 |

### 医院 `/health/api/hospitals`
| 方法 | 路径 | 功能 |
|---|---|---|
| GET | `/health/api/hospitals` | 列表（**Security 放行，免登录**） |
| GET | `/health/api/hospitals/{id}` | 详情 |
| GET | `/health/api/hospitals/city/{city}` | 按城市 |
| GET | `/health/api/hospitals/search?keyword=` | 搜索 |
| GET | `/health/api/hospitals/popular?limit=` | 热门 |
| POST/PUT | `/health/api/hospitals`、`/{id}` | 新增/更新 |

---

## 4. 数据库

库 `health_management`，4 张表：

| 表 | 关键字段 |
|---|---|
| `users` | username、password_hash、**birthDate**（⚠️ 见 §6）、gender、email、phone、realName、created_at、updated_at |
| `health_records` | user_id、record_date、sleep/diet/mood/exercise_score、sleep_type、exercise_type、exercise_duration、medication_status、notes、血压/心率/体重 |
| `health_reports` | user_id、title、report_type、start/end_date、overall_score、health_grade、各维度 score、weight_change、report_status、summary、detail_content、doctor_advice、deleted（逻辑删除） |
| `hospitals` | name、address、distance、emergency_department、business_hours、phone、latitude、longitude、description |

> ⚠️ **本模块没有建表脚本**，需从实体反推生成。这是 Phase 2 的任务。
> ⚠️ 原 `application.yml` 配了 `spring.jpa.hibernate.ddl-auto: update`，但**本模块没有 JPA 依赖**，该配置不会建表（已移除）。

---

## 5. ✅ 已修复的致命 Bug（**不要改回去**）

| # | 文件 | 原问题 | 修复 |
|---|---|---|---|
| 1 | `util/Result.java` | 5 个静态工厂里 `setCode/setMessage/setData` **全被注释掉** → 23 个接口恒返回 `null`，前端无法解析 | 恢复赋值 |
| 2 | `security/JwtAuthenticationFilter.java` | `getAuthorities()` 对可能为 `null` 的 `claims.get("authorities")` 直接 `.stream()` → **所有受保护接口 500** | 空值兜底，返回 `ROLE_USER` |
| 3 | `controller/AuthController.java` | 硬编码 `JWT_SECRET = "your-secret-key-must-be-at-least-256-bits-long"` | 改为注入 `SecretKey`（来自 `JwtConfig#secretKey()`） |

---

## 6. ⚠️ 其他已知问题

| # | 问题 | 说明 |
|---|---|---|
| 1 | `User` 实体的 `@TableField("birthDate")` | 字段名大小写异常，与 `map-underscore-to-camel-case: true` 冲突。**建表时列名必须与此完全一致** |
| 2 | `AnalysisServiceImpl` 是**空壳** | 8 个方法返回空 Map，综合评分写死 85 → **必须重写**（Phase 9） |
| 3 | `HealthReportServiceImpl` 1079 行 | 含 16 处 `return null` + 36 处空集合，大量未完成逻辑 |
| 4 | `GET /users/{id}/stats` | 实现恒返回 null |
| 5 | `jwt.secret` 必须是**合法 Base64** | `JwtConfig` 会 `Base64.getDecoder().decode(secret)`；非法 Base64 会导致启动失败。已改为 `${JWT_SECRET:默认Base64}` |
| 6 | 响应码语义为 **200** | 与其他模块的 `0` 不一致，Phase 3–4 将统一为 `0`。**新写接口请按 `0` 语义** |
| 7 | `hospitals` 表与 `medical-resource` **重复** | 计划由 `medical-resource` 独占，本模块删除 `Hospital` 实体与 `HospitalController`，需要时走 Feign |
| 8 | `users` 表与其他 2 个模块**重复** | 计划收归未来的 `ganpo-auth`。本模块的 `JwtServiceImpl` 是最完整的实现，将作为 `ganpo-auth` 的基础 |
| 9 | 原 `application.yml` 有大量无效配置 | `spring.jpa.*`、`spring.data.redis.*`、`mail.*`、`swagger.*` 对应依赖均不存在，已移除 |

---

## 7. 本地运行

```powershell
# 依赖：MySQL(3307) 且 health_management 库已建
$env:DB_PORT = "3307"
D:\JDK21\bin\java -jar health-management\target\health-management-0.0.1-SNAPSHOT.jar

# 验证
curl "http://localhost:8081/health/api/hospitals"
```
