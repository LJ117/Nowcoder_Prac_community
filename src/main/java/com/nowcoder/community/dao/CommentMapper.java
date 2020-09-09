package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 回复详情有分页需求

    // 更具实体类型返回:
    //  帖子本身的评论, 评论的评论
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    // 返回查询数据的条目数
    int selectCountByEntity(int entityType, int entityId);

    // 增加评论, 返回新增评论行数
    int insertComment(Comment comment);
}
