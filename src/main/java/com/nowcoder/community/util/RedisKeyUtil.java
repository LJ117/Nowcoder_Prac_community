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
    // 被关注用户的前缀
    private static final String PREFIX_FOLLOWEE = "followee";
    // 粉丝的前缀
    private static final String PREFIX_FOLLOWER = "follower";


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

    // 某个用户关注的实体, userId: 谁关注的
    // followee:userId:entityType -> ZSet(entityId, now): now: 时间的整数形式
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个用户拥有的粉丝
    // follower:entityType:entityId -> ZSet(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

}
