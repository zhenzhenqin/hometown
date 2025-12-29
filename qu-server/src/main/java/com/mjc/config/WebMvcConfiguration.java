package com.mjc.config;

import com.mjc.interceptor.JwtTokenAdminInterceptor;
import com.mjc.interceptor.VisitInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Autowired
    private VisitInterceptor visitInterceptor;

    // 读取配置文件中的硬盘路径
    @Value("${hometown.file.upload-path}")
    private String uploadPath;

    // 读取配置文件中的访问前缀
    @Value("${hometown.file.access-path}")
    private String accessPath;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");

        registry.addInterceptor(visitInterceptor)
                .addPathPatterns("/dailyVisit/ping")
                .excludePathPatterns(
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/images/**"
                );

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
                        "/user/update",
                        "/images/**",
                        "/dailyVisit/**"
                );
    }

    /**
     * 设置静态资源映射
     * 作用：当前端访问 /images/** 时，自动去 D:/Code/Hometown/images/ 下找文件
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始设置静态资源映射...");
        // 映射本地文件
        // 注意：addResourceLocations 必须以 file: 开头
        registry.addResourceHandler(accessPath + "**")
                .addResourceLocations("file:" + uploadPath);

        // 如果你有 Swagger，保留 Swagger 的映射（如果有的话）
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}