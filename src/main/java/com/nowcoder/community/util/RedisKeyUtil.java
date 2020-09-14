package com.nowcoder.community.util;

/**
 * Redis 工具类 用于获取 Redis 的点赞的 key
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    // 点赞实体 前缀
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    // 某个实体的赞
    // 示例: like:entity:entityType:entityId -> set{userId}
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

}
