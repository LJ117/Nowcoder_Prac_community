package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断当前拦截的对象是否符合
        // ，注解声明可用的类型【Method】
        // ,Spring MVC 提供的类型：HandlerMethod
        if (handler instanceof HandlerMethod) {
            // 对拦截目标进行转型
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取拦截对象的方法
            Method method = handlerMethod.getMethod();
            // 按照方法取得注解, 反射的形式
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired != null && hostHolder.getUser() == null) {
                // 这个方法是需要登录才能访问的 loginRequired != null,
                // 但是当前用户尚未登录 hostHolder.getUser() == null
                // 此时就返回 false 拒绝后续请求

                // 利用 resp 请求重定向到登录页面
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }

        // 获取当前用户
        User user = hostHolder.getUser();


        return true;
    }
}
