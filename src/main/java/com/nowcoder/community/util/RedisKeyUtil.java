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
    // 验证码前缀
    private static final String PREFIX_KAPTCHA = "kaptcha";
    // 登陆凭证前缀
    private static final String PREFIX_TICKET = "ticket";
    // 用户前缀
    private static final String PREFIX_USER = "user";
    // 统计 UV 相关前缀
    private static final String PREFIX_UV = "uv";
    // 统计 DAU 相关前缀
    private static final String PREFIX_DAU = "dau";
    // 计算 热帖 分数的 前缀
    private static final String PREFIX_POST = "post";


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

    // 登陆验证码
    // String owner: 验证码临时凭证, 用于临时标识用户
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    // 单日UV
    public static String getUVKey(String date) {
        // 传入的时间参数, 只用 年月日 就好
        return PREFIX_UV + SPLIT + date;
    }

    // 区间UV [从 X天 到 Y天 的总UV ]
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    // 单日 DAU 活跃用户
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    // 区间活跃用户
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    // 帖子分数
    public static final String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }
}
