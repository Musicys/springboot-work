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
        String method = request.getMethod();
        System.out.println("Request URI: " + requestUri + ", Method: " + method);

        // 放行OPTIONS请求（跨域预检请求）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 放行登录和注册接口以及静态资源
        if (requestUri.equals("/api/admin/login") ||
                requestUri.equals("/api/admin/logout") ||
                requestUri.equals("/api/user/login") ||
                requestUri.equals("/api/user/register") ||
                requestUri.equals("/api/pro/login") ||
                requestUri.equals("/api/pro/register") ||
                requestUri.startsWith("/api/file") ||
                requestUri.startsWith("/api/doc.html") ||
                requestUri.startsWith("/api/webjars/") ||
                requestUri.startsWith("/api/v2/api-docs") ||
                requestUri.startsWith("/api/swagger-resources") ||
                requestUri.startsWith("/api/swagger-ui.html")||
                requestUri.startsWith("/api/uploads/")
        ) {
            return true;
        }

        // 根据接口前缀确定对应的Authorization头名称
        String authHeaderName = getAuthHeaderName(requestUri);
        
        // 从请求头中读取token
        String token = request.getHeader(authHeaderName);
        System.out.println(authHeaderName + " header: " + token);

        // 打印所有请求头
        System.out.println("All headers:");
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }

        // 如果请求头中没有token，尝试从Cookie中读取
        if (token == null) {
            System.out.println("Trying to get token from cookies");
            javax.servlet.http.Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                System.out.println("Number of cookies: " + cookies.length);
                for (javax.servlet.http.Cookie cookie : cookies) {
                    System.out.println("Cookie name: " + cookie.getName() + ", value: " + cookie.getValue());
                    if (authHeaderName.equals(cookie.getName())) {
                        token = cookie.getValue();
                        System.out.println("Token from cookie: " + token);
                        break;
                    }
                }
            } else {
                System.out.println("No cookies found");
            }
        }

        // 检查Bearer前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            System.out.println("Token after removing Bearer prefix: " + token);
        }

        if (token == null) {
            System.out.println("Token is null");
            writeErrorResponse(response, ErrorCode.NOT_LOGIN_ERROR);
            return false;
        }

        if (!jwtUtils.validateToken(token)) {
            System.out.println("Token validation failed");
            // 尝试解析token，获取具体的错误信息
            try {
                jwtUtils.parseToken(token);
            } catch (Exception e) {
                System.out.println("Token parsing error: " + e.getMessage());
            }
            writeErrorResponse(response, ErrorCode.NOT_LOGIN_ERROR);
            return false;
        }

        Long userId = jwtUtils.getUserIdFromToken(token);
        Integer userType = jwtUtils.getUserTypeFromToken(token);
        System.out.println("User ID: " + userId + ", User Type: " + userType);

        if (!checkPermission(requestUri, userType)) {
            System.out.println("Permission check failed for URI: " + requestUri + ", User Type: " + userType);
            writeErrorResponse(response, ErrorCode.NO_AUTH_ERROR);
            return false;
        }

        UrUsers user = urUsersService.getById(userId);
        if (user == null || user.getStatus() != 1) {
            System.out.println("User not found or status not active: " + userId);
            writeErrorResponse(response, ErrorCode.NOT_LOGIN_ERROR);
            return false;
        }
        System.out.println("User found: " + user.getUsername() + ", Status: " + user.getStatus());

        UserHolder.setUser(user);
        return true;
    }

    private boolean checkPermission(String requestUri, Integer userType) {

        // 静态资源路径直接放行
        if (requestUri.startsWith("/api/doc.html") ||
                requestUri.startsWith("/api/webjars/") ||
                requestUri.startsWith("/api/v2/api-docs") ||
                requestUri.startsWith("/api/swagger-resources") ||
                requestUri.startsWith("/api/swagger-ui.html") ||
                requestUri.startsWith("/api/file")
        ) {
            return true;
        }
        if (requestUri.startsWith("/api/err")) {
            return true;
        }
        if (requestUri.startsWith("/api/admin/")) {
            return userType == 3;
        } else if (requestUri.startsWith("/api/user/")) {
            return userType == 1;
        } else if (requestUri.startsWith("/api/pro/")) {
            return userType == 2;
        }
        return false;
    }

    /**
     * 根据接口前缀获取对应的Authorization头名称
     * @param requestUri 请求URI
     * @return Authorization头名称
     */
    private String getAuthHeaderName(String requestUri) {
        if (requestUri.startsWith("/api/admin/")) {
            return "Admin-Authorization";
        } else if (requestUri.startsWith("/api/user/")) {
            return "User-Authorization";
        } else if (requestUri.startsWith("/api/pro/")) {
            return "Merchant-Authorization";
        }
        return "Authorization";
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        // 未登录错误返回401状态码
        if (errorCode == ErrorCode.NOT_LOGIN_ERROR) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
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
