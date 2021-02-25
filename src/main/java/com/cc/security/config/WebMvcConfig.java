/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.cc.security.config;

import com.cc.security.interceptor.WebAuthorizationInterceptor;
import com.cc.security.resolver.WebLoginUserHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * web客户端拦截登录
     */
    @Autowired
    private WebAuthorizationInterceptor webAuthorizationInterceptor;
    /**
     * web客户端登录参数转换器
     */
    @Autowired
    private WebLoginUserHandlerMethodArgumentResolver webLoginUserHandlerMethodArgumentResolver;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webAuthorizationInterceptor).addPathPatterns("/**")/*.excludePathPatterns("/weChat/**")*/;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(webLoginUserHandlerMethodArgumentResolver);
    }
}