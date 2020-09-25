package com.nowcoder.community.config;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    // 忽略静态资源
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 授权
        // 注意下面自己对 controller 的路径命名
        // 对下面的路径, 只要拥有以下任意权限即可访问
        http.authorizeRequests()
                .antMatchers(
                        // 用户设置
                        "/user/setting",
                        // 用户头像上传
                        "/user/upload",
                        // 发帖
                        "/discuss/add",
                        // 添加评论
                        "/comment/add/**",
                        // 私信
                        "/letter/**",
                        // 通知
                        "/notice/**",
                        // 点赞
                        "/like",
                        // 关注
                        "/follow",
                        //取关
                        "/unfollow"
                )
                .hasAnyAuthority(
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR
                )
                // 其他的所有请求, 统统都允许[即不指定权限, 都可访问]
                .anyRequest().permitAll()
                // 取消 csrf 认证, 仅仅因为项目演示需求, 如果需要自己在需要配置的: 页面配置即可
                .and().csrf().disable();

        // 权限不足时的处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                          // 未登陆时怎么处理
                          @Override
                          public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                              // 判断当前请求是 同步 还是 异步
                              String xRequestedWith = request.getHeader("x-requested with");
                              if ("XMLHttpRequest".equals(xRequestedWith)) {
                                  // 如果是这个值, 则是异步请求
                                  response.setContentType("application/plain;charset=utf-8");
                                  PrintWriter writer = response.getWriter();
                                  writer.write(CommunityUtil.getJSONString(403, "你还没有登陆哦!"));
                              } else {
                                  response.sendRedirect(request.getContextPath() + "/login");
                              }
                          }
                      }
                )
                .accessDeniedHandler(new AccessDeniedHandler() {
                    // 权限不足的处理
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        // 判断当前请求是 同步 还是 异步
                        String xRequestedWith = request.getHeader("x-requested with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            // 如果是这个值, 则是异步请求
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403, "你没有访问此功能的权限!"));
                        } else {
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        // Security 底层默认会拦截 /logout 请求, 进行退出处理
        // 覆盖 默认逻辑, 才能执行我们自己的逻辑代码
        // 这里写一个不存在的逻辑, 欺骗底层替换它的退出路径, 使得我们自己的退出逻辑"/logout"得以执行
        http.logout().logoutUrl("/securitylogout");

    }
}
