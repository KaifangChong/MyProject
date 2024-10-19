package com.kc.lesson.interceptor;

import com.google.gson.Gson;
import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.log.entity.OpLog;
import com.kc.lesson.log.service.OpLogService;
import com.kc.lesson.permission.RolePermission;
import com.kc.lesson.permission.RolePermissionLevel;
import com.kc.lesson.user.entity.User;
import com.kc.lesson.user.service.UserService;
import com.kc.lesson.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 拦截器：1.记录请求日志 2.接口权限验证
 */
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);

    private static OpLogService service;

    private static UserService userService;

    @Autowired
    public void setOpLogService(OpLogService service) {
        // 为解决先@Component 后@Autowired失效的方案
        AccessInterceptor.service = service;
    }

    @Autowired
    public void setUserService(UserService userService) {
        // 为解决先@Component 后@Autowired失效的方案
        AccessInterceptor.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        request.setAttribute("_startTime", System.currentTimeMillis());

        // 验权逻辑
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        RolePermission rolePermission = method.getAnnotation(RolePermission.class);
        if (rolePermission == null) {
            // 如果注解为null, 说明不需要拦截, 直接放过
            return true;
        }

        // 管理员
        if (rolePermission.level().getCode() == RolePermissionLevel.ADMIN.getCode()) {
            String token = request.getHeader("ADMINTOKEN");
            logger.info("token==>" + token);
            User user = userService.getUserByToken(token);
            if (user != null && user.getRole().equals(String.valueOf(User.AdminUser))) {
                return true;
            } else {
                KCResponse KCResponse = new KCResponse(ResponseCode.FAIL, "无操作权限");
                writeResponse(response, KCResponse);
                return false;
            }
        }

        // 用户
        if (rolePermission.level().getCode() == RolePermissionLevel.LOGIN.getCode()) {
            String token = request.getHeader("TOKEN");
            logger.info("token==>" + token);
            User user = userService.getUserByToken(token);
            if (user != null && user.getRole().equals(String.valueOf(User.NormalUser))) {
                return true;
            } else {
                KCResponse KCResponse = new KCResponse(ResponseCode.FAIL, "未登录");
                writeResponse(response, KCResponse);
                return false;
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //记录log代码
        Long endTime = System.currentTimeMillis();
        Long startTime = (Long) request.getAttribute("_startTime");
        Long diff = (endTime - startTime);

        OpLog opLog = new OpLog();
        opLog.setIp(IpUtils.getIpAddr(request));
        opLog.setMethod(request.getMethod());
        opLog.setUrl(request.getRequestURI());
        opLog.setUa(request.getHeader(HttpHeaders.USER_AGENT));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        opLog.setTime(formatter.format(new Date()));
        opLog.setAccessTime(String.valueOf(diff));
        service.createOpLog(opLog);
    }

    public void writeResponse(HttpServletResponse response, KCResponse KCResponse) throws IOException {
        response.setStatus(200);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(KCResponse);
        response.getWriter().println(jsonStr);
        response.getWriter().flush();
    }
}
