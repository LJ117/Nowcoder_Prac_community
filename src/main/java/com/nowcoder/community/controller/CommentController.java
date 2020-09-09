package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        // 利用 Hostholder 获取 当前评论用户, 用当前用户对象完善 Comment 的 UserId
        comment.setUserId(hostHolder.getUser().getId());
        // 默认有效
        comment.setStatus(0);
        // 完善评论时间
        comment.setCreateTime(new Date());

        commentService.addComment(comment);


        return "redirect:/discuss/detail/" + discussPostId;
    }

}
