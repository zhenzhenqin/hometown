package com.mjc.aspect;

import cn.hutool.json.JSONUtil;
import com.mjc.annotation.AutoLog;
import com.mjc.contant.JwtClaimsConstant;
import com.mjc.entity.SysLog;
import com.mjc.mapper.AdminMapper;
import com.mjc.mapper.SysLogMapper;
import com.mjc.mapper.UserMapper;
import com.mjc.properties.JwtProperties;
import com.mjc.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志切面处理类
 * 作用：拦截带有 @AutoLog 注解的方法，记录操作日志到数据库
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 环绕通知 @Around
     * 既可以在目标方法执行前做操作，也可以在执行后做操作
     */
    @Around("@annotation(autoLog)")
    public Object around(ProceedingJoinPoint point, AutoLog autoLog) throws Throwable {
        // 1. 记录开始时间
        long beginTime = System.currentTimeMillis();

        // 2. 执行目标方法 (真正的业务逻辑)
        Object result = point.proceed();

        // 3. 计算耗时
        long time = System.currentTimeMillis() - beginTime;

        // 4. 保存日志 (建议异步保存，防止影响业务性能，这里演示同步保存)
        try {
            saveSysLog(point, autoLog, time);
        } catch (Exception e) {
            log.error("日志记录失败: {}", e.getMessage());
            // 吃掉异常，不要因为日志记录失败导致业务功能回滚
        }

        return result;
    }

    /**
     * 构建并保存日志实体
     */
    private void saveSysLog(ProceedingJoinPoint point, AutoLog autoLog, long time) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();

        // --- 1. 注解上的描述 (如: "新增用户") ---
        if (autoLog != null) {
            sysLog.setOperation(autoLog.value());
        }

        // --- 2. 请求的方法名 (如: com.mjc.controller.UserController.add) ---
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        // --- 3. 请求参数 (过滤掉文件流和Request/Response对象) ---
        Object[] args = point.getArgs();
        try {
            List<Object> arguments = new ArrayList<>();
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse || arg instanceof MultipartFile) {
                    continue;
                }
                arguments.add(arg);
            }
            // 序列化参数，限制长度防止数据库报错
            String params = JSONUtil.toJsonStr(arguments);
            sysLog.setParams(params.length() > 2000 ? params.substring(0, 2000) + "..." : params);
        } catch (Exception e) {
            sysLog.setParams("参数解析失败");
        }

        // --- 4. 获取 IP 和 用户名 ---
        HttpServletRequest request = getHttpServletRequest();
        if (request != null) {
            sysLog.setIp(getIpAddr(request));

            //  Token，解析 Token 获取用户名
            // 从请求头中获取token
            String token = request.getHeader("token");
            //解析token 获取用户名
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Integer adminId= (Integer) claims.get(JwtClaimsConstant.ADMIN_ID);

            //根据adminId 查询用户名
            if (adminId != null) {
                String username = adminMapper.getById(adminId).getUsername();
                sysLog.setUsername(username);
            }

        }

        // --- 5. 其他信息 ---
        sysLog.setCreateTime(LocalDateTime.now());
        sysLog.setTime(time);


        // --- 6. 插入数据库 ---
        sysLogMapper.insert(sysLog);
    }

    /**
     * 获取当前请求对象
     */
    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 获取真实IP地址 (处理 Nginx 代理情况)
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}