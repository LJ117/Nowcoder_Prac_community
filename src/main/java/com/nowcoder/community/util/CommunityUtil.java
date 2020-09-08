package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
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

    /**
     * 返回 JSON 字符串
     *
     * @param code 状态码编号,一定有
     * @param msg  提示信息【可选】
     * @param map  具体业务数据【可选】
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        // 用 fastjson 依赖生成一个 json 对象
        JSONObject json = new JSONObject();
        // 往 json 中传值
        json.put("code", code);
        json.put("msg", msg);

        // map 对象需要打散成每一个键值对, 才会进行封装
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();

    }

    // 根据参数的可选，进行函数重载
    public static String getJSONString(int code, String msg) {
        return getJSONString(code,msg,null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code,null,null);
    }

    public static void main(String[] args) {
        Map<String,Object> map= new HashMap<>();
        map.put("name","张三");
        map.put("age",25);
        System.out.println(getJSONString(0,"ok",map));
    }
}
