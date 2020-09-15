package com.nowcoder.community.util;

/**
 * Redis 工具类 用于获取 Redis 的点赞的 key
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    // 点赞实体 前缀
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    // 用户点赞的前缀
    private static final String PREFIX_USER_LIKE = "like:user";

    // 某个实体的赞
    // 示例: like:entity:entityType:entityId -> set{userId}
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某一个用户获得的赞
    // 示例: like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

}
