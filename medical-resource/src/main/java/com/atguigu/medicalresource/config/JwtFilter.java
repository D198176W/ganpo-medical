package com.atguigu.medicalresource.config;


import com.atguigu.medicalresource.dto.ApiResponse;
import com.atguigu.medicalresource.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.atguigu.medicalresource.service.UserService;
import com.atguigu.medicalresource.util.JwtUtil;



import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;



@Component
public class JwtFilter implements Filter {
    @Autowired
    private JwtUtil jwtUtil;


    private final ObjectMapper objectMapper = new ObjectMapper();

    // 存储需要排除的 URL
    private String excludedUrls = "/api/auth/login,/api/auth/register,/api/departments,/api/hospitals,/api/pharmacies,/api/reimbursement/calculate";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        System.out.println("===== 我的 JWT 过滤器执行了 =====");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        // 检查当前请求路径是否需要排除
        String requestUri = request.getRequestURI();

        if (isExcluded(requestUri)) {
            // 如果请求路径在排除列表中，则直接放行
            System.out.println("请求路径在排除列表中，直接放行：" + requestUri);
            chain.doFilter(request, response);
            return;
        }

        //获取请求头中的 Authorization 字段
        String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);
        // 1. 没带 token，直接拦截
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeFailResponse(response, "未登录，请先登录");
            return;
        }
        //检查是否是有效的Authorization字段
        String token = authHeader.substring(7);
        try {
            //验证token有效性
            String username = jwtUtil.parseToken(token);
            if (username != null) {
                // 将用户名放入 request attribute， service层可取出
                request.setAttribute("currentUser", username);
            }
        } catch (Exception e) {
            writeFailResponse(response, "Token 验证失败");
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * 判断请求路径是否在排除列表中
     */
    private boolean isExcluded(String requestUri) {
        if (excludedUrls == null || excludedUrls.isEmpty()) {
            return false;
        }
        String[] urls = excludedUrls.split(",");
        for (String url : urls) {
            String trimmedUrl = url.trim();
            if (!trimmedUrl.isEmpty() && requestUri.contains(trimmedUrl)) {
                return true;
            }
        }
        return false;
    }

/**
 * 写入失败响应的方法
 * @param response HttpServletResponse对象，用于向客户端返回响应
 * @param message 失败的具体信息，将包含在返回的响应体中
 * @throws IOException 当写入响应时发生I/O异常时抛出
 */
    private void writeFailResponse(HttpServletResponse response, String message) throws IOException {
    // 设置HTTP响应状态码为401未授权
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    // 设置响应内容类型为JSON，并指定字符编码为UTF-8
        response.setContentType("application/json;charset=UTF-8");
    // 将失败信息通过ObjectMapper转换为JSON格式并写入响应输出流
        response.getWriter().write(
                objectMapper.writeValueAsString(ApiResponse.fail(message))
        );
    }


//    @Autowired
//    private UserService userService;
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            try {
//                String username = jwtUtil.parseToken(token);
//                if (username != null) {
//                    // 将用户名放入 request attribute，service 层可取出
//                    request.setAttribute("currentUser", username);
//                }
//            } catch (Exception e) {
//                // token 异常：忽略，这样接口可选择是否需要鉴权
//            }
//        }
//        chain.doFilter(request, response);
//    }

}
