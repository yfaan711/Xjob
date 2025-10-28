package com.yfaan.xjob.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    //HandlerInterceptor 是 Spring 框架里用于对请求处理进行拦截的接口
    //HandlerInterceptor 接口有三个主要方法：
    //preHandle：此方法会在处理器执行之前被调用。若返回 true，请求会继续被处理；若返回 false，请求处理流程会中断。
    //postHandle：该方法在处理器执行之后、视图渲染之前被调用，可用于对处理器的返回结果进行修改。
    //afterCompletion：此方法在整个请求处理完成（包括视图渲染）之后被调用，常用于资源清理工作。
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //判断是否需要拦截，依据threadlocal中是否有用户
        if (UserHolder.getUser() == null) {
            //拦截，设置状态码
            response.setStatus(401);
            return false;
        }
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户
        UserHolder.removeUser();
    }
}
