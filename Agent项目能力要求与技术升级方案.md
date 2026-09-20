# AI 应用开发岗（2027 秋招）Agent 项目能力要求与技术升级方案

> **文档目的**：基于 5 份大厂 AI 应用开发 / Agent 工程师秋招 JD 的要求提取，定义一个"2026 年标准"的 Agent 项目应具备的能力与技术栈，并对照本项目（智能投研平台）现状给出差距分析与可落地的升级方案。
>
> **JD 来源**：
> 1. 字节跳动 —— AI 应用开发工程师（客户端开发）
> 2. 小红书 ——【2027 校招】AI Agent 工程师 - 社区工程
> 3. 小红书 ——【2027 校招】点点 Agent 工程师（社区工程方向）
> 4. 小红书 ——【2027 校招】AI 应用开发工程师（大模型 AI 质效研发方向）
> 5. 康冠科技 KTC —— Agent 工程师（J11671）
>
> **对标项目**：智能投研平台（FastAPI 网关 + LangGraph 五阶段工作流 + Streamlit 前端 + DashScope 通义千问）

---

## 一、JD 关键能力提取与频次分析

### 1.1 高频能力关键词（按 5 份 JD 出现频次排序）

| 能力关键词 | 频次 | 代表性 JD 原文 |
|---|---|---|
| **Agent Harness / Runtime 工程** | 4/5 | "持续跟进大模型、Agent、Harness 工程等前沿技术"；"参与构建面向下一代 Agent 的 Runtime 与 Harness"；"具备 Agent Harness 架构或底层框架的开发经验" |
| **工具使用 / Tool Use / Tool Calling / Function Calling** | 4/5 | "理解 Prompt 工程、RAG、Embedding、向量数据库、Function Calling、Skill、Agent 等核心概念"；"Tool 选择与执行" |
| **记忆与上下文管理（Memory / Context Management）** | 4/5 | "长期记忆"；"多轮上下文管理"；"掌握上下文管理、记忆窗口截断、Token 优化相关原理" |
| **推理与规划（Reasoning & Planning）** | 3/5 | "自主规划"；"任务理解与拆解、规划与推理"；"长程推理、多步骤任务规划" |
| **MCP 协议 / Skills 体系** | 2/5 | "围绕业务场景构建业务 Agent、MCP 工具与 Skills 体系"；"参与 Skill、MCP 模块开发与迭代" |
| **RAG / Embedding / 向量数据库 / 搜索与检索** | 3/5 | "理解 RAG、Embedding、向量数据库"；"Agentic Search"；"持续优化工具检索" |
| **状态管理及异常恢复 / 可靠性** | 3/5 | "状态管理及异常恢复"；"高能力、高可靠、真正解决复杂问题的 AI Agent"；"失败恢复效果" |
| **评测驱动（Eval）** | 2/5 | "建设评测驱动的研发体系，包括端到端任务评测、模型与工程专项评测、Tool 评测及真实 Tool 实验环境" |
| **执行轨迹 / 可观测性（Trace）** | 2/5 | "开展推理轨迹构建"；"通过任务结果和执行轨迹定位模型、工程与 Tool 的能力边界" |
| **多智能体协作（Multi-Agent）** | 2/5 | "个性化及多智能体协作等关键方向"；"探索 Multi-Agent 协作、AI 自我进化等前沿方向" |
| **反思 / 自进化（Reflector）** | 2/5 | "设计实现 Agent 记忆系统、Reflector 自进化机制"；"AI 自我进化" |
| **AI Coding / AI 辅助研发** | 2/5 | "基于 AI Coding 工具，建设从需求产出到功能开发、验证、上线的自动化研发流程"；"熟练使用 Claude Code、Codex、Cursor 等 AI 编程工具" |
| **生成式 UI（Generative UI）** | 1/5 | "探索生成式 UI 的产品与技术形态，动态生成和组织答案、卡片、对比组件及交互流程" |
| **LLM 工程：部署 / 微调 / 后训练** | 2/5 | "了解 Qwen/Gemma/DeepSeek 等 LLM 的部署与微调"；"探索 Agentic Post-training、Agentic RL" |
| **容器化部署 / 云平台** | 1/5 | "熟悉云服务平台和容器化部署工具" |
| **Token 优化 / 成本 / 时延负责** | 2/5 | "对最终用户体验、效果、稳定性、时延和成本负责"；"Token 优化相关原理" |

### 1.2 结论：大厂对 Agent 项目的"能力画像"

5 份 JD 共同指向一个信号：**2026 年的 Agent 项目，已经从"会调 LLM API + 串一个工作流"升级为"一个完整的智能体系统工程"**。面试官期望看到的项目必须覆盖以下三层：

1. **智能体核心层（Agent Core）**：规划、记忆、工具、反思、状态管理——即"Agent 五大件"；
2. **工程运行时层（Harness / Runtime）**：可观测、可评测、可恢复、可控制、成本可度量；
3. **生态接入层**：MCP 工具协议、Skills 技能体系、多模型路由、生成式 UI、容器化交付。

只具备"Prompt + 链式调用 + 简单重试"的项目，已经无法通过简历关。

---

## 二、现代 Agent 项目应具备的能力与技术栈（目标能力清单）

### 2.1 Agent 核心运行时（Harness）能力

| 能力项 | 说明 | 对应技术 |
|---|---|---|
| 任务理解与拆解 | 将用户目标分解为可执行的子任务 DAG / 列表 | Plan-and-Execute、Task Decomposition |
| 规划与推理 | 自主决定下一步行动，而非固定流水线 | ReAct、ToT（Tree of Thoughts）、Reflection |
| Tool 选择与执行 | 模型基于工具描述自主选择工具、生成参数、解析结果 | Function Calling / Tool Calling（JSON Schema 描述） |
| 状态管理 | 全局任务状态机，支持暂停、恢复、回滚 | LangGraph StateGraph / 自研状态机 + Checkpointer |
| 异常恢复 | 工具失败、模型超时、输出非法时的分类处理与回退 | 重试（指数退避）、Fallback 链、降级策略、检查点恢复 |
| 护栏与可控性 | 对模型输出与工具调用做权限/格式/安全约束 | Pydantic 校验、Guardrails、工具白名单、沙箱执行 |

### 2.2 工具与技能生态（MCP + Skills）

- **Function Calling / Tool Calling**：所有外部能力（数据、搜索、计算、导出）以"工具"形式暴露，带标准化 JSON Schema 描述，由模型动态选择，而非代码写死调用顺序。
- **MCP（Model Context Protocol）**：将工具封装为 MCP Server，Agent 作为 MCP Client 动态发现、加载、调用工具；实现"工具与 Agent 解耦、可插拔、可复用"。这是 2025-2026 年工具生态的事实标准。
- **Skills 体系**：把"方法论 / 流程性知识"（如：价值投资分析框架、事件驱动分析框架）封装为可加载的 Skill 文档/配置，按需注入上下文，与 Tool（动作能力）形成互补。
- **多工具协同与失败恢复**：一次任务中可串行/并行调用多个工具；对工具返回做"结果理解"（校验、摘要、截断），失败时换工具或换参数重试。

### 2.3 记忆与上下文管理（Memory / Context Management）

三层记忆架构是 JD 中反复出现的硬要求：

| 记忆层 | 内容 | 技术实现 |
|---|---|---|
| 工作记忆 | 当前任务的中间状态、工具调用结果 | State 对象 / Scratchpad |
| 短期记忆 | 多轮对话上下文 | 上下文窗口管理：滑动窗口、摘要压缩（Summarization）、重要信息置顶 |
| 长期记忆 | 历史任务结论、用户偏好、领域知识 | 向量数据库（Chroma / FAISS / Milvus）+ Embedding 检索；结构化存储（SQLite/JSON）+ 关键词检索 |

必备工程细节（KTC JD 原话）：
- **记忆窗口截断**：超长按 token 计数截断，而非按字符数；
- **Token 优化**：tiktoken 计数、结果摘要压缩、分级加载（先摘要后原文）；
- **记忆写回与淘汰**：Reflector 产出的经验写入长期记忆，按时间/重要性淘汰。

### 2.4 检索与 RAG（含 Agentic Search）

- 基础 RAG：文档/新闻切块 → Embedding → 向量库 → 相似度检索 → 注入 Prompt；
- **Agentic Search**：由 Agent 自主决定"是否检索、检索什么、换什么 query 再检索"，把搜索变成工具调用循环，而非固定前置步骤；
- 混合检索：向量检索 + 关键词检索（BM25）+ 结构化数据查询（行情/财务）融合排序；
- 检索结果要带**来源引用**，支撑"可解释、内容证据"（点点 JD 要求）。

### 2.5 反思与自进化（Reflection / Reflector）

- 在关键阶段后设置 **Reflector 节点**：对推理轨迹自评（结论与数据是否矛盾、证据是否充分），产出修正意见回灌；
- **经验沉淀**：把反思结论写入长期记忆，下次同类任务先检索历史经验（= "AI 自我进化"的工程化落地版本）；
- 与现有"重试"的区别：重试是"原样再来一遍"，反思是"带着诊断结论改策略再来一遍"。

### 2.6 多智能体协作（Multi-Agent）

- 至少实现一种协作模式：Supervisor（主 Agent 分派子 Agent）、串行流水线、或辩论/审核模式；
- 典型拆法：数据检索 Agent / 分析 Agent / 风控审核 Agent / 报告 Agent；
- 子 Agent 间通过结构化消息（Pydantic 模型）通信，而非自由文本。

### 2.7 评测驱动与可观测性（Eval & Tracing）

这是区分"学生项目"和"工程项目"的最关键一项：

- **评测集**：构建带标准答案/检查点的任务集（如 20 条投研问题 + 期望结论要点）；
- **端到端评测**：任务完成率、结论-数据一致性、引用准确率、格式合规率；可用 LLM-as-Judge 打分 + 规则校验结合；
- **Tool 评测**：每个工具的成功率、平均时延、异常类型分布；
- **执行轨迹（Trace）**：完整记录每一步的 input/output/耗时/token 数，支持事后回放与瓶颈定位；LangSmith / LangFuse / 自研 JSON Trace 均可；
- **指标看板**：成功率、P95 时延、Token 成本趋势，驱动版本决策。

### 2.8 LLM 工程能力

- 理解 Transformer / Attention 基本原理，能说清上下文窗口、token、temperature 等对 Agent 行为的影响；
- **多模型路由与降级**：统一 OpenAI 兼容接口接入多家模型（Qwen/DeepSeek/Gemma…）；按任务分级用模（难任务用大模型、摘要/分类用小模型）控制成本；
- 了解**部署与微调**链路：vLLM/Ollama 本地部署、LoRA 微调、后训练（SFT/RLHF/Agentic RL）概念——应用岗要求"了解"即可，但要能聊；
- 结构化输出工程：Prompt 约束 + JSON Schema / Pydantic 校验 + 解析失败重试。

### 2.9 生成式 UI 与交互

- 前端根据 Agent 执行状态**动态生成**界面组件（卡片、对比表、进度轨迹），而非固定页面；
- 流式输出（SSE / WebSocket）展示思考过程与工具调用过程，做到"可控、可解释"。

### 2.10 工程化落地：架构、性能、安全、部署

| 维度 | 要求 |
|---|---|
| 架构 | 前后端分离、服务分层（网关/Agent/数据/报告）、接口契约（Pydantic/OpenAPI）、异步任务队列 |
| 性能 | 工具结果缓存、并发工具调用（asyncio）、流式响应、Token 与时延成本可度量 |
| 安全 | 密钥环境变量管理、工具白名单与只读沙箱、输入校验、限流、敏感信息脱敏 |
| 部署 | Docker 容器化 + Compose 编排、健康检查、日志收集、CI（lint + test 自动化） |
| 研发方式 | 日常使用 AI Coding 工具（Cursor / Claude Code / Codex），并能描述"AI 辅助研发流程" |

---

## 三、本项目现状与差距分析

**现状基线**（智能投研平台）：LangGraph 五阶段固定工作流（感知→建模→推理→决策→报告）；FastAPI 网关 + 异步任务 + 历史管理 + PDF 导出；Streamlit 前端；DashScope 通义千问；已有指数退避重试、文件检查点、Pydantic 输出校验、真实数据一致性校验（置信度奖惩）、akshare 数据工具与缓存。

| 能力维度 | 现状 | 目标（JD 要求） | 差距 |
|---|---|---|---|
| 工作流形态 | 固定五阶段流水线 | 自主规划 + 动态工具选择 | **高** |
| 工具体系 | Python 函数硬编码调用 | Function Calling + MCP Server + Skills | **高** |
| 记忆 | 仅文件历史（analysis_history.json） | 三层记忆 + 向量检索 + 窗口截断/Token 优化 | **高** |
| 检索/RAG | 新闻直取，无 Embedding | 向量库 + Agentic Search + 引用 | **高** |
| 反思/自进化 | 无（仅有原样重试） | Reflector 节点 + 经验写回 | **中** |
| 多 Agent | 单工作流 | Supervisor 或多 Agent 协作 | **中** |
| 评测 | 零散 test_*.py 脚本 | 评测集 + 端到端/Tool 评测 + LLM-as-Judge | **高** |
| 可观测性 | logging 日志 | 全链路 Trace + 指标看板 | **中** |
| 模型工程 | 单一 DashScope | 多模型路由/降级 + 成本分级 | **中** |
| 前端 | Streamlit 固定布局 | 流式 + 生成式 UI 组件 | **中** |
| 部署/CI | start.bat + 部署方案文档（未落地） | Docker Compose 落地 + CI | **中** |
| 可靠性 | 重试 + 检查点（已有，较好） | + 异常分类、Fallback、护栏 | **低** |

---

## 四、技术升级方案（分阶段、可落地）

### P0 阶段一：工具协议化 —— Function Calling + MCP + Skills

**目标**：把"写死的流水线"升级为"模型自主选择工具"，对齐 JD 中 MCP/Skills/Tool Calling 全部关键词。

1. 将 `backend/data_fetcher/` 下四类能力（个股行情 `stock_data`、财务 `financials`、指数 `market_indices`、新闻 `news_fetcher`）封装为标准 Tool：每个工具含 `name / description / parameters(JSON Schema) / executor`。
2. 用 `mcp` Python SDK 把这些 Tool 暴露为 **MCP Server**（stdio 或 SSE 传输）；Agent 侧实现 MCP Client，启动时动态拉取工具清单。
3. 推理/感知阶段改为 **Tool Calling 循环**：模型输出工具调用 → 执行 → 结果理解（校验/截断/摘要）→ 决定继续或结束；单工具失败时换参数或换工具重试（失败恢复）。
4. 建立 **Skills 目录**（`skills/`）：如 `value_investing.md`、`event_driven.md`，描述分析方法论与推荐工具组合；任务开始时由模型选择 Skill 注入 System Prompt。

**验收**：新增一个数据工具只需"写一个函数 + 注册"，Agent 无需改代码即可使用；面试可演示"动态工具发现"。

### P0 阶段二：记忆与上下文管理

**目标**：对齐 Memory/Context Management、记忆窗口截断、Token 优化、RAG/Embedding/向量数据库。

1. 引入 Embedding（如 `text-embedding-v3` 或本地 `bge-small-zh`）+ 向量库（Chroma/FAISS），将历史分析记录、新闻语料入库。
2. 长期记忆读写协议：任务开始检索"相似标的历史分析"注入上下文；任务结束将结论 + Reflector 经验写回。
3. 上下文管理器：tiktoken 计数 → 超预算时按"系统提示 > 当前任务 > 检索证据 > 历史对话"优先级截断/摘要压缩。
4. 新闻检索升级为混合检索（向量 + 关键词），结果带来源与时间，供报告引用。

**验收**：同一标的二次分析时，Agent 能引用自己上次的结论；长任务不超窗口且 token 消耗可量化对比。

### P1 阶段三：Reflector 自进化 + 状态/异常恢复增强

1. 在"决策"与"报告"之间增加 **Reflector 节点**：检查结论-证据一致性、评分合理性，产出 `reflection`（问题清单 + 修正建议）；不达标则带修正意见回退重跑（区别于现有原样重试）。
2. 异常分类处理：可重试（网络/限流）→ 指数退避；不可重试（参数非法）→ 换参数/换工具；模型不可用 → Fallback 到备用模型。
3. 文件检查点升级为 LangGraph Checkpointer 语义：支持从中断阶段恢复、状态回放。

### P1 阶段四：评测体系与可观测性

1. 建 `evals/` 评测集：20+ 条投研任务，含期望要点（结论方向、必引数据、必含风险提示）。
2. 评测 runner：规则校验（方向一致性、字段完整、引用存在）+ LLM-as-Judge 打分，输出结构化评测报告。
3. 全链路 Trace：每次运行落盘 `traces/{task_id}.json`（每步 input/output/tool_calls/tokens/latency）；写一个 trace 查看页或 CLI 回放。
4. Tool 级指标统计：成功率/时延/异常分布，形成"能力边界"结论（例：新闻工具超时率 8% → 加缓存/降级）。

**验收**：每次改动 Prompt/工具后跑一遍 evals，用数据说明"变好了还是变差了"——这正是 JD 要的"评测驱动迭代"。

### P2 阶段五：Agentic Search 与多 Agent 协作

1. 感知阶段改为 Agentic Search：模型自主生成检索计划（查行情？查新闻？查财务？），多轮迭代直到信息充分。
2. 拆分 **风控审核 Agent**：对决策结论做独立复核（红队），与主分析 Agent 形成"分析-审核"双 Agent 结构，消息用 Pydantic 模型传递。

### P2 阶段六：工程化与部署收尾

1. 落地 Docker Compose 容器化（仓库已有《部署方案_Docker_Compose.md》，补 Dockerfile 并跑通）；补 GitHub Actions CI（lint + 单测 + evals 冒烟）。
2. 多模型路由：统一 OpenAI 兼容客户端，配置化切换 Qwen/DeepSeek；摘要类子任务用小模型降本。
3. 前端流式化：SSE 推送阶段进度与工具调用轨迹；关键结果用动态卡片渲染（生成式 UI 的轻量版本）。
4. 安全加固：工具白名单、数据工具只读、API 限流、.env 密钥不进仓库（已有 .gitignore，保持）。

---

## 五、简历亮点与面试表达建议

JD 明确要求"能清晰说明个人贡献与技术取舍"，建议为每个模块准备一条"取舍叙事"：

1. **固定流水线 → 动态规划**：为什么初版用固定五阶段（可控、可评测、上线快），后来引入 Tool Calling 循环解决什么问题（信息获取灵活性），以及保留阶段骨架的原因（投研流程合规、可解释）。
2. **文件检查点 vs Redis/数据库**：单机场景文件最简且可调试，接口抽象后可平滑替换——体现"按场景做工程取舍"。
3. **关键词方向检测 vs 语义模型**：用规则做假设-数据一致性校验是因为可解释、零成本、可评测；知道其边界（同义词/反讽），并给出升级路径（Embedding 相似度/NLI）。
4. **评测数字**：准备 2-3 个真实对比数字（如：加入真实数据校验后结论-数据矛盾率从 X% 降到 Y%；加入缓存后 P95 时延从 A s 降到 B s）。
5. **MCP 的"为什么"**：工具与 Agent 解耦、跨项目复用、生态兼容——能画出 MCP Client/Server 握手与 tools/list、tools/call 流程。

---

## 六、附：五份 JD 核心要求原文摘录

**JD1 字节·AI 应用开发（客户端）**：构建业务 Agent、MCP 工具与 Skills 体系；基于 AI Coding 建设自动化研发流程；跟进大模型、Agent、Harness 工程；理解 Prompt 工程、RAG、Embedding、向量数据库、Function Calling、Skill、Agent；熟练使用 Claude Code、Codex、Cursor。

**JD2 小红书·AI Agent 工程师（社区工程）**：打造具备深度理解、自主规划、工具使用、长期记忆和持续迭代能力的智能系统；探索 Agentic Post-training、Agentic RL、推理与规划、Agentic Search、Memory/Context Management、个性化及多智能体协作；参与构建下一代 Agent 的 Runtime 与 Harness；建立数据、评测与迭代闭环。

**JD3 小红书·点点 Agent**：任务理解与拆解、规划与推理、多轮上下文管理、长期记忆、Tool 选择与执行、状态管理及异常恢复；推理轨迹构建；工具检索、多工具协同、结果理解及失败恢复；生成式 UI；评测驱动（端到端任务评测、模型与工程专项评测、Tool 评测及真实 Tool 实验环境）；熟悉 Transformer、Prompt、上下文管理、推理规划、Tool Calling、RAG、模型训练或评测。

**JD4 小红书·AI 质效研发**：AI Coding、AI Testing、AI Agent、Agent Harness；覆盖研发、测试设计、用例生成、执行验证、缺陷诊断与质量度量全流程；攻克上下文理解、长程推理、多步骤任务规划；Multi-Agent 协作、AI 自我进化；沉淀最佳实践、技术标准和开发框架。

**JD5 康冠 KTC·Agent 工程师**：Skill、MCP 模块开发；Agent 记忆系统、Reflector 自进化机制；掌握 Transformer 基础原理，了解 Qwen/Gemma/DeepSeek 等 LLM 的部署与微调；Agent Harness 架构或底层框架开发经验；掌握上下文管理、记忆窗口截断、Token 优化；熟悉 vibe coding、云服务平台和容器化部署工具。
