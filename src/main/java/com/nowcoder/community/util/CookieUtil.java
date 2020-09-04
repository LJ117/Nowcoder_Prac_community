package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

    // 返回类型: Cookie 里面, 需要得到的值
    public static String getValue(HttpServletRequest request, String name) {
        if (request ==null||name==null){
            throw new IllegalArgumentException("参数为空!");
        }

        // 从request 中 获取所有 cookie
        Cookie[] cookies = request.getCookies();

        // 要找到确定的cookie , 需要从上面获取到的 cookies 中查找
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
