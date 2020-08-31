package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

// 不加 @requestMapping , 访问的直接是方法
@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        // 返回的 map 是空的, 就是注册成功
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功, 已向您的邮箱发送激活邮件, 请尽快激活!");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            // 注册失败回到注册页面
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }

    }

    //  http://localhost:8080/community/activation/${userId}/code
    @RequestMapping(path = "activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功, 您的账号已经可以正常使用");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作, 您的账号已经激活过");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败, 您提供的激活码不可用");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }
}
