package com.kc.lesson.handler;


import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.log.entity.ErrorLog;
import com.kc.lesson.log.service.ErrorLogService;
import com.kc.lesson.utils.HttpContextUtils;
import com.kc.lesson.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @Autowired
    private ErrorLogService service;

    @ExceptionHandler(Exception.class)
    public KCResponse handleException(Exception ex) {
        logger.error("Error log: {}", ex.getMessage(), ex);
        saveErrorLog(ex);
        return new KCResponse(ResponseCode.FAIL, ex.getMessage());
    }

    /**
     * 保存异常日志
     */
    private void saveErrorLog(Exception ex) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

        ErrorLog errorLog = new ErrorLog();
        errorLog.setIp(IpUtils.getIpAddr(request));
        errorLog.setUrl(request.getRequestURI());
        errorLog.setMethod(request.getMethod());

        // 异常信息
        errorLog.setContent(ex.getClass().getName() + ": " + ex.getMessage());
        errorLog.setLogTime(String.valueOf(System.currentTimeMillis()));

        // 保存日志
        service.createErrorLog(errorLog);
    }
}
