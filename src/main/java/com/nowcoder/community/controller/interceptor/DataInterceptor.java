package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DataService;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理用户 UV 和 DAU
 */
@Component
public class DataInterceptor implements HandlerInterceptor {

    @Autowired
    private DataService dataService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 统计 UV
        String ip = request.getRemoteHost();
        dataService.recordUV(ip);

        // 统计 DAU,
        // 即记录当前登录用户
        User user = hostHolder.getUser();
        if (user != null) {
            // 只有用户登录了才记录
            dataService.recordDAU(user.getId());
        }

        return true;
    }
}
