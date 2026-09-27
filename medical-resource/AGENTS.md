# AGENTS.md — medical-resource（医疗资源与预约服务）

> **进入本模块前，请先阅读根目录 `AGENTS.md`（尤其 §0 三条铁律）。**
> 负责人：**后端 B（业务服务）**

---

## 1. 模块职责

医院、科室、药店、报销规则查询，以及 **预约挂号**（本模块是预约的**权威服务**）。

| 项 | 值 |
|---|---|
| 端口 | **8083**（无 context-path） |
| 技术栈 | Spring Boot 3.2.4 + Spring Security + MyBatis-Plus 3.5.7 |
| 数据库 | MySQL `medical_resource` |
| 接口数 | 10 个 |

> ⭐ **本模块的领域模型最完整**：`Hospital` 含 `level`/`rating`/`lat`/`lng`/`core_departments`；`Appointment` 含 `ticketNumber`/`timeslot`/`status`。
> **`consult` 的预约功能计划合并到本模块**（届时 `consult` 通过 Feign 调用这里）。

---

## 2. 包结构

```
com.ganpo.medicalresource
├── MedicalResourceApplication.java
├── config/
│   ├── JwtFilter.java                    ★ 401 拦截 + excludedUrls 白名单 + JSON 错误响应
│   ├── CorsConfig.java                   ⚠️ 过宽，见 §5
│   ├── WebConfig.java
│   ├── MybatisPlusConfig.java
│   ├── FactoryBeanObjectTypeDetector.java         临时兼容补丁
│   └── FactoryBeanObjectTypeCompatPostProcessor.java  临时兼容补丁（与 pathology 重复）
├── controller/
│   ├── AuthController.java              /api/auth/register|login
│   ├── HospitalController.java          /api/hospitals
│   ├── DepartmentController.java        /api/departments
│   ├── PharmacyController.java          /api/pharmacies
│   ├── ReimbursementController.java     /api/reimbursement/rules|calculate
│   └── AppointmentController.java       ★ /api/appointments
├── service/ + impl/                      AppointmentService / DepartmentService /
│                                         HospitalService / PharmacyService /
│                                         ReimbursementService / UserService
├── entity/  User / Hospital / Department / Pharmacy / ReimbursementRule / Appointment
├── mapper/  6 个 Mapper
├── dto/ApiResponse.java                 统一响应体（成功码 0）
├── exception/GlobalExceptionHandler.java
└── util/JwtUtil.java                    HS256，读 app.jwt.secret
```

---

## 3. 对外接口（10 个）

| 方法 | 路径 | 功能 | 鉴权 |
|---|---|---|---|
| POST | `/api/auth/register` | 注册（BCrypt） | 免过滤 |
| POST | `/api/auth/login` | 登录签发 JWT | 免过滤 |
| POST | `/api/appointments` | 创建预约（生成 `ticketNumber`） | 需 token（取 `currentUser` attribute） |
| GET | `/api/appointments/{id}` | 预约详情 | ⚠️ **无归属校验**（水平越权） |
| GET | `/api/hospitals?page=&size=&departmentId=&sort=&lat=&lng=&q=` | 医院分页/筛选/排序 | 需 token |
| GET | `/api/hospitals/{id}` | 医院详情 | 需 token |
| GET | `/api/departments` | 科室列表 | 需 token |
| GET | `/api/pharmacies?page=&size=&lat=&lng=&radius=&q=` | 附近药店 | 需 token |
| GET | `/api/reimbursement/rules?crowd=` | 报销规则 | 需 token |
| POST | `/api/reimbursement/calculate` | 报销试算 | 需 token |

> ⚠️ **前端调用方式有 Bug**：`MedicalResources.vue` 写的是 `request.axios.post(...)`，但 `utils/request.js` 默认导出的是 axios 实例本身（**没有 `.axios` 属性**）→ `undefined is not a function`。且未 `await` 却访问 `res.data`。
> 前端期望的响应是 `{ code: 0, data: { details: [...], reimbursableAmount } }`。

---

## 4. 数据库

库 `medical_resource`，6 张表，脚本：`src/main/resources/sql/schema-and-sample-data.sql`（含南昌医院/药店种子数据）

| 表 | 关键字段 |
|---|---|
| `users` | username、password(bcrypt)、name、phone、created_at |
| `appointments` | user_id、hospital_id、department_id、doctor_id、date、timeslot、status、ticket_number |
| `hospitals` | name、level、rating、address、lat、lng、core_departments(JSON)、has_open_slots |
| `departments` | id(VARCHAR)、name、parent_id、alias(JSON) |
| `pharmacies` | name、address、lat、lng、is_designated、opening_hours |
| `reimbursement_rules` | crowd_type、outpatient/inpatient/exam/drug_ratio、max_per_year、生效区间 |

> 🔴 **脚本库名不一致（必须修）**：脚本里是 `CREATE DATABASE medical_db` / `USE medical_db`，而 `application.yml` 连的是 `medical_resource` → **直接执行脚本后应用仍连不上**。
> Phase 2 任务：把脚本库名改成 `medical_resource`（或反之），并保证脚本可重复执行。

---

## 5. ⚠️ 已知问题（改动前必读）

| # | 问题 | 说明 |
|---|---|---|
| 1 | **建表脚本库名不一致** | `medical_db` vs `medical_resource`，见 §4 |
| 2 | **`GET /api/appointments/{id}` 无归属校验** | 任何人可查任意预约（水平越权）。需加 `userId` 比对 |
| 3 | **`CorsConfig` 过宽** | `*` + `allowCredentials(true)`，浏览器会拒绝。计划由网关统一管理 |
| 4 | **`JwtFilter` 与 pathology 的语义不一致** | 本模块拦截（无 Bearer 返回 401），pathology 只解析不拦截。需统一 |
| 5 | **JWT 密钥与其他模块相同** | 曾与 `pathology` 使用同一字面量密钥 → 跨模块信任边界失控。已改为 `${JWT_SECRET:默认值}` |
| 6 | `FactoryBeanObjectType*` 两个兼容补丁 | 与 `pathology` 的 `FixFactoryBeanObjectTypeConfig` 目的重复，**只应保留一份** |
| 7 | `SecurityAutoConfiguration` 被 exclude | `application.yml` 中 `spring.autoconfigure.exclude` 排除了 Spring Security 自动配置，鉴权完全依赖自定义 `JwtFilter`。**改动鉴权前务必理解这一点** |
| 8 | `hospitals` 表与 `health-management` **重复** | 计划由本模块独占，`health-management` 删除其 `Hospital` 实体与 Controller |
| 9 | `users` 表与其他 2 个模块**重复** | 计划收归 `ganpo-auth` |
| 10 | `AppointmentController` 类注释里有一处示例 JSON | 描述的是请求体格式，`departmentId` 是字符串（如 `"cardiology"`） |

---

## 6. ✅ 本模块已修复（Phase 1）

**13 个 Java 文件、28 处冲突标记全部清理完毕**，取向均为 HEAD 侧。其中几处有实际语义差异：

| 文件 | 说明 |
|---|---|
| `config/JwtFilter.java` | 取 HEAD = 保留完整的 **401 拦截 + 白名单 + JSON 错误响应**；丢弃了另一侧的「只解析不拦截」弱化版与未使用的 `UserService` 注入 |
| `entity/User.java` | 取 HEAD = 保留 `@JsonFormat` 注解（`createdAt` 的时区/格式序列化），另一侧缺失 |
| `entity/Appointment.java` | 取 HEAD = 保留 `createdAt` **与** `updatedAt` 两个 `@JsonFormat` 注解 |
| `controller/AuthController.java` | 取 HEAD，并**删除了调试输出** `System.out.println(req.getId())` |

> ⚠️ 这些取向不要随意改回。

---

## 7. 本地运行

```powershell
# 1) 建库（⚠️ 先用编辑器把脚本里的 medical_db 改成 medical_resource）
mysql -h 127.0.0.1 -P 3307 -u root -p < medical-resource\src\main\resources\sql\schema-and-sample-data.sql

# 2) 启动
D:\JDK21\bin\java -jar medical-resource\target\medical-resource-0.0.1-SNAPSHOT.jar

# 3) 验证（需先登录拿 token）
curl -X POST "http://localhost:8083/api/auth/login" -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"...\"}"
curl "http://localhost:8083/api/hospitals?page=1&size=10" -H "Authorization: Bearer <token>"
```
