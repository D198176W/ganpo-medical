package com.atguigu.consult.mcp;

import com.atguigu.consult.entity.Appointment;
import com.atguigu.consult.tools.AppointmentTools;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * MCP Server：将预约工具体系以 Model Context Protocol 标准暴露
 *
 * 实现"工具与 Agent 解耦、可插拔、可复用"：
 * - 传输层：HTTP/SSE（GET /mcp/sse 建立事件流，POST /mcp/message 收发 JSON-RPC）
 * - 协议握手：initialize → tools/list 动态发现 → tools/call 执行
 * - 任何 MCP Client（Claude Desktop、Cline、其他 Agent 系统）均可直接接入调用，
 *   新增工具只需在此注册，Agent 侧零改动
 */
@Configuration
public class McpServerConfig {

    private static final Logger log = LoggerFactory.getLogger(McpServerConfig.class);

    /** SSE 传输层：/mcp/sse（事件流）+ /mcp/message（JSON-RPC 消息） */
    @Bean
    public HttpServletSseServerTransportProvider mcpTransportProvider(ObjectMapper objectMapper) {
        return new HttpServletSseServerTransportProvider(objectMapper, "/mcp/message", "/mcp/sse");
    }

    /** 将 MCP 传输层注册为 Servlet */
    @Bean
    public ServletRegistrationBean<HttpServletSseServerTransportProvider> mcpServletRegistration(
            HttpServletSseServerTransportProvider transportProvider) {
        ServletRegistrationBean<HttpServletSseServerTransportProvider> registration =
                new ServletRegistrationBean<>(transportProvider, "/mcp/*");
        registration.setLoadOnStartup(1);
        registration.setName("mcp-server");
        return registration;
    }

    /** MCP Server：注册预约挂号工具三件套（tools/list 动态发现） */
    @Bean(destroyMethod = "close")
    public McpSyncServer mcpSyncServer(HttpServletSseServerTransportProvider transportProvider,
                                       AppointmentTools appointmentTools) {

        String queryDeptSchema = """
                {
                  "type": "object",
                  "properties": {
                    "department": { "type": "string", "description": "科室名称" },
                    "date": { "type": "string", "description": "日期，格式 YYYY-MM-DD" },
                    "time": { "type": "string", "description": "时段：上午/下午" },
                    "doctorName": { "type": "string", "description": "医生姓名（可选）" }
                  },
                  "required": ["department", "date", "time"]
                }
                """;

        String appointmentSchema = """
                {
                  "type": "object",
                  "properties": {
                    "username": { "type": "string", "description": "患者姓名" },
                    "idCard": { "type": "string", "description": "身份证号（18位）" },
                    "department": { "type": "string", "description": "科室名称" },
                    "date": { "type": "string", "description": "日期，格式 YYYY-MM-DD" },
                    "time": { "type": "string", "description": "时段：上午/下午" },
                    "doctorName": { "type": "string", "description": "医生姓名（可选）" }
                  },
                  "required": ["username", "idCard", "department", "date", "time"]
                }
                """;

        McpSchema.Tool queryDepartmentTool = new McpSchema.Tool(
                "query-department", "查询指定科室/日期/时段是否还有号源及剩余数量", queryDeptSchema);
        McpSchema.Tool bookAppointmentTool = new McpSchema.Tool(
                "book-appointment", "为患者预约挂号（需完整预约信息）", appointmentSchema);
        McpSchema.Tool cancelAppointmentTool = new McpSchema.Tool(
                "cancel-appointment", "取消已有预约挂号（需姓名+身份证号验证）", appointmentSchema);

        McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo("ganpo-medical-tools", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder().tools(true).build())
                .tools(
                        new McpServerFeatures.SyncToolSpecification(queryDepartmentTool, (exchange, args) -> {
                            String result = appointmentTools.queryDepartment(
                                    str(args, "department"), str(args, "date"),
                                    str(args, "time"), str(args, "doctorName"));
                            return new McpSchema.CallToolResult(result, false);
                        }),
                        new McpServerFeatures.SyncToolSpecification(bookAppointmentTool, (exchange, args) -> {
                            String result = appointmentTools.bookAppointment(toAppointment(args));
                            return new McpSchema.CallToolResult(result, false);
                        }),
                        new McpServerFeatures.SyncToolSpecification(cancelAppointmentTool, (exchange, args) -> {
                            String result = appointmentTools.cancelAppointment(toAppointment(args));
                            return new McpSchema.CallToolResult(result, false);
                        })
                )
                .build();

        log.info("MCP Server 启动成功: ganpo-medical-tools v1.0.0，端点 GET /mcp/sse, POST /mcp/message");
        return server;
    }

    private static String str(Map<String, Object> args, String key) {
        Object v = args.get(key);
        return v == null ? null : v.toString();
    }

    private static Appointment toAppointment(Map<String, Object> args) {
        Appointment appointment = new Appointment();
        appointment.setUsername(str(args, "username"));
        appointment.setIdCard(str(args, "idCard"));
        appointment.setDepartment(str(args, "department"));
        appointment.setDate(str(args, "date"));
        appointment.setTime(str(args, "time"));
        appointment.setDoctorName(str(args, "doctorName"));
        return appointment;
    }
}
