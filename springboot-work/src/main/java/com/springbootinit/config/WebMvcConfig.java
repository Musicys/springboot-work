package com.springbootinit.config;

import com.springbootinit.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/admin/**", "/api/user/**", "/api/pro/**")
                .excludePathPatterns(
                        "/api/admin/login",
                        "/api/user/login",
                        "/api/user/register",
                        "/api/pro/login",
                        "/api/pro/register"
                );
    }
}
