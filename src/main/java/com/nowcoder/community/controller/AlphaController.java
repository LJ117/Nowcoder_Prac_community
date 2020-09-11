package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

//@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    // @ResponseBody , 直接打印， 不是网页跳转
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot.";
    }


    // 模拟查询请求
    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    // 获得请求和响应对象
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());//请求路径
        // # 请求体
        Enumeration<String> enumeration = request.getHeaderNames();// 一个较老的迭代器
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + " : " + value);
        }
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try (
//                Java 7 新增语法，括号中创建，编译自动加 finally，
//                finally中自动关闭writer，
//                前提是 writer 有 close 方法才行
                PrintWriter writer = response.getWriter();
        ) {

            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // GET 请求，默认的请求方式

    // 查询所有学生， /students？current=1&limit=20
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(@RequestParam(name = "current", required = false, defaultValue = "1") int current,
                              @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return " some students ";
    }

    // 根据学生 id，查询指定学生， /student/123 ， 将参数 123 编成路径的一部分
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    //@PathVariable: 从路径中获得变量
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }


    // 提交数据
    // POST 请求方法
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    // 声明与提交表单 名字一致的 变量， 即可完成传参
    public String saveStudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // 响应动态 HTML 数据
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    //不加 @ResponseBody 默认，返回 html
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        // 模板需要多少个数据， add 多少条
        mav.addObject("name", "张三");
        mav.addObject("age", "30");
        // 设置模板的路径 和 名字
        mav.setViewName("/demo/view"); // thymeleaf模板默认文件类型：html
        return mav;
    }

    // 查询学校
    @RequestMapping(path = "/school", method = RequestMethod.GET)
    // 返回 类型： String； 返回 view 的路径
    public String getSchool(Model model) {
        model.addAttribute("name", "MIT");
        model.addAttribute("age", 120);
        return "/demo/view";
    }

    // 响应 JSON 数据（异步请求）
    // Java对象 ---》JSON字符串---》 JS对象

    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody // 要返回 JSON 字符串
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 18);
        emp.put("salary", 8000);
        return emp;
    }

    // 返回多个相似数据
    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 18);
        emp.put("salary", 8000);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "李四");
        emp.put("age", 24);
        emp.put("salary", 9000.0);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "王五");
        emp.put("age", 26);
        emp.put("salary", 13000.0);
        list.add(emp);

        return list;
    }
    // Cookie 示例

    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        // 创建Cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 设置 Cookie 生效范围
        cookie.setPath("/community/alpha");
        // 设置 Cookie 生存时间, 默认 关闭浏览器就消失
        cookie.setMaxAge(60 * 10);// 单位秒
        // 发送 cookie
        response.addCookie(cookie);

        return "set cookie";
    }

    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){
        System.out.println(code);
        return "get cookie";
    }

    // Session 示例
    @RequestMapping(path ="/session/set" , method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id",1);
        session.setAttribute("name","test");
        return "set session";
    }

    @RequestMapping(path ="/session/get" , method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

    // ajax 示例
    // 一般 异步请求,都会提交数据, 故请求选用 POST 方式
    // 异步请求, 直接在当前页面返回 消息, 故 要加上 @ResponseBody 注解
    @RequestMapping(path = "/ajax",method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0,"操作成功");
    }
}
