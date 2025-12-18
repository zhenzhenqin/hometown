package com.mjc.config;

import com.mjc.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/**")  // 1. 拦截所有请求
                .excludePathPatterns(    // 2. 排除不需要登录的接口
                        "/admin/login",        // 登录接口必须排除
                        "/captchaImage",       // 验证码接口必须排除
                        "/doc.html",           // Swagger/Knife4j 文档相关
                        "/webjars/**",
                        "/swagger-resources",
                        "/v3/api-docs/**",
                        "/attractions/all",
                        "/cultures/all",
                        "/specialties/all",
                        "/user/login",
                        "/user/register",
                        "/user/{id}",
                        "/attractions/likes/{id}",
                        "/attractions/dislikes/{id}",
                        "/upload",
                        "/user/update"
                );
    }
}