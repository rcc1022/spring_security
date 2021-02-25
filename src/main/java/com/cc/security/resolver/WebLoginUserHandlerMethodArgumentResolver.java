/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.cc.security.resolver;

import com.cc.security.annotation.LoginUser;
import com.cc.security.entity.UserInfo;
import com.cc.security.interceptor.WebAuthorizationInterceptor;
import com.cc.security.service.UserInfoService;
import com.cc.security.util.RRException;
import com.cc.security.util.SpringContextUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * web客户端 有@LoginUser注解的方法参数，注入当前登录用户
 */
@Component
public class WebLoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
//
//    @Autowired
//    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserInfo.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request, WebDataBinderFactory factory) {
        //获取用户ID
        Object object = request.getAttribute(WebAuthorizationInterceptor.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            return null;
        }
        //获取用户信息
        UserInfoService userService = SpringContextUtils.getBean("userInfoService", UserInfoService.class);
        UserInfo user = userService.getUserInfo((String) object);
//        if (user.getStatus() == 1) {
//            throw new RRException("你已被禁用,请联系客服人员!!!");
//        }
        return user;
    }
}
