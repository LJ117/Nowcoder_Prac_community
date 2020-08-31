package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    // 1. 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // 2. MD5 加密
    // MD5特征：只能加密不能解密
    // hello -> abc123def456
    // hello + salt(3e4a8) -> abc123def456asv; 提升数据安全性
    public static String md5(String key) {
        // key: 原始密码 + salt
        // 用 lang3 包里的 isBlank 函数 完成对 key 的判空
        if (StringUtils.isBlank(key)) {
            return null;
        }
        // 用 Spring 自带 MD5 加密工具完成 加密
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
