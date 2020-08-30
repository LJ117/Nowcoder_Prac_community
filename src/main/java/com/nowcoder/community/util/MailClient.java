package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 用来完成邮件发送的客户端
 */

// 给一个全bean 注释
@Component
public class MailClient {
    // 记录邮箱的日志
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    // Spring中已经集成了
    // 它的 send 方法,参数类型 MimeMessage
    @Autowired
    private JavaMailSender mailSender;

    // 指定邮件发送者
    // 通过 key 完成 值 的注入
    @Value("${spring.mail.username}")
    private String from;

    //String to: 邮件的接收者 ,
    //String subject: 邮件的主题,
    //String content: 邮件的内容
    public void sendMail(String to, String subject, String content) {
        try {
            // 用 mailSender 来创建 MimeMessage 对象
            MimeMessage message = mailSender.createMimeMessage();
            // 创建 MimeMessageHelper 对象, 用 Helper 配置 MimeMessage
            MimeMessageHelper helper = new MimeMessageHelper(message);
            // 设置 helper 的 发送者, 接收者, 邮件标题, 右键内容
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            // 允许发送的文本支持 html 格式
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());
            System.out.println("send success");
        } catch (MessagingException e) {
            logger.error("发送右键失败" + e.getMessage());
        }
    }
}
