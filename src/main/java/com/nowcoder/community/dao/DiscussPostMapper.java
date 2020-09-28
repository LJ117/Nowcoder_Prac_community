package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    // 延展到 将来查询自己的所有帖子, 所以增加参数 userId,
    // 如果只是获取所有讨论内容, 可以无参
    // 增加分页考虑: MySQL 用的是 limit 方法 需要两个参数: int offset[每页起始行, 行号], int limit[每页最多显示条数]
    List<DiscussPost> selectDiscussPosts(int userId,int offset, int limit, int orderMode);

    // 查询帖子行数
    // @Parm 注解用于给参数取别名
    // 如果需要动态 sql  <if> 里面使用, 并且该方法有且只有一个参数, 这个参数必须取别名
    int selectDiscussPostRows(@Param("userId") int userId);


    // 增加帖子, 发布新帖
    // 唯一参数: 帖子对象
    int insertDiscussPost(DiscussPost discussPost);

    // 根据帖子的 id 查询帖子详情
    DiscussPost selectDiscussPostById(int id);

    /**
     * 冗余更新帖子评论总数
     * @param id : 帖子Id
     * @param commentCount : 评论数量
     * @return
     */
    int updateCommentCount(int id, int commentCount);

    // 修改帖子类型
    int updateType(int id, int type);

    // 修改帖子状态
    int updateStatus(int id,int status);

    // 修改帖子分数
    int updateScore(int id,double score);
}
