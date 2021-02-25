/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.cc.security.interceptor;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cc.security.annotation.Login;
import com.cc.security.entity.UserInfo;
import com.cc.security.service.UserInfoService;
import com.cc.security.util.JwtUtil;
import com.cc.security.util.RRException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web端登录 权限(Token)验证
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class WebAuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtil jwtUtils;

    public static final String USER_KEY = "userName";

    @Value("${spring_security.SysCode}")
    private String sysCode;

    @Autowired
    private UserInfoService loginService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Login annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        } else {
            return true;
        }

        if (annotation == null) {
            return true;
        }

        //获取用户凭证
        String token = request.getHeader(jwtUtils.getHeader());
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(jwtUtils.getHeader());
        }

        //凭证为空
        if (ObjectUtils.isEmpty(token)) {
            throw new RRException(jwtUtils.getHeader() + "不能为空", HttpStatus.UNAUTHORIZED.value());
        }

        Claims claims = jwtUtils.getClaimByToken(token);
        if(claims == null || jwtUtils.isTokenExpired(claims.getExpiration())){
            throw new RRException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }

        //验证token是否存在
        String userName = claims.get("sub").toString();
        String userToken = redisTemplate.opsForValue().get("USER_TOKEN:"+userName);
        if (ObjectUtils.isEmpty(userToken)){
            throw new RRException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }
        //获取用户信息
        UserInfo user = loginService.getUserInfo(userName);
        if (user == null) {
            //这里返回后会报出对应异常
            return false;
        }
        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, userName);
        return true;
    }
}
