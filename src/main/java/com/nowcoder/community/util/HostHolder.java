package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户的信息
 * 用户代替 session 对象, 降低服务器压力
 */
@Component
public class HostHolder  {

    private ThreadLocal<User> users = new ThreadLocal<>();
    // ThreadLocal :
    // 以线程[CurrentThread]为 Map 的key 来存取值

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    // 清空ThreadLocal, 释放内存
    public void clear(){
        users.remove();
    }

}
