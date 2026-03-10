# you-ai-agent

一个基于 **Spring Boot 3 + Spring AI** 的 AI 智能体/应用示例项目，包含：

- **旅游助手应用 `LoveApp`**：支持普通对话、SSE 流式对话、结构化输出、RAG（向量检索增强）、工具调用、MCP 工具调用
- **超级智能体 `YouManus`**：基于 ReAct 思路的多步推理 + 工具调用（SSE 输出每一步）
- **内置工具集**：网页搜索、 网页抓取、资源下载、文件操作、终端操作、PDF 生成、终止任务

---

## 技术栈

- **Java**：21
- **Spring Boot**：3.4.11
- **Spring AI**：Alibaba DashScope Starter（Qwen）+ MCP Client + PgVector（可选）
- **API 文档**：springdoc-openapi + Knife4j
- **PDF**：iText（kernel/layout/font-asian）

---

## 快速开始

### 1) 环境要求

- JDK 21
- Maven 3.9+
- （可选）PostgreSQL + pgvector（如果你要启用向量库相关能力）
- （可选）一个 MCP Server（如果你要启用 MCP 工具调用）

### 2) 配置

项目默认启用 `local` profile：

```yaml
spring:
  profiles:
    active: local
```

本地配置文件：`src/main/resources/application-local.yml`（你需要根据自己的环境修改）

关键配置项：

- **DashScope**
  - `spring.ai.dashscope.api-key`：大模型 API Key
  - `spring.ai.dashscope.chat.options.model`：模型（示例中为 `qwen3-max`）
- **Search API（用于百度搜索工具）**
  - `search-api.api-key`：`searchapi.io` 的 api key
- **MCP Client（可选）**
  - `spring.ai.mcp.client.sse.connections.server1.url`：MCP SSE server 地址（示例 `http://localhost:8127`）
- **数据源（可选，向量库/RAG 相关）**
  - `spring.datasource.*`：PostgreSQL 连接信息

> 注意：`application-local.yml` 里可能包含敏感信息（key/password），建议本地用环境变量或私有配置文件覆盖，不要提交到公网仓库。

### 3) 启动

在项目根目录执行：

```bash
mvn spring-boot:run
```

默认服务地址：

- **端口**：`8123`
- **Context Path**：`/api`

API 文档：

- Knife4j：`/api/swagger-ui.html`
- OpenAPI JSON：`/api/v3/api-docs`

---

## 接口说明（核心）

控制器：`com.you.youaiagent.controller.AIController`

### 1) 旅游助手（LoveApp）

- **同步对话**
  - `GET /api/ai/love_app/chat/sync?message=...&chatId=...`

- **SSE（Flux<String>）**
  - `GET /api/ai/love_app/chat/sse?message=...&chatId=...`

- **SSE（ServerSentEvent）**
  - `GET /api/ai/love_app/chat/server_sent_event?message=...&chatId=...`

- **SSE（SseEmitter）**
  - `GET /api/ai/love_app/chat/sse_emitter?message=...&chatId=...`

### 2) 超级智能体（YouManus，多步 + 工具调用）

- `GET /api/ai/manus/chat?message=...`

说明：

- 智能体会按步骤执行（最多 20 步），每一步可能调用 0~N 个工具
- SSE 输出形如：
  - `Step1:工具:searchWeb返回的结果：...`
  - `StepN:工具:doTerminate返回的结果："任务结束"`

---

## 工具清单

集中注册在：`com.you.youaiagent.tools.ToolRegistration`

- `WebSearchTool`：网页搜索（SearchAPI + 百度引擎）
- `WebScrapingTool`：网页内容抓取
- `ResourceDownloadTool`：资源下载
- `FileOperationTool`：文件操作
- `TerminalOperationTool`：终端操作
- `PDFGenerationTool`：PDF 生成（iText）
- `TerminateTool`：终止任务

---

## 常见问题（FAQ）

### 1) `Error searching Baidu: toIndex = 5`

原因：搜索结果不足 5 条时，代码截取 `subList(0, 5)` 越界。

修复：已在 `WebSearchTool` 做了长度保护（取 `min(5, size)`）。

### 2) `generatePDF` 报 `Unresolved compilation problems: PdfWriter cannot be resolved...`

原因：iText 依赖未正确引入实现 jar（只引入聚合 POM 不能提供 `PdfWriter/PdfDocument/Document` 等类）。

修复：`pom.xml` 已改为引入：

- `com.itextpdf:kernel`
- `com.itextpdf:layout`
- `com.itextpdf:font-asian`

### 3) 启动时报 `McpError: Failed to wait for the message endpoint / ConnectException`

原因：MCP Client 在启动期初始化连接，目标 MCP SSE 服务不可达（例如 `http://localhost:8127` 未启动）。

处理方式（二选一）：

- **启动 MCP Server**（确保地址可访问）
- **关闭 MCP 相关配置/依赖**（如果当前不需要 MCP）

---

## 测试

```bash
mvn test
```

示例测试：

- `WebSearchToolTest`
- `PDFGenerationToolTest`

---

## 目录结构（简）

```
src/main/java/com/you/youaiagent
  agent/         # 智能体框架（BaseAgent / ReActAgent / ToolCallAgent / YouManus）
  app/           # 应用（LoveApp）
  controller/    # 接口（AIController）
  tools/         # 工具实现与注册（ToolRegistration 等）
src/main/resources
  application.yml
  application-local.yml
  mcp-servers.json
```

