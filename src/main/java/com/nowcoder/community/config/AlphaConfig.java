package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

//这个注解@Configuration：这是一个配置类 ，定义第三方的Bean
@Configuration
public class AlphaConfig {

    @Bean
    //Bean 的名字就是方法名
    // 这个方法返回的 对象【SimpleDateFormat类型】，将被装配到 Bean 容器中
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

}


