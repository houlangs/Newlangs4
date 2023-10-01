package com.guaning.newlangs.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.guaning.newlangs.config.RequestLimit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Component
public class RequestLimitIntercept implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RequestLimit methodAnnotation = method.getAnnotation(RequestLimit.class);
            RequestLimit classAnnotation = method.getDeclaringClass().getAnnotation(RequestLimit.class);
            RequestLimit requestLimit = methodAnnotation != null ? methodAnnotation : classAnnotation;
            if (requestLimit != null) {
                if (isLimit(request, requestLimit)) {
                    resonseOut(response, SaResult.error("访问受限"));
                    return false;
                }
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    public boolean isLimit(HttpServletRequest request, RequestLimit requestLimit) {
        String limitKey = StpUtil.getLoginIdAsString();
        Integer redisCount = (Integer) redisTemplate.opsForValue().get(limitKey);
        if (redisCount == null) {
            redisTemplate.opsForValue().set(limitKey, 1, requestLimit.second(), TimeUnit.SECONDS);
        } else {
            if (redisCount >= requestLimit.maxCount()) {
                return true;
            }
            redisTemplate.opsForValue().increment(limitKey);
        }
        return false;
    }

    /**
     * @param response
     * @param result
     * @throws IOException
     */
    private void resonseOut(HttpServletResponse response, SaResult result) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        String json = JSONObject.toJSON(result).toString();
        out = response.getWriter();
        out.append(json);
    }
}
