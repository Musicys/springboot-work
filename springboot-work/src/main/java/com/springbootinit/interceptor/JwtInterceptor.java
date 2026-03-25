package com.springbootinit.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.model.domain.UrUsers;
import com.springbootinit.service.UrUsersService;
import com.springbootinit.utils.JwtUtils;
import com.springbootinit.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UrUsersService urUsersService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();

        // 从请求头中读取token
        String token = request.getHeader("Authorization");
        
        // 如果请求头中没有token，尝试从Cookie中读取
        if (token == null) {
            javax.servlet.http.Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (javax.servlet.http.Cookie cookie : cookies) {
                    if ("Authorization".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        
        // 检查Bearer前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        if (token == null) {
            writeErrorResponse(response, ErrorCode.NOT_LOGIN_ERROR);
            return false;
        }

        if (!jwtUtils.validateToken(token)) {
            writeErrorResponse(response, ErrorCode.NOT_LOGIN_ERROR);
            return false;
        }

        Long userId = jwtUtils.getUserIdFromToken(token);
        Integer userType = jwtUtils.getUserTypeFromToken(token);

        if (!checkPermission(requestUri, userType)) {
            writeErrorResponse(response, ErrorCode.NO_AUTH_ERROR);
            return false;
        }

        UrUsers user = urUsersService.getById(userId);
        if (user == null || user.getStatus() != 1) {
            writeErrorResponse(response, ErrorCode.NOT_LOGIN_ERROR);
            return false;
        }

        UserHolder.setUser(user);
        return true;
    }

    private boolean checkPermission(String requestUri, Integer userType) {
        if (requestUri.startsWith("/api/admin/")) {
            return userType == 3;
        } else if (requestUri.startsWith("/api/user/")) {
            return userType == 1;
        } else if (requestUri.startsWith("/api/pro/")) {
            return userType == 2;
        }
        return false;
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        BaseResponse<?> baseResponse = new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage());
        writer.write(new ObjectMapper().writeValueAsString(baseResponse));
        writer.flush();
        writer.close();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
