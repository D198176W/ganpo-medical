package com.atguigu.medicalresource.config;

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

    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtUtil.parseToken(token);
                if (username != null) {
                    // 将用户名放入 request attribute，service 层可取出
                    request.setAttribute("currentUser", username);
                }
            } catch (Exception e) {
                // token 异常：忽略，这样接口可选择是否需要鉴权
            }
        }
        chain.doFilter(request, response);
    }
}
