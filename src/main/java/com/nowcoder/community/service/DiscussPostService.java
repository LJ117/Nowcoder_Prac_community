package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.SensitiveFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    // 查询某页的数据
    public List<DiscussPost> findDiscussPosts(int userId,int offset, int limit){
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }


    // 查询行数
    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }


    // 添加帖子
    public int addDiscussPost(DiscussPost post){
        // 对帖子判空
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 对帖子 标题 与 内容 进行过滤
        // 去掉网页标签, 将 标题与内容 处理成普通文本
        // 转义 HTML 标记, 去掉 html 标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

        // 敏感词过滤
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        // 插入数据, 发布新贴
        return  discussPostMapper.insertDiscussPost(post);
    }


    // 帖子查询
    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }

    // 冗余更新帖子评论总数
    public int updateCommentCount(int id,int commentCount){
            return discussPostMapper.updateCommentCount(id,commentCount);
    }

}
