package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    // 接入连接工厂, 才能创建对象
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        // 方法的实例化
        RedisTemplate<String,Object> template = new RedisTemplate<String,Object>();
        // 给对象设置连接工厂
        template.setConnectionFactory(factory);

        // 设置 key 的序列化方式, 参数是 spring 框架的 redis 下的
        // 返回一个能够序列化字符串的序列化器
        template.setKeySerializer(RedisSerializer.string());

        // 设置 value 的序列化方式
        template.setValueSerializer(RedisSerializer.json());

        // 设置 hash 的 key 的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());

        // 设置 hash 的 value 的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());

        // 触发设置结束后 生效
        template.afterPropertiesSet();
        return template;
    }

}
